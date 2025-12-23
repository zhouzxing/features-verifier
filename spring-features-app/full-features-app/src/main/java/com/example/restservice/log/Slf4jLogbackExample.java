package com.example.restservice.log;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @className: Slf4jLogbackExample
 * @author: geeker
 * @date: 11/10/25 1:03 PM
 * @Version: 1.0
 * @description:
 */
//@XSlf4j
@Slf4j
public class Slf4jLogbackExample {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jLogbackExample.class);

    public static void main(String[] args) {
        log.info("Hello @Slf4j");
        logger.info("Hello loggerFactory");

        try {
            int result = 10 / 2;
            log.debug("result {}", result);

            throw new RuntimeException("A test error occurred");
        }catch (Exception e) {
            log.error("An error occured: ", e.getMessage());
        }

        log.warn("Application is about to exit.");
    }
}
