package com.example.restservice.bean.task;

import org.junit.Test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @className: TimerTest
 * @author: geeker
 * @date: 11/14/25 11:31 AM
 * @Version: 1.0
 * @description:
 */

public class TimerTest {

    class SimpleTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("SimpleTask running:\t" + new Date());
        }
    }

    class RepeatableTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("RepeatableTask running:\t" + new Date());
        }
    }

    class FixedRateTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("FixedRateTask running:\t" + new Date() + this);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class FixedRateTaskAsync extends TimerTask {
        @Override
        public void run() {
            System.out.println("FixedRateTask running:\t" + new Date() + this);
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }



    @Test
    public void test() throws InterruptedException {
        Timer timer = new Timer();
//        timer.schedule(new SimpleTask(), 1000);
//        timer.schedule(new RepeatableTask(), new Date(), 1000);
//        timer.schedule(new FixedRateTask(), new Date(), 1000);
//        timer.scheduleAtFixedRate(new FixedRateTask(), new Date(), 1000);

        timer.schedule(new FixedRateTaskAsync(), new Date(), 1000);
        timer.scheduleAtFixedRate(new FixedRateTaskAsync(), new Date(), 1000);


        Thread.sleep(TimeUnit.MINUTES.toMillis(2));

    }





}
