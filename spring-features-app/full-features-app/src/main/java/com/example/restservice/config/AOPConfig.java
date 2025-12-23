package com.example.restservice.config;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @className: AOPConfig
 * @author: geeker
 * @date: 11/16/25 3:23 PM
 * @Version: 1.0
 * @description:
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false, exposeProxy = true)
@Order(Ordered.HIGHEST_PRECEDENCE)
//@Order(Ordered.LOWEST_PRECEDENCE)
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AOPConfig {


//    @Bean
    public Advisor aopAdvisor() {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* com.example.restservice.*.*(..))");

        Advice advice = new MethodBeforeAdvice(){
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("Before advice");
            }
        };

        return new DefaultPointcutAdvisor(aspectJExpressionPointcut,advice);
    }
}
