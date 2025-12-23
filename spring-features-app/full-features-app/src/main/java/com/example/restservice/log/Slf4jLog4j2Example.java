package com.example.restservice.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @className: Slf4jLog4j2Example
 * @author: geeker
 * @date: 11/10/25 1:18 PM
 * @Version: 1.0
 * @description:
 */

public class Slf4jLog4j2Example {
    // 代码与上面的例子完全相同！
    private static final Logger logger = LoggerFactory.getLogger(Slf4jLog4j2Example.class);

    public static void main(String[] args) {
        logger.info("This is using Log4j2 as the implementation!");

        String user = "Alice";
        int age = 30;
        logger.info("User {} is {} years old.", user, age); // 多参数占位符

        logger.debug("This is a debug message.");
    }

}
