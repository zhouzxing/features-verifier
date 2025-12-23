package com.example.restservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: HomeController
 * @author: geeker
 * @date: 9/10/25 4:41 PM
 * @Version: 1.0
 * @description:
 */

@Controller()
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "client";
    }



}
