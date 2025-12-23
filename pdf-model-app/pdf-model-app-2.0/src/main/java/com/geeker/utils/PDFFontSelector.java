package com.geeker.utils;

/**
 * @className: PdfFontSelector
 * @author: geeker
 * @date: 12/11/25 6:36 PM
 * @Version: 1.0
 * @description:
 */

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * PDF Font 智能选择器
 */
public class PDFFontSelector {

    private Map<String, PDFont> fontCache = new HashMap<>();
    private PDDocument document;

    public PDFFontSelector(PDDocument document) {
        this.document = document;
    }

    /**
     * 根据文本内容自动选择字体
     */
    public PDFont selectFontForText(String text) throws IOException {
        // 分析文本内容
        FontRequirements requirements = analyzeTextRequirements(text);

        // 选择最佳字体
        return getBestMatchingFont(requirements);
    }

    /**
     * 分析文本需求
     */
    private FontRequirements analyzeTextRequirements(String text) {
        FontRequirements req = new FontRequirements();

        for (char c : text.toCharArray()) {
            if (isChinese(c)) {
                req.requiresChinese = true;
            } else if (Character.isLetter(c)) {
                req.containsLetters = true;
            } else if (Character.isDigit(c)) {
                req.containsNumbers = true;
            }

            // Unicode范围判断
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                req.cjkCount++;
            }
        }

        return req;
    }

    /**
     * 获取最佳匹配字体
     */
    private PDFont getBestMatchingFont(FontRequirements req) throws IOException {
        // 优先使用缓存
        String fontKey = generateFontKey(req);
        if (fontCache.containsKey(fontKey)) {
            return fontCache.get(fontKey);
        }

        // 根据需求选择字体
        PDFont selectedFont;

        if (req.requiresChinese) {
            // 加载中文字体
            selectedFont = loadChineseFont();
        } else if (req.containsLetters) {
            // 西文字体
            selectedFont = loadLatinFont();
        } else {
            // 默认字体
            selectedFont = loadDefaultFont();
        }

        // 缓存结果
        fontCache.put(fontKey, selectedFont);
        return selectedFont;
    }

    private PDFont loadLatinFont() {
        return null;
    }

    private PDFont loadDefaultFont() {
        return null;
    }

    /**
     * 根据填充内容进行选择字体，如
     *   - 是否包含中文
     *   - 是否包含数字，字符
     * @param fontRequirements
     * @return
     */
    private String generateFontKey(FontRequirements fontRequirements) {
        return null;
    }

    /**
     * 加载中文字体
     */
    private PDFont loadChineseFont() throws IOException {
        // 尝试多种中文字体路径
        String[] fontPaths = {
                "/fonts/simsun.ttf",
                "C:/Windows/Fonts/simsun.ttc",
                "/System/Library/Fonts/PingFang.ttc"
        };

        for (String path : fontPaths) {
            File fontFile = new File(path);
            if (fontFile.exists()) {
                return PDType0Font.load(document, fontFile);
            }
        }

        // 使用资源文件
        InputStream is = getClass().getResourceAsStream("/fonts/fallback.ttf");
        if (is != null) {
            return PDType0Font.load(document, is);
        }

        throw new IOException("未找到可用的中文字体");
    }

    /**
     * 辅助类
     */
    private static class FontRequirements {
        boolean requiresChinese = false;
        boolean containsLetters = false;
        boolean containsNumbers = false;
        int cjkCount = 0;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION;
    }
}
