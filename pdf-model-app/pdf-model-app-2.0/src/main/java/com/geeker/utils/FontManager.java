package com.geeker.utils;

/**
 * @className: FontManager
 * @author: geeker
 * @date: 12/11/25 6:48 PM
 * @Version: 1.0
 * @description:
 */
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.*;
import java.io.*;
import java.util.*;

public class FontManager {

    private PDDocument document;
    private Map<String, PDFont> loadedFonts = new HashMap<>();
    private FontRegistry fontRegistry = new FontRegistry();

    public FontManager(PDDocument document) {
        this.document = document;
        loadDefaultFonts();
    }

    /**
     * 加载默认字体集
     */
    private void loadDefaultFonts() {
        try {
            // 标准PDF字体
            loadedFonts.put("Helvetica", PDType1Font.HELVETICA);
            loadedFonts.put("Helvetica-Bold", PDType1Font.HELVETICA_BOLD);
            loadedFonts.put("Times-Roman", PDType1Font.TIMES_ROMAN);
            loadedFonts.put("Courier", PDType1Font.COURIER);

            // 尝试加载中文字体
            loadChineseFonts();

        } catch (Exception e) {
            System.err.println("加载默认字体失败: " + e.getMessage());
        }
    }

    /**
     * 加载中文字体
     */
    private void loadChineseFonts() throws IOException {
        List<FontSource> chineseFonts = Arrays.asList(
                new FontSource("宋体", "simsun.ttf", FontCategory.CHINESE),
                new FontSource("仿宋体", "simfang.ttf", FontCategory.CHINESE),
                new FontSource("黑体", "simhei.ttf", FontCategory.CHINESE),
                new FontSource("微软雅黑", "msyh.ttf", FontCategory.CHINESE),
                new FontSource("思源黑体", "SourceHanSansCN.ttf", FontCategory.CHINESE)
        );

        for (FontSource source : chineseFonts) {
            try {
                PDFont font = loadFontFromResource(source.path);
                if (font != null) {
                    loadedFonts.put(source.name, font);
                    fontRegistry.register(source);
                }
            } catch (Exception e) {
                // 忽略加载失败的字体
            }
        }
    }

    /**
     * 从资源文件加载字体
     */
    private PDFont loadFontFromResource(String resourcePath) throws IOException {
        InputStream is = getClass().getResourceAsStream("/fonts/" + resourcePath);
        if (is != null) {
            return PDType0Font.load(document, is);
        }
        return null;
    }

    /**
     * 获取支持指定文本的字体列表
     */
    public List<String> getCompatibleFonts(String text) {
        List<String> compatible = new ArrayList<>();

        for (Map.Entry<String, PDFont> entry : loadedFonts.entrySet()) {
            if (isFontCompatible(entry.getValue(), text)) {
                compatible.add(entry.getKey());
            }
        }

        return compatible;
    }

    /**
     * 检查字体兼容性
     */
    private boolean isFontCompatible(PDFont font, String text) {
        try {
            for (char c : text.toCharArray()) {
                String encoding = Arrays.toString(font.encode(String.valueOf(c)));
                if (encoding.equals(".notdef")) {
                    return false;
                }
            }
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 字体选择对话框（控制台版）
     */
    public String selectFontInteractive(String text) {
        List<String> compatibleFonts = getCompatibleFonts(text);

        if (compatibleFonts.isEmpty()) {
            return suggestFallbackFont(text);
        }

        System.out.println("=== 可选字体 ===");
        for (int i = 0; i < compatibleFonts.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, compatibleFonts.get(i));
        }

        // 这里可以扩展为实际的选择逻辑
        return compatibleFonts.get(0); // 返回第一个
    }

    /**
     * 建议回退字体信息
     * @param text
     * @return
     */
    private String suggestFallbackFont(String text) {
        return "资源列表-字体不全面，没有找到兼容的字体";
    }

    /**
     * 字体注册表类
     */
    private static class FontRegistry {
        private Map<String, FontSource> sources = new HashMap<>();

        void register(FontSource source) {
            sources.put(source.name, source);
        }

        FontSource get(String name) {
            return sources.get(name);
        }
    }

    private static class FontSource {
        String name;
        String path;
        FontCategory category;

        FontSource(String name, String path, FontCategory category) {
            this.name = name;
            this.path = path;
            this.category = category;
        }
    }

    private enum FontCategory {
        LATIN, CHINESE, JAPANESE, KOREAN, SYMBOL
    }
}
