package com.example.restservice.controller;

import com.example.restservice.bean.User;
import com.example.restservice.bean.vo.RequestBaseBody;
import com.example.restservice.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @className: JsonController
 * @author: geeker
 * @date: 11/17/25 12:53 PM
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/param")
public class ParamController {

    @Autowired
    private ParamService paramService;

    @GetMapping
    public String get() {
        return paramService.param("aaa",new User().setName("aaa").setAge(11));
    }




}
