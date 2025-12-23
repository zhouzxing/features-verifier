package com.example.restservice.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * @className: NioApp
 * @author: geeker
 * @date: 12/1/25 7:06 PM
 * @Version: 1.0
 * @description:
 */

public class NioApp {

    public static void main(String[] args) {
         File file = new File("classpath:data/report.md");

        try (InputStream resourceAsStream =
                     NioApp.class.getClassLoader().getResourceAsStream("data/report.md");
             //InputStream inputStream = Files.newInputStream(file.toPath());
             //FileInputStream fileInputStream = new FileInputStream(String.valueOf(resourceAsStream));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        ) {
            // List<String> strings = Files.readAllLines(, Charset.defaultCharset());

            //strings = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));

            //Files.readAllLines(fileInputStream)

            List<String> strings = bufferedReader.lines().toList();
            System.out.println(strings);


            // Buffer 对比
            ByteBuffer buffer = ByteBuffer.allocate(1024);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
