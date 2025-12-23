package com.example.restservice.thread.aqs;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @className: CompletableFutureApp
 * @author: geeker
 * @date: 11/18/25 9:23 AM
 * @Version: 1.0
 * @description:
 */

public class CompletableFutureApp {
    public static void main(String[] args) {
        CompletableFutureApp app = new CompletableFutureApp();
        app.start();
        app.stop();
    }

    private void stop() {
        try {
            CompletableFuture.allOf(increment,decrement).orTimeout(8, TimeUnit.SECONDS).join();
        }catch (Exception e) {
            System.out.println("App stoped!");
        }
        pool.shutdownNow();
    }

    private ExecutorService pool = Executors.newScheduledThreadPool(2);
    private AtomicInteger count = new AtomicInteger(0);
    private CompletableFuture increment;
    private CompletableFuture decrement;

    public void start(){
        increment = CompletableFuture.runAsync(this::increment,pool);
        decrement = CompletableFuture.runAsync(this::decrement,pool);
    }

    public void increment(){
        try {
            while (!Thread.currentThread().isInterrupted()) {
                count.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + " : " + count.get());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void decrement(){
        try {
            while (!Thread.currentThread().isInterrupted()) {
                count.decrementAndGet();
                System.out.println(Thread.currentThread().getName() + " : " + count.get());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
