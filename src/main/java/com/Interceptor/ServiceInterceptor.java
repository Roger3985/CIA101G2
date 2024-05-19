package com.Interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceInterceptor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Before("execution(* com.*.*.service.impl.*.*(..))")
    public void before(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("Called: " + methodName + " with args: " + Arrays.toString(args));
    }

    @AfterReturning("execution(* com.*.*.service.impl.*.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Called: " + methodName + "執行成功!");

        // 生成要發送的消息
        String message = "Method: " + methodName + " executed with result: test";

        // 這裡指定適當的路由鍵
        String routingKey = "1"; // 根據需要更改

        // 將消息發送到 RabbitMQ
        rabbitTemplate.convertAndSend("adminExchange", routingKey, message);
    }
}
