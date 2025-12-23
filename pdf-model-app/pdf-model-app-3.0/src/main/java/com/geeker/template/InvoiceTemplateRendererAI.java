package com.geeker.template;

/**
 * @className: InVoice
 * @author: geeker
 * @date: 12/18/25 2:36 PM
 * @Version: 1.0
 * @description:
 */
import com.azul.crs.client.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 完整的发票模板渲染器
 */
public class InvoiceTemplateRendererAI {

    // 页面尺寸
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();

    // 字体
    private PDFont titleFont;
    private PDFont labelFont;
    private PDFont dataFont;
    private PDFont watermarkFont;

    // 颜色定义
    private static final Color BORDER_COLOR = new Color(0, 0, 0); // 黑色
    private static final Color TITLE_COLOR = new Color(0, 0, 0);  // 黑色
    private static final Color LABEL_COLOR = new Color(0, 0, 0);  // 黑色
    private static final Color DATA_COLOR = new Color(0, 0, 0);   // 黑色

    /**
     * 颜色0-255 -> 0-1
     * @param rgbValue
     * @return
     */
    private float toPDFColor(int rgbValue) {
        return rgbValue / 255.0f;
    }


    /**
     * 渲染发票主方法
     */
    public void renderInvoice(String fileName, InvoiceData data) throws Exception {
        PDDocument document = new PDDocument();

        try {
            // 创建页面
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // 加载字体
            loadFonts(document);

            // 使用分层渲染（最稳定）
            renderInLayers(document, page, data);

            // 保存文档
            Path path = Files.createFile(Paths.get(getClass().getResource("/templates/").getFile()+fileName));
            document.save(path.toFile().getAbsolutePath());
            System.out.println("发票生成成功: " + path.toFile().getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            document.close();
        }
    }

    /**
     * 加载字体
     */
    private void loadFonts(PDDocument document) throws IOException {
        // 尝试加载中文字体
        try {
            InputStream fontStream = getClass().getClassLoader()
                    .getResourceAsStream("fonts/simfang.ttf");
            if (fontStream != null) {
                titleFont = PDType0Font.load(document, fontStream);
                labelFont = PDType0Font.load(document,
                        getClass().getClassLoader()
                                .getResourceAsStream("fonts/simfang.ttf"));
                dataFont = PDType0Font.load(document,
                        getClass().getClassLoader()
                                .getResourceAsStream("fonts/simfang.ttf"));
            }
        } catch (Exception e) {
            System.err.println("加载中文字体失败，使用默认字体: " + e.getMessage());
        }

        // 如果中文字体加载失败，使用PDFBox默认字体
        if (titleFont == null) {
            titleFont =  PDType0Font.load(document,getClass().getResourceAsStream("/fonts/simfang.ttf"));// PDType1Font.HELVETICA_BOLD;
            labelFont = PDType0Font.load(document,getClass().getResourceAsStream("/fonts/simfang.ttf"));
            dataFont = PDType0Font.load(document,getClass().getResourceAsStream("/fonts/simfang.ttf"));
        }

        // 水印字体
        watermarkFont = PDType0Font.load(document,getClass().getResourceAsStream("/fonts/simfang.ttf"));
    }

    /**
     * 分层渲染 - 最稳定的方法
     */
    private void renderInLayers(PDDocument document, PDPage page,
                                InvoiceData data) throws IOException {
        // 第一层：水印和背景
        renderWatermarkAndBackground(document, page);

        // 第二层：边框和表格线
        renderBordersAndLines(document, page, data);

        // 第三层：固定文本（标题、标签）
        renderStaticText(document, page, data);

        // 第四层：动态数据
        // renderDynamicData(document, page, data);

        // 第五层：页脚和印章
        renderFooterAndSeal(document, page, data);
    }

    /**
     * 第一层：水印和背景
     */
    private void renderWatermarkAndBackground(PDDocument document, PDPage page)
            throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        try {
            // 添加浅色背景
            cs.setNonStrokingColor(toPDFColor(245), toPDFColor(245), toPDFColor(245)); // 浅灰色
            cs.addRect(0, 0, PAGE_WIDTH, PAGE_HEIGHT);
            cs.fill();

            // 添加水印
            addWatermark(cs, page);

        } finally {
            cs.close();
        }
    }

