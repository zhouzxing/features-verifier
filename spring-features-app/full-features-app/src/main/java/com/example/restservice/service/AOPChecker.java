package com.example.restservice.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @className: AOPChecker
 * @author: geeker
 * @date: 11/16/25 3:34 PM
 * @Version: 1.0
 * @description:
 */
@Component
public class AOPChecker {

    @Autowired
    private IAopWithNoMethodService aopWithNoMethodService;

   @Autowired
   private IAopWithMethodService aopWithMethodService;

    @Autowired
    private UserService userService;


//    @PostConstruct
    public void init() {
        System.out.println("AOP Checker test!");
        System.out.println(aopWithMethodService.getClass().getName());
        System.out.println(aopWithNoMethodService.getClass().getName());
        System.out.println(userService.getClass().getName());

        userService.aop();
        aopWithMethodService.save();
    }
}
