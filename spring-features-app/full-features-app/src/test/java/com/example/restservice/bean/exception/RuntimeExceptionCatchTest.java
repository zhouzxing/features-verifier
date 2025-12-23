package com.example.restservice.bean.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @className: CatchThrowTest
 * @author: geeker
 * @date: 11/10/25 4:20 PM
 * @Version: 1.0
 * @description:
 */
@Slf4j
public class RuntimeExceptionCatchTest {

    private int count = 3;

    @Test
    public void test(){
        System.out.println("test init");
        try {
            System.out.println("try init");
            if (count > 2) {
                throw new RuntimeException("throw exception");
            }
            log.info("after throw exception");
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("finally init");
        }
    }

    @Test
    public void testThreadPool() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("test init");
                try {
                    System.out.println("try init");
                    if (count > 2) {
                        throw new RuntimeException("throw exception");
                    }
                    log.info("after throw exception");
                }catch (RuntimeException e){
                    log.info("catch exception:" + e.getCause());
                }
            }
        });

        Thread.sleep(10000);
    }

}
