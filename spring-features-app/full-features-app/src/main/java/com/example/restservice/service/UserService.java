package com.example.restservice.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.restservice.bean.User;
import com.example.restservice.dao.UserMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @className: UserService
 * @author: geeker
 * @date: 10/16/25 2:59 PM
 * @Version: 1.0
 * @description:
 */
@Service
public class UserService {

    void aop(){
        System.out.println("AOP AOP");
    }


    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private UserService self;

    @Transactional
    public void methodA(){
        User user = new User().setAge(12).setName("aaa");

        System.out.println("before methodB");
        //methodB();
        //self.methodB();

        UserService userService = (UserService) AopContext.currentProxy();
        try {
            userService.methodB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("after methodB");

        userMapper.insert(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodB(){
        User user = new User().setAge(15).setName("bbb");
        int insert = userMapper.insert(user);
        if (insert > 0) {
            throw new RuntimeException("insert failed");
        }
    }

    @Transactional
    public void methodG(){
        User user = new User().setAge(12).setName("ggg");

        System.out.println("before methodH");
        methodH();
        System.out.println("after methodH");

        userMapper.insert(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void methodH(){
        User user = new User().setAge(15).setName("hhh");
        int insert = userMapper.insert(user);
        if (insert > 0) {
            throw new RuntimeException("insert failed");
        }
    }

    @Transactional
    public void methodE(){
        User user = new User().setAge(12).setName("eee");

        System.out.println("before methodF");
        methodF();
        System.out.println("after methodF");

        userMapper.insert(user);
    }
    public void methodF(){
        User user = new User().setAge(15).setName("fff");
        int insert = userMapper.insert(user);
        if (insert > 0) {
            throw new RuntimeException("insert failed");
        }
    }

    @Transactional
    public void methodC(){
        User user = new User().setAge(12).setName("aaa");
        methodD();
        userMapper.insert(user);
    }

    public void methodD(){
        User user = new User().setAge(15).setName("bbb");
        int insert = userMapper.insert(user);
    }


    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    public User getByName(String name) {
        return userMapper.selectOne(Wrappers.<User>query().eq("name",name));
        //return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName,name));
        //return userMapper.selectOne(Wrappers.query(new User().setName(name)));
        //return userMapper.selectOne(Wrappers.lambdaQuery(new User().setName(name)));
    }

    public List<User> getAll() {
        return userMapper.selectList(Wrappers.query());
    }
}
