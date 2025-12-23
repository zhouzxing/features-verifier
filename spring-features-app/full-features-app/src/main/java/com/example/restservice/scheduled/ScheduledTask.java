package com.example.restservice.scheduled;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @className: ScheduledTask
 * @author: geeker
 * @date: 11/14/25 1:49 PM
 * @Version: 1.0
 * @description:
 */
@Component
public class ScheduledTask {

//    @Scheduled(fixedRate = 1000)
    public void scheduledTask() throws InterruptedException {
        System.out.println("scheduledTask:\t" + System.currentTimeMillis() + "date:\t" + new Date());
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }
}
