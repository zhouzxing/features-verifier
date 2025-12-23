package com.example.restservice.io;

/**
 * @className: BufferBench
 * @author: geeker
 * @date: 12/1/25 7:44 PM
 * @Version: 1.0
 * @description:
 */

import lombok.SneakyThrows;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class BufferBench {

    private static final int TEST_ITERATIONS = 10;
    private static final int BUFFER_SIZE = 8192;
    private static final Path LARGE_TEXT_FILE = Paths.get("spy.log");

    public static void main(String[] args) throws Exception {
        // 准备测试文件（100MB文本）
        createTestFile(100 * 1024 * 1024);

        System.out.println("=== CharBuffer vs BufferedReader 性能对比 ===");

        // 预热 -> 分别执行BufferedReader和CharBuffer 三次
        warmUp();

        // 运行测试 -> 迭代10000+求平均
        long bufferedReaderTime = runBenchmark("BufferedReader",
                BufferBench::testBufferedReader);

        long charBufferTime = runBenchmark("CharBuffer (堆内)",
                BufferBench::testCharBufferHeap);

        long charBufferDirectTime = runBenchmark("CharBuffer (直接)",
                BufferBench::testCharBufferDirect);

        long charBufferMappedTime = runBenchmark("CharBuffer (内存映射)",
                BufferBench::testCharBufferMapped);

        System.out.println("\n=== 性能对比结果 ===");
        System.out.printf("BufferedReader:     %10d ms%n", bufferedReaderTime);
        System.out.printf("CharBuffer(堆内):   %10d ms%n", charBufferTime);
        System.out.printf("CharBuffer(直接):   %10d ms%n", charBufferDirectTime);
        System.out.printf("CharBuffer(映射):   %10d ms%n", charBufferMappedTime);

        // 清理
        Files.deleteIfExists(LARGE_TEXT_FILE);
    }

    private static void createTestFile(long sizeBytes) throws IOException {
        System.out.println("创建测试文件...");
        if (Files.exists(LARGE_TEXT_FILE)) {
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(LARGE_TEXT_FILE)) {
            long written = 0;
            int lineNum = 0;

            while (written < sizeBytes) {
                String line = String.format("Line %08d: %s\n",
                        lineNum++,
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
                writer.write(line);
                written += line.length();
            }
        }
    }

    // 预热JVM： 有什么用？
    private static void warmUp() throws IOException {
        System.out.println("预热JVM...");
        for (int i = 0; i < 3; i++) {
            testBufferedReader();
            testCharBufferHeap();
        }
    }

    private static long runBenchmark(String name, Runnable test) throws Exception {
        System.out.printf("测试 %s... ", name);
        long totalTime = 0;

        for (int i = 0; i < TEST_ITERATIONS; i++) {
            long start = System.nanoTime();
            test.run();
            long end = System.nanoTime();
            totalTime += TimeUnit.NANOSECONDS.toMillis(end - start);
        }

        long avgTime = totalTime / TEST_ITERATIONS;
        System.out.printf("平均: %d ms%n", avgTime);
        return avgTime;
    }

    // 测试方法：BufferedReader
    private static void testBufferedReader() throws IOException{
        try (BufferedReader reader = Files.newBufferedReader(LARGE_TEXT_FILE,
                StandardCharsets.UTF_8)) {

            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;
            long totalChars = 0;

            while ((charsRead = reader.read(buffer)) != -1) {
                totalChars += charsRead;
                // 模拟处理：计算字符数
                for (int i = 0; i < charsRead; i++) {
                    if (buffer[i] == 'A') {
                        // 简单处理
                    }
                }
            }
        }
    }

    // 测试方法：CharBuffer（堆内）
    private static void testCharBufferHeap() throws IOException {
        try (FileChannel channel = FileChannel.open(LARGE_TEXT_FILE,
                StandardOpenOption.READ)) {

            // 使用CharsetDecoder将字节转换为字符
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            CharBuffer charBuffer = CharBuffer.allocate(BUFFER_SIZE);

            long totalChars = 0;

            while (channel.read(byteBuffer) != -1) {
                byteBuffer.flip();

                CoderResult result = decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();

                totalChars += charBuffer.remaining();

                // 模拟处理
                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();
                    if (c == 'A') {
                        // 简单处理
                    }
                }

                charBuffer.clear();
                byteBuffer.compact();
            }
        }
    }

    // 测试方法：CharBuffer（直接内存）
    private static void testCharBufferDirect() throws IOException {
        try (FileChannel channel = FileChannel.open(LARGE_TEXT_FILE,
                StandardOpenOption.READ)) {

            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            CharBuffer charBuffer = CharBuffer.allocate(BUFFER_SIZE);

            long totalChars = 0;

            while (channel.read(byteBuffer) != -1) {
                byteBuffer.flip();

                decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();

                totalChars += charBuffer.remaining();

                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();
                    if (c == 'A') {
                        // 简单处理
                    }
                }

                charBuffer.clear();
                byteBuffer.clear();
            }
        }
    }

    // 测试方法：CharBuffer（内存映射）
    private static void testCharBufferMapped() throws IOException {
        try (FileChannel channel = FileChannel.open(LARGE_TEXT_FILE,
                StandardOpenOption.READ)) {

            long fileSize = channel.size();
            MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_ONLY, 0, fileSize);

            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            CharBuffer charBuffer = CharBuffer.allocate(BUFFER_SIZE);

            long totalChars = 0;

            while (mappedBuffer.hasRemaining()) {
                int remaining = Math.min(mappedBuffer.remaining(), BUFFER_SIZE);
                ByteBuffer slice = mappedBuffer.slice();
                slice.limit(remaining);
                mappedBuffer.position(mappedBuffer.position() + remaining);

                decoder.decode(slice, charBuffer, false);
                charBuffer.flip();

                totalChars += charBuffer.remaining();

                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();
                    if (c == 'A') {
                        // 简单处理
                    }
                }

                charBuffer.clear();
                decoder.reset();
            }
        }
    }

    @FunctionalInterface
    private interface Runnable {
        public void run() throws Exception;

    }
}
