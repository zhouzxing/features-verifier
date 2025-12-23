package com.example.restservice.annotation.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @className: EmailNotificationStrategy
 * @author: geeker
 * @date: 11/10/25 3:44 PM
 * @Version: 1.0
 * @description:
 */
@Component
@ConditionalOnProperty(name = "notification.type",havingValue = "sms")
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public void send(String message) {
        System.out.println("using sms to send " + message);
    }
}
