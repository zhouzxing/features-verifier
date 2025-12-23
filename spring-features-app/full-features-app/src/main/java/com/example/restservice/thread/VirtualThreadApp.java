package com.example.restservice.thread;

/**
 * @className: VirtualThread
 * @author: geeker
 * @date: 9/15/25 5:30 PM
 * @Version: 1.0
 * @description:
 */

public class VirtualThreadApp {

    public static void main(String[] args) {
        Thread.startVirtualThread(() -> {
            System.out.println("Starting virtual thread");
        });
    }
}