    /**
     * 第二层：边框和表格线
     */
    private void renderBordersAndLines(PDDocument document, PDPage page,
                                       InvoiceData data) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        try {
            cs.saveGraphicsState();

            // 外边框
            drawRectangle(cs, 30, 30, PAGE_WIDTH - 60, PAGE_HEIGHT - 60, 1.5f, BORDER_COLOR);

            // 标题框
            drawRectangle(cs, 40, PAGE_HEIGHT - 80, PAGE_WIDTH - 80, 50, 1, BORDER_COLOR);

            // 购买方信息框
            drawRectangle(cs, 40, PAGE_HEIGHT - 180, PAGE_WIDTH - 80, 90, 1, BORDER_COLOR);

            // 商品表格
            float tableY = PAGE_HEIGHT - 300;
            float tableHeight = 180;

            // 表头
            drawRectangle(cs, 40, tableY, PAGE_WIDTH - 80, 30, 1, BORDER_COLOR);

            // 表格内部横线
            float[] rowHeights = {30, 30, 30, 30, 30, 30};
            float currentY = tableY;
            for (int i = 0; i <= rowHeights.length; i++) {
                if (i > 0) {
                    currentY -= rowHeights[i-1];
                    drawHorizontalLine(cs, 40, currentY, PAGE_WIDTH - 80, 1, BORDER_COLOR);
                }
            }

            // 表格竖线
            float[] columnWidths = {60, 250, 60, 60, 60};
            float currentX = 40;
            for (int i = 0; i <= columnWidths.length; i++) {
                if (i > 0) {
                    currentX += columnWidths[i-1];
                    drawVerticalLine(cs, currentX, tableY - tableHeight, tableHeight, 1, BORDER_COLOR);
                }
            }

            // 合计框
            drawRectangle(cs, 40, tableY - tableHeight - 60, PAGE_WIDTH - 80, 60, 1, BORDER_COLOR);

            // 备注框
            drawRectangle(cs, 40, tableY - tableHeight - 160, PAGE_WIDTH - 80, 90, 1, BORDER_COLOR);

            cs.restoreGraphicsState();

        } finally {
            cs.close();
        }
    }

    /**
     * 第三层：固定文本（标题、标签）
     */
    private void renderStaticText(PDDocument document, PDPage page,
                                  InvoiceData data) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        try {
            cs.saveGraphicsState();

            // 发票标题
            drawText(cs, "增值税专用发票", PAGE_WIDTH / 2, PAGE_HEIGHT - 55,
                    titleFont, 20, TITLE_COLOR, true);

            // 发票代码标签
            drawText(cs, "发票代码:", 45, PAGE_HEIGHT - 100,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "发票号码:", 180, PAGE_HEIGHT - 100,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "开票日期:", 300, PAGE_HEIGHT - 100,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "校验码:", 420, PAGE_HEIGHT - 100,
                    labelFont, 10, LABEL_COLOR, false);

            // 购买方标签
            drawText(cs, "购买方", 45, PAGE_HEIGHT - 140,
                    labelFont, 12, LABEL_COLOR, false);

            drawText(cs, "名    称:", 50, PAGE_HEIGHT - 160,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "纳税人识别号:", 50, PAGE_HEIGHT - 180,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "地址、电话:", 50, PAGE_HEIGHT - 200,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "开户行及账号:", 50, PAGE_HEIGHT - 220,
                    labelFont, 10, LABEL_COLOR, false);

            // 表格标题
            String[] headers = {"序号", "货物或应税劳务名称", "规格型号", "单位", "数量", "单价", "金额", "税率", "税额"};
            float[] headerX = {50, 110, 360, 420, 480, 540, 600, 660, 720};
            float headerY = PAGE_HEIGHT - 285;

            for (int i = 0; i < headers.length; i++) {
                drawText(cs, headers[i], headerX[i], headerY,
                        labelFont, 8, LABEL_COLOR, false);
            }

            // 合计标签
            drawText(cs, "价税合计（大写）", 50, PAGE_HEIGHT - 470,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "（小写）", 50, PAGE_HEIGHT - 490,
                    labelFont, 10, LABEL_COLOR, false);

            // 销售方标签
            drawText(cs, "销售方", 400, PAGE_HEIGHT - 140,
                    labelFont, 12, LABEL_COLOR, false);

            drawText(cs, "名    称:", 405, PAGE_HEIGHT - 160,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "纳税人识别号:", 405, PAGE_HEIGHT - 180,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "地址、电话:", 405, PAGE_HEIGHT - 200,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "开户行及账号:", 405, PAGE_HEIGHT - 220,
                    labelFont, 10, LABEL_COLOR, false);

            // 备注标签
            drawText(cs, "备注:", 50, PAGE_HEIGHT - 520,
                    labelFont, 10, LABEL_COLOR, false);

            // 开票人标签
            drawText(cs, "开票人:", 400, PAGE_HEIGHT - 560,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "收款人:", 400, PAGE_HEIGHT - 580,
                    labelFont, 10, LABEL_COLOR, false);

            drawText(cs, "复核人:", 400, PAGE_HEIGHT - 600,
                    labelFont, 10, LABEL_COLOR, false);

            cs.restoreGraphicsState();

        } finally {
            cs.close();
        }
    }

    /**
     * 第四层：动态数据
     */
    private void renderDynamicData(PDDocument document, PDPage page,
                                   InvoiceData data) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        try {
            cs.saveGraphicsState();

            // 发票代码和号码
            drawText(cs, data.getInvoiceCode(), 100, PAGE_HEIGHT - 100,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getInvoiceNumber(), 240, PAGE_HEIGHT - 100,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getInvoiceDate(), 360, PAGE_HEIGHT - 100,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getCheckCode(), 470, PAGE_HEIGHT - 100,
                    dataFont, 10, DATA_COLOR, false);

            // 购买方信息
            drawText(cs, data.getBuyerName(), 120, PAGE_HEIGHT - 160,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getBuyerTaxId(), 120, PAGE_HEIGHT - 180,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getBuyerAddress(), 120, PAGE_HEIGHT - 200,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getBuyerBankAccount(), 120, PAGE_HEIGHT - 220,
                    dataFont, 10, DATA_COLOR, false);

            // 商品数据
            List<ProductItem> products = data.getProducts();
            float startY = PAGE_HEIGHT - 315;
            float rowHeight = 30;

            for (int i = 0; i < Math.min(products.size(), 6); i++) {
                ProductItem item = products.get(i);
                float y = startY - i * rowHeight;

                drawText(cs, String.valueOf(i + 1), 55, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, item.getProductName(), 115, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, item.getSpecification(), 365, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, item.getUnit(), 425, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, formatNumber(item.getQuantity()), 485, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, formatCurrency(item.getUnitPrice()), 545, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, formatCurrency(item.getAmount()), 605, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, item.getTaxRate() + "%", 665, y, dataFont, 9, DATA_COLOR, false);
                drawText(cs, formatCurrency(item.getTaxAmount()), 725, y, dataFont, 9, DATA_COLOR, false);
            }

            // 合计金额
            drawText(cs, data.getTotalAmountInWords(), 150, PAGE_HEIGHT - 470,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, "RMB:" + formatCurrency(data.getTotalAmount()), 150, PAGE_HEIGHT - 490,
                    dataFont, 10, DATA_COLOR, false);

            // 销售方信息
            drawText(cs, data.getSellerName(), 475, PAGE_HEIGHT - 160,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getSellerTaxId(), 475, PAGE_HEIGHT - 180,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getSellerAddress(), 475, PAGE_HEIGHT - 200,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getSellerBankAccount(), 475, PAGE_HEIGHT - 220,
                    dataFont, 10, DATA_COLOR, false);

            // 备注
            drawText(cs, data.getRemark(), 85, PAGE_HEIGHT - 520,
                    dataFont, 10, DATA_COLOR, false);

            // 开票人等
            drawText(cs, data.getDrawer(), 450, PAGE_HEIGHT - 560,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getPayee(), 450, PAGE_HEIGHT - 580,
                    dataFont, 10, DATA_COLOR, false);

            drawText(cs, data.getChecker(), 450, PAGE_HEIGHT - 600,
                    dataFont, 10, DATA_COLOR, false);

            cs.restoreGraphicsState();

        } finally {
            cs.close();
        }
    }

    /**
     * 第五层：页脚和印章
     */
    private void renderFooterAndSeal(PDDocument document, PDPage page,
                                     InvoiceData data) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        try {
            cs.saveGraphicsState();

            // 页脚信息
            drawText(cs, "注：本发票为电子发票，与纸质发票具有同等法律效力",
                    PAGE_WIDTH / 2, 40, dataFont, 8, new Color(100, 100, 100), true);

            // 模拟印章（使用文本和圆形）
            if (data.getSealText() != null && !data.getSealText().isEmpty()) {
                drawSeal(cs, 500, 200, data.getSealText());
            }

            cs.restoreGraphicsState();

        } finally {
            cs.close();
        }
    }

    /**
     * 基础绘制方法
     */

    private void drawText(PDPageContentStream cs, String text, float x, float y,
                          PDFont font, float fontSize, Color color, boolean center)
            throws IOException {
        cs.saveGraphicsState();

        // 设置颜色
        cs.setNonStrokingColor(color);

        // 开始文本
        cs.beginText();
        cs.setFont(font, fontSize);

        // 计算文本宽度用于居中
        if (center) {
            float textWidth = font.getStringWidth(text) * fontSize / 1000f;
            x = x - textWidth / 2;
        }

        // 设置位置
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();

        cs.restoreGraphicsState();
    }

    private void drawRectangle(PDPageContentStream cs, float x, float y,
                               float width, float height, float lineWidth,
                               Color color) throws IOException {
        cs.saveGraphicsState();

        cs.setStrokingColor(color);
        cs.setLineWidth(lineWidth);
        cs.addRect(x, y, width, height);
        cs.stroke();

        cs.restoreGraphicsState();
    }

    private void drawHorizontalLine(PDPageContentStream cs, float x, float y,
                                    float length, float lineWidth, Color color)
            throws IOException {
        cs.saveGraphicsState();

        cs.setStrokingColor(color);
        cs.setLineWidth(lineWidth);
        cs.moveTo(x, y);
        cs.lineTo(x + length, y);
        cs.stroke();

        cs.restoreGraphicsState();
    }

    private void drawVerticalLine(PDPageContentStream cs, float x, float y,
                                  float length, float lineWidth, Color color)
            throws IOException {
        cs.saveGraphicsState();

        cs.setStrokingColor(color);
        cs.setLineWidth(lineWidth);
        cs.moveTo(x, y);
        cs.lineTo(x, y + length);
        cs.stroke();

        cs.restoreGraphicsState();
    }

    private void addWatermark(PDPageContentStream cs, PDPage page)
            throws IOException {
        cs.saveGraphicsState();

        PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
        graphicsState.setNonStrokingAlphaConstant(0.1f); // 透明度
        graphicsState.setAlphaSourceFlag(true);
        cs.setGraphicsStateParameters(graphicsState);

        cs.setNonStrokingColor(toPDFColor(200),toPDFColor(200), toPDFColor(200)); // 浅灰色

        cs.beginText();
        cs.setFont(watermarkFont, 60);

        // 旋转45度
        cs.setTextMatrix(Matrix.getRotateInstance(Math.PI / 4,
                PAGE_WIDTH / 2 - 100, PAGE_HEIGHT / 2));

        cs.showText("样    本");
        cs.endText();

        cs.restoreGraphicsState();
    }

    private void drawSeal(PDPageContentStream cs, float centerX, float centerY,
                          String text) throws IOException {
        float radius = 50;

        // 绘制外圆
        cs.saveGraphicsState();
        cs.setStrokingColor(1, 0, 0); // 红色
        cs.setLineWidth(2);

        // 绘制圆形
        float angle = 0;
        float angleStep = (float) (Math.PI / 180); // 1度

        cs.moveTo(centerX + radius, centerY);
        for (int i = 1; i <= 360; i++) {
            angle += angleStep;
            float x = centerX + radius * (float) Math.cos(angle);
            float y = centerY + radius * (float) Math.sin(angle);
            cs.lineTo(x, y);
        }
        cs.stroke();

        // 绘制五角星（简化版）
        float starRadius = 15;
        cs.moveTo(centerX, centerY + starRadius);
        for (int i = 1; i <= 5; i++) {
            float angleOuter = (float) (Math.PI / 2 + i * 2 * Math.PI / 5);
            float angleInner = (float) (Math.PI / 2 + i * 2 * Math.PI / 5 + Math.PI / 5);

            float xOuter = centerX + starRadius * (float) Math.cos(angleOuter);
            float yOuter = centerY + starRadius * (float) Math.sin(angleOuter);

            float xInner = centerX + starRadius * 0.5f * (float) Math.cos(angleInner);
            float yInner = centerY + starRadius * 0.5f * (float) Math.sin(angleInner);

            cs.lineTo(xOuter, yOuter);
            cs.lineTo(xInner, yInner);
        }
        cs.closeAndStroke();

        // 绘制印章文字（环绕）
        cs.beginText();
        cs.setFont(titleFont, 10);
        cs.setNonStrokingColor(1, 0, 0);

        // 文字环绕逻辑（简化版）
        float textRadius = radius + 10;
        float startAngle = (float) (Math.PI); // 从左边开始

        // 实际中需要更复杂的文字环绕算法
        cs.newLineAtOffset(centerX - 30, centerY - 5);
        cs.showText("发票专用章");
        cs.endText();

        cs.restoreGraphicsState();
    }

    /**
     * 工具方法
     */
    private String formatCurrency(double amount) {
        return String.format("%.2f", amount);
    }

    private String formatNumber(double number) {
        return String.format("%.2f", number);
    }

    /**
     * 数据模型类
     */
    public static class InvoiceData {
        private String invoiceCode;
        private String invoiceNumber;
        private String invoiceDate;
        private String checkCode;
        private String buyerName;
        private String buyerTaxId;
        private String buyerAddress;
        private String buyerBankAccount;
        private String sellerName;
        private String sellerTaxId;
        private String sellerAddress;
        private String sellerBankAccount;
        private String remark;
        private String drawer;
        private String payee;
        private String checker;
        private String sealText;
        private double totalAmount;
        private String totalAmountInWords;
        private List<ProductItem> products = new ArrayList<>();

        // Getters and Setters
        public String getInvoiceCode() { return invoiceCode; }
        public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }

        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

        public String getInvoiceDate() { return invoiceDate; }
        public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }

        public String getCheckCode() { return checkCode; }
        public void setCheckCode(String checkCode) { this.checkCode = checkCode; }

        public String getBuyerName() { return buyerName; }
        public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

        public String getBuyerTaxId() { return buyerTaxId; }
        public void setBuyerTaxId(String buyerTaxId) { this.buyerTaxId = buyerTaxId; }

        public String getBuyerAddress() { return buyerAddress; }
        public void setBuyerAddress(String buyerAddress) { this.buyerAddress = buyerAddress; }

        public String getBuyerBankAccount() { return buyerBankAccount; }
        public void setBuyerBankAccount(String buyerBankAccount) { this.buyerBankAccount = buyerBankAccount; }

        public String getSellerName() { return sellerName; }
        public void setSellerName(String sellerName) { this.sellerName = sellerName; }

        public String getSellerTaxId() { return sellerTaxId; }
        public void setSellerTaxId(String sellerTaxId) { this.sellerTaxId = sellerTaxId; }

        public String getSellerAddress() { return sellerAddress; }
        public void setSellerAddress(String sellerAddress) { this.sellerAddress = sellerAddress; }

        public String getSellerBankAccount() { return sellerBankAccount; }
        public void setSellerBankAccount(String sellerBankAccount) { this.sellerBankAccount = sellerBankAccount; }

        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }

        public String getDrawer() { return drawer; }
        public void setDrawer(String drawer) { this.drawer = drawer; }

        public String getPayee() { return payee; }
        public void setPayee(String payee) { this.payee = payee; }

        public String getChecker() { return checker; }
        public void setChecker(String checker) { this.checker = checker; }

        public String getSealText() { return sealText; }
        public void setSealText(String sealText) { this.sealText = sealText; }

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

        public String getTotalAmountInWords() { return totalAmountInWords; }
        public void setTotalAmountInWords(String totalAmountInWords) { this.totalAmountInWords = totalAmountInWords; }

        public List<ProductItem> getProducts() { return products; }
        public void setProducts(List<ProductItem> products) { this.products = products; }
        public void addProduct(ProductItem product) { this.products.add(product); }
    }

    public static class ProductItem {
        private String productName;
        private String specification;
        private String unit;
        private double quantity;
        private double unitPrice;
        private double amount;
        private double taxRate;
        private double taxAmount;

        public ProductItem(String productName, String specification, String unit,
                           double quantity, double unitPrice, double taxRate) {
            this.productName = productName;
            this.specification = specification;
            this.unit = unit;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.taxRate = taxRate;
            this.amount = quantity * unitPrice;
            this.taxAmount = this.amount * taxRate / 100;
        }

        // Getters
        public String getProductName() { return productName; }
        public String getSpecification() { return specification; }
        public String getUnit() { return unit; }
        public double getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
        public double getAmount() { return amount; }
        public double getTaxRate() { return taxRate; }
        public double getTaxAmount() { return taxAmount; }
    }

    /**
     * 主方法 - 测试运行
     */
    public static void main(String[] args) {
        try {
            // 创建渲染器
            InvoiceTemplateRendererAI renderer = new InvoiceTemplateRendererAI();

            // 准备测试数据
            InvoiceData data = createTestData();

            // 渲染发票
            renderer.renderInvoice("Invoice_template.pdf", data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建测试数据
     */
    private static InvoiceData createTestData() {
        InvoiceData data = new InvoiceData();

        // 发票头信息
        data.setInvoiceCode("011001800111");
        data.setInvoiceNumber("12345678");
        data.setInvoiceDate("2024年01月15日");
        data.setCheckCode("1234 5678 9012 3456");

        // 购买方信息
        data.setBuyerName("示例科技有限公司");
        data.setBuyerTaxId("91110108MA01A9T123");
        data.setBuyerAddress("北京市海淀区中关村大街1号 010-88888888");
        data.setBuyerBankAccount("中国银行北京分行 1234 5678 9012 3456 789");

        // 销售方信息
        data.setSellerName("供应商有限公司");
        data.setSellerTaxId("91110105MA01B8U456");
        data.setSellerAddress("上海市浦东新区张江路100号 021-99999999");
        data.setSellerBankAccount("工商银行上海分行 9876 5432 1098 7654 321");

        // 商品信息
        data.addProduct(new ProductItem("笔记本电脑", "ThinkPad X1 Carbon", "台", 2, 8999.00, 13));
        data.addProduct(new ProductItem("显示器", "27寸4K IPS", "台", 5, 1999.00, 13));
        data.addProduct(new ProductItem("键盘", "机械键盘青轴", "个", 10, 299.00, 13));
        data.addProduct(new ProductItem("鼠标", "无线游戏鼠标", "个", 15, 199.00, 13));

        // 计算合计
        double totalAmount = data.getProducts().stream()
                .mapToDouble(ProductItem::getAmount)
                .sum();
        double totalTax = data.getProducts().stream()
                .mapToDouble(ProductItem::getTaxAmount)
                .sum();

        data.setTotalAmount(totalAmount + totalTax);
        data.setTotalAmountInWords("贰万柒仟叁佰肆拾陆元玖角整");

        // 其他信息
        data.setRemark("请于30日内付款，逾期按日收取0.05%滞纳金");
        data.setDrawer("张三");
        data.setPayee("李四");
        data.setChecker("王五");
        data.setSealText("供应商有限公司发票专用章");

        return data;
    }
}
