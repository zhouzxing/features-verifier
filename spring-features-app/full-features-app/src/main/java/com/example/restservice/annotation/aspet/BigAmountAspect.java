package com.example.restservice.annotation.aspet;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @className: BigAmountAspect
 * @author: geeker
 * @date: 11/10/25 5:40 PM
 * @Version: 1.0
 * @description:
 */
@Aspect
@Slf4j
@Component
@Order(1)
public class BigAmountAspect {

    @Around("@annotation(bigAmount)")
    public Object around(ProceedingJoinPoint joinPoint, BigAmount bigAmount) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("[{}] 处理 {}.{} start", bigAmount.condition(),className,methodName);
        String condition = bigAmount.condition();

        Expression expression = new SpelExpressionParser().parseExpression(condition);
        EvaluationContext context = new StandardEvaluationContext();

        // 获取上下文变量
        context.setVariable("amount", joinPoint.getArgs()[0]);
        boolean flag = expression.getValue(context, Boolean.class);
        if (flag) {
            log.info("[{}] 校验通过 {}.{} end", bigAmount.condition(),className,methodName);
            return joinPoint.proceed();
        }
        log.info("[{}] 处理 {}.{} end", bigAmount.condition(),className,methodName);
        return null;
    }

    @After("@annotation(bigAmount)")
    public Object afterBigAmount(JoinPoint joinPoint, BigAmount bigAmount) throws Throwable {


        return null;
    }



    @After("@annotation(BigAmount)")
    public Object after(JoinPoint joinPoint) throws Throwable {

        return null;
    }



    @Before("@annotation(bigAmount)")
    public Object before(JoinPoint joinPoint, BigAmount bigAmount) throws Throwable {

        return null;
    }


}
