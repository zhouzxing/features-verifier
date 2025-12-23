package com.example.restservice.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: RandomAccess
 * @author: geeker
 * @date: 12/1/25 8:23 PM
 * @Version: 1.0
 * @description:
 */

public class RandomTextAccess {

    // ✅ 场景：全文检索、大型文本文件索引
    public void createTextIndex(Path largeTextFile) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(largeTextFile.toFile(), "r");
             FileChannel channel = raf.getChannel()) {

            // 使用CharBuffer进行随机访问
            long fileSize = channel.size();
            Map<String, List<Long>> wordPositions = new HashMap<>();

            // 分块处理
            long chunkSize = 1024 * 1024; // 1MB
            for (long position = 0; position < fileSize; position += chunkSize) {
                long size = Math.min(chunkSize, fileSize - position);

                MappedByteBuffer buffer = channel.map(
                        FileChannel.MapMode.READ_ONLY, position, size);

                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                indexChunk(charBuffer, position, wordPositions);
            }

            // 现在可以快速查找单词位置
            System.out.println("索引创建完成，共" + wordPositions.size() + "个单词");
        }
    }

    private void indexChunk(CharBuffer buffer, long basePosition,
                            Map<String, List<Long>> index) {
        StringBuilder word = new StringBuilder();

        while (buffer.hasRemaining()) {
            char c = buffer.get();

            if (Character.isLetterOrDigit(c)) {
                word.append(c);
            } else if (word.length() > 0) {
                String wordStr = word.toString().toLowerCase();
                long position = basePosition + buffer.position() - word.length() - 1;

                index.computeIfAbsent(wordStr, k -> new ArrayList<>())
                        .add(position);

                word.setLength(0);
            }
        }
    }
}
