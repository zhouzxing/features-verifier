package com.example.restservice.controller;

import com.example.restservice.service.ResourceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: HomeController
 * @author: geeker
 * @date: 9/10/25 4:41 PM
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/resource")
public class ResourceController {


    @Qualifier("resourceServiceImp")
    @Autowired
    private ResourceService resourceService;


    @Qualifier("resourceServiceImp0")
    @Autowired
    private ResourceService resourceService0;

    // name
    //@Resource(lookup = "resourceServiceImp0")
    @Resource
    private ResourceService resourceServiceImp0;

    /*@GetMapping("/name")
    public String index(){
        return resourceService.resource();
    }*/

    /*@GetMapping("/name0")
    public String index0(){
        return resourceService0.resource();
    }*/


    @GetMapping("/name1")
    public String index1(){
        return resourceService0.resource();
    }





}
