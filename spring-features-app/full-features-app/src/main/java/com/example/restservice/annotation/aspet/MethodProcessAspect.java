package com.example.restservice.annotation.aspet;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @className: MethodProcessAspect
 * @author: geeker
 * @date: 11/10/25 4:46 PM
 * @Version: 1.0
 * @description:
 */
@Aspect
@Component
@Slf4j
@Order(2)
public class MethodProcessAspect {

    //  定义切点
    @Pointcut("@annotation(processMethod)")
    public void methodPointCut(ProcessMethod processMethod) {}

    @Around("methodPointCut(processMethod)")
    public Object around(ProceedingJoinPoint joinPoint, ProcessMethod processMethod) throws Throwable {
        Object obj = null;
        long startTime = System.currentTimeMillis();
        // 获取切点增强方法与类信息
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        if (processMethod.logged()) {
            log.info("[{}] 开始执行 Method [{}] executed in [{}]", processMethod.type(),methodName, className);
        }
        int attempt = 0;
        while (attempt < processMethod.retry()) {
            try {
                obj = joinPoint.proceed();
                break;
            } catch (Throwable throwable) {
                attempt++;
                log.warn("方法执行失败，第{}次重试：{}.{}", attempt,className, methodName);
                Thread.sleep(1000);
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("[{}] 方法执行完成{}.{}，耗时：{} ms",
                processMethod.type(),className,methodName,endTime - startTime);
        return obj;
    }



}
