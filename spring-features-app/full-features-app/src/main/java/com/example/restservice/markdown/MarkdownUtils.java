package com.example.restservice.markdown;

import org.commonmark.node.Document;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.markdown.MarkdownRenderer;

/**
 * @className: MarkdownUtils
 * @author: geeker
 * @date: 8/7/25 8:41 PM
 * @Version: 1.0
 * @description:
 */

public class MarkdownUtils {
    public static void main(String[] args) {

        /*Parser parser = Parser.builder().build();
        Node document = parser.parse("This is *Markdown*");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        renderer.render(document);  // "<p>This is <em>Markdown</em></p>\n"
        System.out.println(renderer.render(document));*/

        MarkdownRenderer renderer = MarkdownRenderer.builder().build();
        Node document = new Document();
        Heading heading = new Heading();
        heading.setLevel(2);
        heading.appendChild(new Text("My title"));
        document.appendChild(heading);

        System.out.println(renderer.render(document));;  // "## My title\n"
    }
}
