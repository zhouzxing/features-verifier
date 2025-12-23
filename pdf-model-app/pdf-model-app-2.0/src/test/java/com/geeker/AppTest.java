package com.geeker;

import com.geeker.utils.FontManager;
import com.geeker.utils.SystemFontScanner;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Rigorous Test :-)
     */
    public void testShouldAnswerWithTrue()
    {
        Assert.assertTrue( true );
    }

    public void testDrawPdfGrid() throws IOException {
        // Loading an existing document
        InputStream in = PDFBoxDemo.class.getClassLoader().getResourceAsStream("pdf/VAT_INVOICE.pdf");
        PDDocument document = PDDocument.load(in);

        // Retrieving the pages of the document, and using PREPEND mode
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.PREPEND, false);

        PDFBoxDemo.drawGrid(contentStream,page);
        contentStream.close();
        String parent = PDFBoxDemo.class.getClassLoader().getResource("pdf").getPath();
        File file = new File(parent, "DrawGrid_VAT_INVOICE.pdf");
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
        document.save(file);
        document.close();
    }

    public void testDrawPdfGrid_Page() throws IOException {
        // Loading an existing document
        InputStream in = PDFBoxDemo.class.getClassLoader().getResourceAsStream("pdf/VAT_INVOICE.pdf");
        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        // page.setResources(new PDResources("pdf/VAT_INVOICE.pdf"));

        // 无效
        page.setContents(new PDStream(document,PDFBoxDemo.class.getClassLoader().getResourceAsStream("pdf/VAT_INVOICE.pdf")));


        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.PREPEND, false);


        PDFBoxDemo.drawGrid(contentStream,page);
        contentStream.close();
        String parent = PDFBoxDemo.class.getClassLoader().getResource("pdf").getPath();
        File file = new File(parent, "DrawGrid_PAGE_VAT_INVOICE.pdf");
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
        document.save(file);
        document.close();
    }



    public void testPath(){
        File file = new File("c://tmp");
        file.mkdirs();
        System.out.println(file.getAbsoluteFile());
    }


    public void testFontSelector() throws IOException {
        List<String> chineseFonts = SystemFontScanner.findChineseFonts();
        System.out.println(chineseFonts);
    }


    public void testFontManager(){
        FontManager fontManager = new FontManager(new PDDocument());
        String s = fontManager.selectFontInteractive("你好测试！");
        System.out.println(s);
    }

    /**
     * 类路径问题：
     *  - 默认 project root
     *  - resources <- spring
     */
    public void test_java_path(){
        File file = new File("config/fonts/simfang.ttf");
        // 项目根目录加载不到
        // URL url = AppTest.class.getResource("config/fonts/simfang.ttf");

        System.out.println(file.getAbsolutePath());
//        System.out.println(url.getPath());
//        System.out.println(url.getFile());

        file = new File("src/config/fonts/simfang.ttf");
        System.out.println(file.getAbsolutePath());

//        url = AppTest.class.getResource("config/fonts/simfang.ttf");
//
//        System.out.println(url.getPath());
//        System.out.println(url.getFile());

        System.out.println(System.getProperty("user.dir"));
    }

    public void testResource(){
        // target/classes + test-classes
        System.out.println(System.getProperty("java.class.path"));

        /**
         * 对比获取路径
         * File： 项目根路径： 1
         * ClassLoader: 类路径 -》
         * Class： 当前类位置
         * maven - 多module
         */
        File file = new File("config/fonts/simfang.ttf");
        System.out.println(file.getAbsolutePath());
        file = new File("src/config/fonts/simfang.ttf");
        System.out.println(file.getAbsolutePath());


        URL url = getClass().getClassLoader().getResource("config/fonts/simfang.ttf");
        System.out.println(url.getFile() + "|||" + url.getPath());

        url = getClass().getClassLoader().getResource("/config/fonts/simfang.ttf");
        System.out.println(url.getFile() + "|||" + url.getPath());

        getClass().getResource("/config/fonts/simfang.ttf");

        this.getClass().getResource("/config/fonts/simfang.ttf");
    }

}
