package com.geeker.utils;

/**
 * @className: FontSelector
 * @author: geeker
 * @date: 12/11/25 4:42 PM
 * @Version: 1.0
 * @description:
 */

import java.awt.*;
import java.util.*;
import java.util.List;

public class SystemFontScanner {

    /**
     * 获取系统所有可用字体
     */
    public static Map<String, String> scanSystemFonts() {
        Map<String, String> fonts = new LinkedHashMap<>();

        // 获取系统字体
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();

        for (Font font : allFonts) {
            String name = font.getFontName();
            String family = font.getFamily();

            // 过滤并分类
            if (name.contains("宋") || name.contains("楷") ||
                    name.contains("黑") || name.contains("仿宋")) {
                fonts.put(name, "中文字体");
            } else if (isCJKFont(font)) {
                fonts.put(name, "CJK字体");
            } else {
                fonts.put(name, "西文字体");
            }
        }

        return fonts;
    }

    /**
     * 检查是否为CJK字体
     */
    private static boolean isCJKFont(Font font) {
        String name = font.getFontName().toLowerCase();
        return font.canDisplay('汉') ||
                name.contains("cjk") ||
                name.contains("chinese") ||
                name.contains("japanese") ||
                name.contains("korean");
    }

    /**
     * 查找系统支持中文的字体
     */
    public static List<String> findChineseFonts() {
        List<String> chineseFonts = new ArrayList<>();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();

        for (Font font : allFonts) {
            if (font.canDisplay('中') && font.canDisplay('文')) {
                chineseFonts.add(font.getFontName());
                font.deriveFont(1);
            }
        }

        return chineseFonts;
    }
}
