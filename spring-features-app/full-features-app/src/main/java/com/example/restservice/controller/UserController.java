package com.example.restservice.controller;

import com.example.restservice.annotation.LogMonitor;
import com.example.restservice.bean.User;
import com.example.restservice.dao.UserMapper;
import com.example.restservice.service.IAopWithMethodService;
import com.example.restservice.service.IAopWithNoMethodService;
import com.example.restservice.service.UserService;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @className: HomeController
 * @author: geeker
 * @date: 9/10/25 4:41 PM
 * @Version: 1.0
 * @description:
 */

//@Controller("/user")
@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IAopWithMethodService aopService;

    @Autowired
    private IAopWithNoMethodService aopWithNoMethodService;

    @GetMapping("/{id}")
    @LogMonitor(name = "byId query")
    public User user(@PathVariable Long id){
        //aopService.save();
        return userService.getById(id);
    }

    @GetMapping
    public List<User> user(){
        aopService.save();
        return userService.getAll();
    }

    @GetMapping("/name")
    public User user(@RequestParam String name){
        aopWithNoMethodService.save();
        return userService.getByName(name);
    }

    //
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/batch")
    public List<BatchResult> batch(){
        List<User> users = userService.getAll();

        return userMapper.insertOrUpdate(users);
    }

    @GetMapping("/batchAdv")
    public Integer batchAdv(){
        List<User> users = userService.getAll();
        return userMapper.insertOrUpdateAdv(users);
    }

    @GetMapping("/batchAdv1")
    public Integer batchAdv1(){
        List<User> users = userService.getAll();
        userMapper.insertOrUpdatePrimaryKey(users);

        List<User> usersUnique = Arrays.asList(
                new User().setId(113L).setName("Jone").setAge(18).setEmail("ccc@aa.com"),
                new User().setId(112L).setName("Jack").setAge(20).setEmail("ccc@aa.com"),
                new User().setId(111L).setName("111").setAge(111).setEmail("ccc@aa.com")
        );
        userMapper.insertOrUpdateUniqueKey(usersUnique);
        return 1;
    }


    @GetMapping("/transactionS")
    public User transactionS(){
        userService.methodC();
        return null;
    }


    @GetMapping("/tranRequireNew")
    public User tranRequreNew(){
        userService.methodA();
        return null;
    }
    @GetMapping("/tranMandatory")
    public User tranMandatory(){
        userService.methodG();
        return null;
    }
    @GetMapping("/tran")
    public User transactionA(){
        userService.methodE();
        return null;
    }





}
