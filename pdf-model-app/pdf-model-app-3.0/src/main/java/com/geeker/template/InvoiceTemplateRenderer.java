package com.geeker.template;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.*;
import java.io.IOException;

/**
 * @className: InvoiceTemplate
 * @author: geeker
 * @date: 12/18/25 10:21 AM
 * @Version: 1.0
 * @description:
 */
public class InvoiceTemplateRenderer {

    public void renderInvoice(PDDocument document, InvoiceData data)
            throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // 方法1：使用分层渲染
        renderInLayers(document, page, data);

        // 或方法2：使用状态管理器
        // renderWithStateManager(document, page, data);
    }

    /**
     * 分层渲染 - 最稳定的方法
     */
    private void renderInLayers(PDDocument document, PDPage page,
                                InvoiceData data) throws IOException {
        // 第一层：边框和表格线
        PDPageContentStream borderStream = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true);
        drawBorders(borderStream, data);
        borderStream.close();

        // 第二层：固定文本（标题、标签）
        PDPageContentStream labelStream = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true);
        drawLabels(labelStream, data);
        labelStream.close();

        // 第三层：动态数据
        PDPageContentStream dataStream = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true);
        drawData(dataStream, data);
        dataStream.close();
    }

    private void drawLabels(PDPageContentStream labelStream, InvoiceData data) {

    }

    private void drawData(PDPageContentStream dataStream, InvoiceData data) {
        
    }

    private void drawBorders(PDPageContentStream borderStream, InvoiceData data) {
        
    }

    /**
     * 使用状态管理器
     */
    private void renderWithStateManager(PDDocument document, PDPage page,
                                        InvoiceData data) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true);

        StateManager stateManager = new StateManager(cs);

        // 绘制边框
        stateManager.drawRectangle(50, 750, 500, 50, 1, Color.BLACK);

        // 绘制文本（自动重置状态）1
        stateManager.drawText("发票代码:", 55, 765,
                new PDType1Font(Standard14Fonts.getMappedFontName("Helvetica-Bold")), 10);
        stateManager.drawText(data.getInvoiceCode(), 120, 765,
                new PDType1Font(Standard14Fonts.getMappedFontName("Helvetica-Bold")), 10);

        // 绘制另一个边框（不会影响文本坐标）
        stateManager.drawRectangle(50, 700, 500, 30, 1, Color.BLACK);

        cs.close();
    }

    /**
     * 状态管理器类
     */
    static class StateManager {
        private PDPageContentStream cs;
        private float currentTextX = 0;
        private float currentTextY = 0;
        private boolean inTextMode = false;

        public StateManager(PDPageContentStream cs) {
            this.cs = cs;
        }

        public void drawText(String text, float x, float y,
                             PDFont font, float size) throws IOException {
            // 如果之前在其他文本模式，先结束
            if (inTextMode) {
                cs.endText();
                inTextMode = false;
            }

            // 开始新的文本块
            cs.saveGraphicsState();
            cs.beginText();
            cs.setFont(font, size);
            cs.newLineAtOffset(x, y);
            cs.showText(text);
            cs.endText();
            cs.restoreGraphicsState();

            // 记录最后文本位置
            currentTextX = x;
            currentTextY = y;
        }

        public void drawRectangle(float x, float y, float w, float h,
                                  float lineWidth, Color color)
                throws IOException {
            // 确保不在文本模式
            if (inTextMode) {
                cs.endText();
                inTextMode = false;
            }

            cs.saveGraphicsState();
            cs.setStrokingColor(color);
            cs.setLineWidth(lineWidth);
            cs.addRect(x, y, w, h);
            cs.stroke();
            cs.restoreGraphicsState();
        }
    }

    private class InvoiceData {
        public String getInvoiceCode() {
            return "";
        }
    }
}