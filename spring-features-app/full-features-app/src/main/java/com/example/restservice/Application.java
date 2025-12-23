package com.example.restservice;

import com.example.restservice.service.SingletonService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication(exclude = AopAutoConfiguration.class)
@MapperScan("com.example.restservice.dao")
@EnableScheduling
//@EnableAspectJAutoProxy(proxyTargetClass = false)
public class Application /*implements CommandLineRunner*/ {

    private static final Logger log = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("Application started");
    }

    @Autowired
//    @Lazy
    private SingletonService singletonService;

    @Autowired
    private ApplicationContext context;

    //@Override
    public void run(String... args) throws Exception {
        System.out.println(singletonService);

        String[] names = context.getBeanNamesForType(SingletonService.class);
        if (names.length > 0) {
            System.out.println("懒加载但未初始化:\t" + Arrays.toString(names));
        }

        SingletonService bean = context.getBean(SingletonService.class);
        System.out.println(bean == singletonService);
    }
}
