package com.aop;

import com.aop.annotation.Boss;
import com.aop.annotation.FullTime;
import com.aop.annotation.Manager;
import com.aop.annotation.Employee;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ren.util.Constants.*;

@Aspect
@Component
public class ServiceAOP {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Before("execution(* com.*.*.service.impl.*.*(..))")
//    public void before(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        System.out.println("Called: " + methodName + " with args: " + Arrays.toString(args));
//    }
//
//    @AfterReturning("execution(* com.*.*.service.impl.*.*(..))")
//    public void afterReturning(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        System.out.println("Called: " + methodName + "執行成功!");
//
//        // 生成要發送的消息
//        String message = "Method: " + methodName + " executed with result: test";
//
//        // 這裡指定適當的路由鍵
//        String routingKey = "1"; // 根據需要更改
//
//        // 將消息發送到 RabbitMQ
//        rabbitTemplate.convertAndSend("adminExchange", routingKey, message);
//    }

    @AfterReturning(value = "@annotation(employee)")
    public void employee(JoinPoint joinPoint, Employee employee) {
        System.out.println("AOP啟動");
        // 生成要發送的消息
        String employeeMsg = employee.message();
        // 將消息發送到 RabbitMQ
        rabbitTemplate.convertAndSend("adminHeadersExchange", "", employeeMsg, message -> {
            message.getMessageProperties().setHeader("title", employee.title());
            return message;
        });
    }

    @AfterReturning(value = "@annotation(fullTime)")
    public void fullTime(JoinPoint joinPoint, FullTime fullTime) {
        // 生成要發送的消息
        String fullTimeMsg = fullTime.message();
        // 將消息發送到 RabbitMQ
        rabbitTemplate.convertAndSend("adminHeadersExchange", fullTime.title(), fullTimeMsg);
    }

    @AfterReturning(value = "@annotation(manager)")
    public void manager(JoinPoint joinPoint, Manager manager) {
        // 生成要發送的消息
        String managerMsg = manager.message();
        // 將消息發送到 RabbitMQ
        rabbitTemplate.convertAndSend("adminHeadersExchange", manager.title(), managerMsg);
    }

    @AfterReturning(value = "@annotation(boss)")
    public void boss(JoinPoint joinPoint, Boss boss) {
        // 生成要發送的消息
        String bossMsg = boss.message();
        // 將消息發送到 RabbitMQ
        rabbitTemplate.convertAndSend("adminHeadersExchange", boss.title(), bossMsg);
    }


}
