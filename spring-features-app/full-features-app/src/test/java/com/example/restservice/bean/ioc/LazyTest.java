package com.example.restservice.bean.ioc;

import com.example.restservice.service.SingletonService;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

/**
 * @className: BeanTest
 * @author: geeker
 * @date: 11/17/25 2:33 PM
 * @Version: 1.0
 * @description:
 */
@SpringBootTest
public class LazyTest {

    @Autowired
    @Lazy
    private SingletonService singletonService;

    @Test
    public void testSingletonService() {
        System.out.println(singletonService);
        System.out.println(singletonService.getInstanceInfo());
    }
}
