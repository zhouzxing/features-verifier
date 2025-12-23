package com.example.restservice.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: TextChannel
 * @author: geeker
 * @date: 12/1/25 8:44 PM
 * @Version: 1.0
 * @description:
 */
public class TextProcessingPipeline {

    // 结合使用BufferedReader和CharBuffer的优点
    public void processTextWithHybridApproach(Path inputFile, Path outputFile)
            throws IOException {

        // 第一阶段：使用BufferedReader快速扫描和过滤
        List<Long> relevantPositions = scanFileForRelevantSections(inputFile);

        // 第二阶段：使用CharBuffer精确处理特定部分
        processSelectedSections(inputFile, outputFile, relevantPositions);
    }

    private List<Long> scanFileForRelevantSections(Path file) throws IOException {
        List<Long> positions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            long position = 0;

            while ((line = reader.readLine()) != null) {
                if (line.contains("IMPORTANT")) {
                    positions.add(position);
                }
                position += line.length() + 1; // +1 for newline
            }
        }

        return positions;
    }

    private void processSelectedSections(Path inputFile, Path outputFile,
                                         List<Long> positions) throws IOException {

        try (FileChannel inputChannel = FileChannel.open(inputFile);
             FileChannel outputChannel = FileChannel.open(outputFile,
                     StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            for (Long position : positions) {
                // 精确定位并处理
                MappedByteBuffer inputBuffer = inputChannel.map(
                        FileChannel.MapMode.READ_ONLY, position, 1024);

                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(inputBuffer);
                processSection(charBuffer, outputChannel);
            }
        }
    }

    private void processSection(CharBuffer buffer, FileChannel outputChannel)
            throws IOException {

        // 高效处理特定部分的文本
        ByteBuffer outputBuffer = StandardCharsets.UTF_8.encode(buffer);
        outputChannel.write(outputBuffer);
    }
}