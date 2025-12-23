package com.example.restservice.io.form;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @className: FormData
 * @author: geeker
 * @date: 12/2/25 6:31 PM
 * @Version: 1.0
 * @description:
 */
public class PerformanceComparison {

    public static void main(String[] args) throws IOException {
        // 测试不同大小的文件上传
        int[] fileSizes = {10, 100, 1024, 10240}; // KB

        for (int sizeKB : fileSizes) {
            byte[] fileData = generateTestData(sizeKB * 1024);

            System.out.printf("\n测试文件大小: %d KB%n", sizeKB);

            // 测试multipart/form-data
            long multipartTime = testMultipart(fileData);
            long multipartSize = calculateMultipartSize(fileData);

            // 测试x-www-form-urlencoded
            long urlencodedTime = testUrlEncoded(fileData);
            long urlencodedSize = calculateUrlEncodedSize(fileData);

            System.out.printf("multipart: 时间=%dms, 大小=%,d字节%n",
                    multipartTime, multipartSize);
            System.out.printf("urlencoded: 时间=%dms, 大小=%,d字节%n",
                    urlencodedTime, urlencodedSize);
            System.out.printf("urlencoded比multipart大: %.1f%%%n",
                    ((double)(urlencodedSize - multipartSize) / multipartSize * 100));
        }
    }

    private static long calculateUrlEncodedSize(byte[] fileData) {
        return 0;
    }

    private static long calculateMultipartSize(byte[] data) {
        return 0;
    }

    private static byte[] generateTestData(int size) {
        byte[] data = new byte[size];
        return data;
    }

    private static long testMultipart(byte[] fileData) {
        long start = System.currentTimeMillis();

        String boundary = "----Boundary123456";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            // 构建multipart请求体
            writeField(output, boundary, "username", "张三");
            writeField(output, boundary, "email", "test@example.com");
            writeFile(output, boundary, "file", "test.jpg", fileData);

            // 结束边界
            output.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis() - start;
    }

    private static long testUrlEncoded(byte[] fileData) {
        long start = System.currentTimeMillis();

        StringBuilder body = new StringBuilder();

        try {
            // URL编码文本字段
            body.append("username=")
                    .append(URLEncoder.encode("张三", "UTF-8"))
                    .append("&email=")
                    .append(URLEncoder.encode("test@example.com", "UTF-8"))
                    .append("&file=");

            // Base64编码文件数据
            String base64 = Base64.getEncoder().encodeToString(fileData);
            // Base64数据中的=需要URL编码
            base64 = URLEncoder.encode(base64, "UTF-8");
            body.append(base64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis() - start;
    }

    private static void writeField(ByteArrayOutputStream output,
                                   String boundary, String name, String value)
            throws IOException {
        output.write(("--" + boundary + "\r\n").getBytes());
        output.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n").getBytes());
        output.write("\r\n".getBytes());
        output.write((value + "\r\n").getBytes());
    }

    private static void writeFile(ByteArrayOutputStream output,
                                  String boundary, String name,
                                  String filename, byte[] data)
            throws IOException {
        output.write(("--" + boundary + "\r\n").getBytes());
        output.write(("Content-Disposition: form-data; name=\"" + name +
                "\"; filename=\"" + filename + "\"\r\n").getBytes());
        output.write("Content-Type: application/octet-stream\r\n".getBytes());
        output.write("\r\n".getBytes());
        output.write(data);
        output.write("\r\n".getBytes());
    }
}