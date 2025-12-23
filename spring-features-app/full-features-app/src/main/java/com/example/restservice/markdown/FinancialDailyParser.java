package com.example.restservice.markdown;

/**
 * @className: FinancialDailyParser
 * @author: geeker
 * @date: 8/12/25 3:52 PM
 * @Version: 1.0
 * @description:
 */
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinancialDailyParser {

    public static void main(String[] args) throws IOException {
        String path = "/home/geeker/arch/projs/java/spring_mesh/spring-cli_dev/demo/rest-service/src/main/resources/report.md";
        String rawContent = Files.readString(Paths.get(path));
        FinancialDailyParser financialDailyParser = new FinancialDailyParser();
        FinancialDaily financialDaily = financialDailyParser.parse(rawContent);
        System.out.println(financialDaily);
    }

    private static final Pattern DATE_PATTERN = Pattern.compile("# (.+日报) - (.+)");
    private static final Pattern DATA_CUTOFF_PATTERN = Pattern.compile("数据截止：(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} UTC\\+8)");
    private static final Pattern SECTION_PATTERN = Pattern.compile("## ([^\\n]+)");
    private static final Pattern SUBSECTION_PATTERN = Pattern.compile("### ([^\\n]+)"); // 这只是获取title,content 丢失！
    private static final Pattern SUBSECTION_PATTERN_ADV = Pattern.compile("###\s(.+?)\n+([^#]+)");

    private static final Pattern LIST_ITEM_PATTERN = Pattern.compile("[+\\-] (.+)");
    private static final Pattern BOLD_TEXT_PATTERN = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static final Pattern TABLE_PATTERN = Pattern.compile("\\|(.+)\\|[\\s\\S]+?\\|[-:| ]+\\|[\\s\\S]+?(\\|.+\\|)");
    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile("```([\\s\\S]+?)```");
    private static final Pattern QUOTE_PATTERN = Pattern.compile("> \"(.+?)\"");

    public FinancialDaily parse(String markdown) {
        FinancialDaily daily = new FinancialDaily();

        // 解析标题和日期
        Matcher dateMatcher = DATE_PATTERN.matcher(markdown);
        if (dateMatcher.find()) {
            daily.setTitle(dateMatcher.group(1));
            daily.setDate(dateMatcher.group(2));
        }

        // 解析数据截止时间
        Matcher cutoffMatcher = DATA_CUTOFF_PATTERN.matcher(markdown);
        if (cutoffMatcher.find()) {
            daily.setDataCutoff(cutoffMatcher.group(1));
        }

        // 分割并解析各个部分
        String[] sections = markdown.split("------");
        for (String section : sections) {
            parseSection(section.trim(), daily);
        }

        return daily;
    }

    private void parseSection(String sectionText, FinancialDaily daily) {
        Matcher sectionMatcher = SECTION_PATTERN.matcher(sectionText);
        if (!sectionMatcher.find()) return;

        String sectionTitle = sectionMatcher.group(1).trim();
        Section section = new Section(sectionTitle);
        SubSection subSection = new SubSection();

        Matcher subSectionMatcher = SUBSECTION_PATTERN_ADV.matcher(sectionText);
        while (subSectionMatcher.find()) {
            String title = subSectionMatcher.group(1).trim();
            String content = subSectionMatcher.group(2).trim();
            subSection.setTitle(title);
            parseContent(content, subSection);
        }


        // 如果没有子部分，直接解析内容
        /*if (subSections.length == 1) {
            parseContent(sectionText.substring(sectionMatcher.end()).trim(), subSection);
        }*/
        section.addSubSection(subSection);
        daily.addSection(section);
    }

    private void parseSubSection(String subSectionText, Section section) {
        // todo: 上面split后应该是没有### -> ### 丢失导致正则匹配失效 -> 直接丢弃[0]即可
        Matcher subSectionMatcher = SUBSECTION_PATTERN.matcher(subSectionText);
        //if (!subSectionMatcher.find()) return;

        while (subSectionMatcher.find()) {
            // 获取标题
            String subSectionTitle = subSectionMatcher.group(1).trim();
            SubSection subSection = new SubSection(subSectionTitle);

            parseContent(subSectionText.substring(subSectionMatcher.end()).trim(), subSection);

            section.addSubSection(subSection);
        }

    }

    private void parseContent(String content, SectionElement element) {
        // 解析列表
        Matcher listMatcher = LIST_ITEM_PATTERN.matcher(content);
        while (listMatcher.find()) {
            element.addListItem(parseListItem(listMatcher.group(1)));
        }

        // 解析表格
        Matcher tableMatcher = TABLE_PATTERN.matcher(content);
        while (tableMatcher.find()) {
            element.addTable(parseTable(tableMatcher.group(0)));
        }

        // 解析代码块
        Matcher codeMatcher = CODE_BLOCK_PATTERN.matcher(content);
        while (codeMatcher.find()) {
            element.addCodeBlock(codeMatcher.group(1).trim());
        }

        // 解析引用
        Matcher quoteMatcher = QUOTE_PATTERN.matcher(content);
        while (quoteMatcher.find()) {
            element.addQuote(quoteMatcher.group(1).trim());
        }

        // 解析粗体文本
        Matcher boldMatcher = BOLD_TEXT_PATTERN.matcher(content);
        while (boldMatcher.find()) {
            element.addBoldText(boldMatcher.group(1).trim());
        }

        // 添加普通文本
        String plainText = content.replaceAll(LIST_ITEM_PATTERN.pattern(), "")
                .replaceAll(TABLE_PATTERN.pattern(), "")
                .replaceAll(CODE_BLOCK_PATTERN.pattern(), "")
                .replaceAll(QUOTE_PATTERN.pattern(), "")
                .replaceAll(BOLD_TEXT_PATTERN.pattern(), "")
                .trim();
        if (!plainText.isEmpty()) {
            element.addPlainText(plainText);
        }
    }

    private Table parseTable(String tableText) {
        Table table = new Table();
        String[] lines = tableText.split("\n");

        // 解析表头
        String[] headers = lines[0].split("\\|");
        for (String header : headers) {
            if (!header.trim().isEmpty()) {
                table.addHeader(header.trim());
            }
        }

        // 解析数据行
        for (int i = 2; i < lines.length; i++) {
            String[] cells = lines[i].split("\\|");
            List<String> row = new ArrayList<>();
            for (String cell : cells) {
                if (!cell.trim().isEmpty()) {
                    row.add(cell.trim());
                }
            }
            if (!row.isEmpty()) {
                table.addRow(row);
            }
        }

        return table;
    }

    private ListItem parseListItem(String itemText) {
        // 解析带链接的列表项
        if (itemText.contains("[")) {
            Pattern linkPattern = Pattern.compile("(.+?)\\[(.+?)\\]");
            Matcher linkMatcher = linkPattern.matcher(itemText);
            if (linkMatcher.find()) {
                return new ListItem(linkMatcher.group(1).trim(), linkMatcher.group(2).trim());
            }
        }
        return new ListItem(itemText, null);
    }

    // 数据模型类
    @Data
    public static class FinancialDaily {
        private String title;
        private String date;
        private String dataCutoff;
        private List<Section> sections = new ArrayList<>();

        // getters and setters
        public void addSection(Section section) { sections.add(section); }
    }

    @Data
    public static class Section {
        private String title;
        private List<SubSection> subSections = new ArrayList<>();
        // private SectionContent content = new SectionContent();

        public Section(String title) { this.title = title; }
        public void addSubSection(SubSection subSection) { subSections.add(subSection); }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SubSection extends SectionElement {
        public SubSection() { }
        private String title;

        public SubSection(String title) { this.title = title; }
    }

    @Data
    public static class SectionElement {
        private List<ListItem> listItems = new ArrayList<>();
        private List<Table> tables = new ArrayList<>();
        private List<String> codeBlocks = new ArrayList<>();
        private List<String> quotes = new ArrayList<>();
        private List<String> boldTexts = new ArrayList<>();
        private List<String> plainTexts = new ArrayList<>();

        // 添加内容的方法
        public void addListItem(ListItem item) { listItems.add(item); }
        public void addTable(Table table) { tables.add(table); }
        public void addCodeBlock(String codeBlock) { codeBlocks.add(codeBlock); }
        public void addQuote(String quote) { quotes.add(quote); }
        public void addBoldText(String boldText) { boldTexts.add(boldText); }
        public void addPlainText(String plainText) { plainTexts.add(plainText); }
    }

    @Data
    public static class ListItem {
        private String text;
        private String link;

        public ListItem(String text, String link) {
            this.text = text;
            this.link = link;
        }
    }

    @Data
    public static class Table {
        private List<String> headers = new ArrayList<>();
        private List<List<String>> rows = new ArrayList<>();

        public void addHeader(String header) { headers.add(header); }
        public void addRow(List<String> row) { rows.add(row); }
    }
}
