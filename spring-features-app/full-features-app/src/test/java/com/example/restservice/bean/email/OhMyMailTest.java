package com.example.restservice.bean.email;

import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

/**
 * @className: EmailTest
 * @author: geeker
 * @date: 9/12/25 2:24 PM
 * @Version: 1.0
 * @description:
 */

public class OhMyMailTest {
    @Before
    public void before() throws GeneralSecurityException {
        // 配置，一次即可
        OhMyEmail.config(SMTP_QQ(false), "1097784186@qq.com", "bskunsfbqkdpgdia");
    }

    @Test
    public void testSendText() throws MessagingException, SendMailException {
        OhMyEmail.subject("这是一封测试TEXT邮件")
                .from("xz的qq邮箱")
                .to("17675616902@163.com")
                .text("信件内容: test")
                .send();
    }

    @Test
    public void testSendHtml() throws Exception {
        OhMyEmail.subject("这是一封测试HTML邮件")
                .from("小姐姐的邮箱")
                .to("xiaojiejie@gmail.com")
                .html("<h1 font=red>信件内容</h1>")
                .send();
    }

    @Test
    public void testSendAttach() throws Exception {
        OhMyEmail.subject("这是一封测试附件邮件")
                .from("小姐姐的邮箱")
                .to("17675616902@163.com")
                .html("<h1 font=red>信件内容</h1>")
                .attach(new File("/home/geeker/Pictures/head.png"), "head.png ddd")
                .send();
    }

    @Test
    public void testSendAttachURL() throws Exception {
        try {
            OhMyEmail.subject("这是一封测试网络资源作为附件的邮件")
                    .from("小姐姐的邮箱")
                    .to("xiaojiejie@gmail.com")
                    .html("<h1 font=red>信件内容</h1>")
                    .attachURL(new URL("https://avatars1.githubusercontent.com/u/2784452?s=40&v=4"), "测试图片.jpeg")
                    .send();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPebble() throws IOException, PebbleException, MessagingException, SendMailException {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("register.html");

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("username", "biezhi");
        context.put("email", "admin@biezhi.me");

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
        System.out.println(output);

        OhMyEmail.subject("这是一封测试Pebble模板邮件")
                .from("小姐姐的邮箱")
                .to("xiaojiejie@gmail.com")
                .html(output)
                .send();
    }

    @Test
    public void testJetx() throws IOException, PebbleException, MessagingException, SendMailException {
        JetEngine engine = JetEngine.create();
        JetTemplate template = engine.getTemplate("/register.jetx");

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("username", "biezhi");
        context.put("email", "admin@biezhi.me");
        context.put("url", "<a href='http://biezhi.me'>https://biezhi.me/active/asdkjajdasjdkaweoi</a>");

        StringWriter writer = new StringWriter();
        template.render(context, writer);
        String output = writer.toString();
        System.out.println(output);

        OhMyEmail.subject("这是一封测试Jetx模板邮件")
                .from("小姐姐的邮箱")
                .to("xiaojiejie@gmail.com")
                .html(output)
                .send();
    }
}
