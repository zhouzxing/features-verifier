package com.example.restservice.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: LargeFileParser
 * @author: geeker
 * @date: 12/1/25 8:14 PM
 * @Version: 1.0
 * @description:
 */

public class HighPerformanceTextParser {

    // ✅ 场景：CSV/JSON解析器、模板引擎
    public void parseCSVFile(Path csvFile) throws IOException {
        try (FileChannel channel = FileChannel.open(csvFile)) {
            // 使用内存映射提高性能
            MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_ONLY, 0, channel.size());

            // 转换为CharBuffer
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(mappedBuffer);

            // 高性能解析
            parseCSVBuffer(charBuffer);
        }
    }

    private void parseCSVBuffer(CharBuffer buffer) {
        List<List<String>> records = new ArrayList<>();
        List<String> currentRecord = new ArrayList<>();
        StringBuilder field = new StringBuilder();

        boolean inQuotes = false;

        while (buffer.hasRemaining()) {
            char c = buffer.get();

            switch (c) {
                case '"':
                    inQuotes = !inQuotes;
                    break;
                case ',':
                    if (!inQuotes) {
                        currentRecord.add(field.toString());
                        field.setLength(0);
                    } else {
                        field.append(c);
                    }
                    break;
                case '\n':
                    if (!inQuotes) {
                        currentRecord.add(field.toString());
                        records.add(new ArrayList<>(currentRecord));
                        currentRecord.clear();
                        field.setLength(0);
                    } else {
                        field.append(c);
                    }
                    break;
                default:
                    field.append(c);
            }
        }

        // 处理最后一条记录
        if (field.length() > 0 || !currentRecord.isEmpty()) {
            currentRecord.add(field.toString());
            records.add(currentRecord);
        }
    }

    // ❌ 用BufferedReader解析CSV（性能较差）
    public void parseCSVWithBufferedReader(Path csvFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(csvFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 每行都要创建String对象 -> 内存占用 高
                // 无法处理跨行的引用字段
                String[] fields = line.split(",");
                // 需要额外处理引用和转义
            }
        }
    }

    public static void main(String[] args) {
        try {
            Path csvFile = Path.of("pom.xml");
            System.out.println("Parsing CSV file: " + csvFile.toAbsolutePath());
            HighPerformanceTextParser parser = new HighPerformanceTextParser();
            parser.parseCSVFile(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
