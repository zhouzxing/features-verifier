package com.example.restservice.controller;

import com.example.restservice.bean.vo.RequestBaseBody;
import org.springframework.web.bind.annotation.*;

/**
 * @className: JsonController
 * @author: geeker
 * @date: 11/17/25 12:53 PM
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/json")
public class JsonController {

    @GetMapping
    public String get() {
        return "Get Json";
    }

    @PostMapping
    public String post() {
        return "Post Json";
    }

    @PostMapping("/basetype")
    public String json(@RequestBody RequestBaseBody requestBaseBody){
        System.out.println(requestBaseBody);
        return "Post Json";
    }



}
