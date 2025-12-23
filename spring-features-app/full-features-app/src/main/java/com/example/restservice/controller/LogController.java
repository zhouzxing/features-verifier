package com.example.restservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: LogController
 * @author: geeker
 * @date: 11/10/25 2:32 PM
 * @Version: 1.0
 * @description:
 */
@RestController

@Slf4j
public class LogController {

    Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log")
    public void log() {
        System.out.println("log method is called");
        log.info("log info");
        log.debug("log debug");
        log.warn("log warn");
        log.error("log error");

        logger.trace("log trace");
        logger.debug("log debug");
        logger.info("log info");
    }
}
