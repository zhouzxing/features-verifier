package com.example.restservice.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @className: LogAspect
 * @author: geeker
 * @date: 11/16/25 3:27 PM
 * @Version: 1.0
 * @description:
 */
@Aspect
@Component
@Slf4j
public class LogMonitorAspect {

/*    @Before("execution(* com.example.restservice.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("方法执行：\t" + joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName());
    }*/

    @Around("@annotation(logMonitor)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogMonitor logMonitor) throws Throwable {
        log.info(logMonitor.name()+"...start!");
        Object result = joinPoint.proceed();
        log.info(logMonitor.name()+"...end!");
        return result;
    }
}
