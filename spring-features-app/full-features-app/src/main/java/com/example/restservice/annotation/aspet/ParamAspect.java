package com.example.restservice.annotation.aspet;

import com.example.restservice.bean.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @className: ParamAspect
 * @author: geeker
 * @date: 12/22/25 2:58 PM
 * @Version: 1.0
 * @description:
 */
@Aspect
@Component
public class ParamAspect {

    /*@Pointcut("@annotation(ParamDeal)")
    public void methodPointCut(ProcessMethod processMethod) {}*/

    /**
     *
     * @param joinPoint
     * @param paramDeal
     * @return
     * @throws Throwable
     */
    @Around("@annotation(paramDeal)")
    public Object around(ProceedingJoinPoint joinPoint, ParamDeal paramDeal) throws Throwable {

        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
        User user = (User)args[1];
        user.setName("ddd");

        Object result = joinPoint.proceed();
        return result;
    }
}
