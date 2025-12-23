package com.example.restservice.bean.email;

import org.junit.Test;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

/**
 * @className: SimpleMailTest
 * @author: geeker
 * @date: 9/12/25 3:25 PM
 * @Version: 1.0
 * @description:
 */

public class SimpleMailTest {

    @Test
    public void test() {
        Email email = EmailBuilder.startingBlank()
                .from("From Name", "1097784186@qq.com")
                .to("To Name", "17675616902@163.com")
                .withSubject("Hello!")
                .appendTextHTML("<b>This is an HTML message simpleMail</b>")
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.qq.com", 465, "1097784186@qq.com", "bskunsfbqkdpgdia")
                .buildMailer();
        mailer.sendMail(email);
    }
}
