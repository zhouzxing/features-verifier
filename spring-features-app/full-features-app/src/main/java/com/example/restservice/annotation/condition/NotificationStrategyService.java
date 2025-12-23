package com.example.restservice.annotation.condition;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: NotificationStrategyService
 * @author: geeker
 * @date: 11/10/25 3:47 PM
 * @Version: 1.0
 * @description:
 */
@Service
public class NotificationStrategyService {

    @Autowired
    private NotificationStrategy notificationStrategy;

    @PostConstruct
    public void init() {
        // notificationStrategy.send("test nofication strategy");
    }
}
