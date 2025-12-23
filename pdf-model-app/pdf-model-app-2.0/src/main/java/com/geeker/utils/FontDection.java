package com.geeker.utils;

/**
 * @className: FontDection
 * @author: geeker
 * @date: 12/15/25 5:42 PM
 * @Version: 1.0
 * @description:
 */
import org.apache.fontbox.cff.CFFFont;
import org.apache.fontbox.cff.CFFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;

public class FontDection {

    /**
     * 智能字体加载器
     */
    public static PDFont loadFontSmart(PDDocument document, File fontFile)
            throws IOException {
        // 1. 检测字体格式
        FontFormat format = detectFontFormat(fontFile);

        // 2. 根据格式选择加载方式
        switch (format) {
            case TRUETYPE:
                return loadTrueTypeFont(document, fontFile);

            case OPENTYPE_CFF:
                return loadCFFFont(document, fontFile);

            case WOFF:
                return loadWOFFFont(document, fontFile);

            default:
                throw new IOException("不支持的字体格式: " + format);
        }
    }

    private static PDFont loadWOFFFont(PDDocument document, File fontFile) {
        return null;
    }

    /**
     * 检测字体格式
     */
    private static FontFormat detectFontFormat(File fontFile) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(fontFile, "r")) {
            int magic = raf.readInt();

            switch (magic) {
                case 0x00010000: // 'true' (Mac TTF)
                case 0x74727565: // 'true' (Windows TTF)
                    return FontFormat.TRUETYPE;

                case 0x4F54544F: // 'OTTO' - OpenType with CFF
                    return FontFormat.OPENTYPE_CFF;

                case 0x774F4646: // 'wOFF' - WOFF
                    return FontFormat.WOFF;

                case 0x774F4632: // 'wOF2' - WOFF2
                    return FontFormat.WOFF2;

                default:
                    // 检查文件扩展名
                    String name = fontFile.getName().toLowerCase();
                    if (name.endsWith(".ttf")) return FontFormat.TRUETYPE;
                    if (name.endsWith(".otf")) return FontFormat.OPENTYPE_CFF;
                    return FontFormat.UNKNOWN;
            }
        }
    }

    /**
     * 加载TrueType字体
     */
    private static PDFont loadTrueTypeFont(PDDocument document, File ttfFile)
            throws IOException {
        // 标准加载方式
        return PDType0Font.load(document, ttfFile);
    }

    /**
     * 加载CFF格式的OpenType字体
     */
    private static PDFont loadCFFFont(PDDocument document, File otfFile)
            throws IOException {
        try {
            // PDFBox 2.x 应该能正确处理CFF OTF -> 3.0 抛： loca table!
            // 如果报错，尝试下面的方法 -> java.io.IOException: loca is mandatory
            return PDType0Font.load(document, otfFile);

        } catch (IOException e) {
            if (e.getMessage().contains("loca is mandatory")) {
                // 使用FontBox的CFF解析器
                return loadCFFWithFontBox(document, otfFile);
            }
            throw e;
        }
    }

    private static PDFont loadCFFWithFontBox(PDDocument document, File otfFile)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(otfFile)) {
            return PDType0Font.load(document, fis, true);
        }
    }

    /**
     * 使用FontBox底层API加载CFF字体
     */
    /*private static PDFont loadCFFWithFontBox(PDDocument document, File otfFile)
            throws IOException {
        try (RandomAccessRead fis = new RandomAccessReadBufferedFile(otfFile)) {
            // 解析CFF字体
            CFFParser cffParser = new CFFParser();
            CFFFont cffFont = cffParser.parse(fis).get(0);

            // 创建CID字体
            //PDCIDFontType2 cidFont = new PDCIDFontType2(document, cffFont);

            // 包装为Type0字体
            return PDType0Font.load(document, fis,false,false);
        }
    }*/

    enum FontFormat {
        TRUETYPE, OPENTYPE_CFF, WOFF, WOFF2, UNKNOWN
    }


    public static void main(String[] args){
        try {
            PDDocument document = new PDDocument();
            // 测试otf -》ttf 是否可行 ->  todo 有问题：java.io.EOFException
//            URL url = FontDection.class.getClassLoader()
//                    .getResource("fonts/SourceHanSerifSC-Bold.ttf");

            URL url = FontDection.class.getClassLoader()
                    .getResource("fonts/Alibaba-PuHuiTi-Bold.ttf");
            FontDection.loadFontSmart(document, new File(url.getFile()));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
