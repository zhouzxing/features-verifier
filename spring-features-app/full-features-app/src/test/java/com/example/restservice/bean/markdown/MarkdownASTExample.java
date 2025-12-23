package com.example.restservice.bean.markdown;

/**
 * @className: MarkdownParserTest
 * @author: geeker
 * @date: 8/12/25 6:00 PM
 * @Version: 1.0
 * @description:
 */

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MarkdownASTExample {

    public static void main(String[] args) {
        // 1. 准备Markdown内容（可以从文件或字符串读取）
        String markdownContent = "# Hello, World!\n\n" +
                "This is a *sample* Markdown document.\n\n" +
                "- Item 1\n" +
                "- Item 2\n\n" +
                "Visit [CommonMark](https://commonmark.org) for more info.";

        // 或者从文件读取
        // String markdownContent = readMarkdownFile("example.md");

        // 2. 创建解析器
        Parser parser = Parser.builder().build();

        // 3. 解析Markdown为AST
        Node document = parser.parse(markdownContent);

        // 4. 遍历AST节点
        System.out.println("AST Structure:");
        printAST(document, 0);
    }

    // 递归打印AST结构
    private static void printAST(Node node, int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        sb.append(node.getClass().getSimpleName());

        // 对于某些节点类型，显示额外信息
        if (node instanceof Text) {
            sb.append(": ").append(((Text) node).getLiteral());
        } else if (node instanceof Link) {
            sb.append(" (destination=").append(((Link) node).getDestination()).append(")");
        } else if (node instanceof Heading) {
            sb.append(" (level=").append(((Heading) node).getLevel()).append(")");
        }

        System.out.println(sb.toString());

        // 递归处理子节点
        Node child = node.getFirstChild();
        while (child != null) {
            printAST(child, indent + 1);
            child = child.getNext();
        }
    }

    // 从文件读取Markdown内容
    private static String readMarkdownFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "";
        }
    }
}
