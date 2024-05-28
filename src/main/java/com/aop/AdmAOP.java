package com.aop;

import com.aop.annotation.*;
import com.ren.administrator.dto.LoginState;
import com.ren.monitor.dto.Monitor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class AdmAOP {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("monitorIntMon")
    private RedisTemplate<Integer, Monitor> redisTemplate;

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
        Integer admNo = null;
        Monitor monitor = null;
        String currentTime = null;
        LoginState loginState = null;
        // 獲取當前HTTP請求的Session
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                // 從Session中獲取loginState對象
                loginState = (LoginState) session.getAttribute("loginState");
                if (loginState != null) {
                    // 獲取當前時間
                    admNo = loginState.getAdmNo();
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String messageId = "message:" + UUID.randomUUID().toString();
                    monitor = new Monitor(messageId, loginState.getAdmNo(), loginState.getAdmName(), loginState.getTitleNo(), employee.message(), currentTime, false);
                }
            }
        }

//        // 儲存消息到 Redis
//        String messageId = "message:" + System.currentTimeMillis();
//        redisTemplate.opsForHash().put(admNo, messageId, monitor);
        // 將消息發送到 RabbitMQ
        rabbitTemplate.convertAndSend("adminHeadersExchange", "", monitor, message -> {
            message.getMessageProperties().setHeader("title", employee.title());
            return message;
        });
    }

    @AfterReturning(value = "@annotation(fullTime)")
    public void fullTime(JoinPoint joinPoint, FullTime fullTime) {
        Integer admNo = null;
        Monitor monitor = null;
        String currentTime = null;
        LoginState loginState = null;
        // 獲取當前HTTP請求的Session
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                // 從Session中獲取loginState對象
                loginState = (LoginState) session.getAttribute("loginState");
                if (loginState != null) {
                    // 獲取當前時間
                    admNo = loginState.getAdmNo();
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String messageId = "message:" + UUID.randomUUID().toString();
                    monitor = new Monitor(messageId, loginState.getAdmNo(), loginState.getAdmName(), loginState.getTitleNo(), fullTime.message(), currentTime, false);
                }
            }
        }

        rabbitTemplate.convertAndSend("adminHeadersExchange", "", monitor, message -> {
            message.getMessageProperties().setHeader("title", fullTime.title());
            return message;
        });
    }

    @AfterReturning(value = "@annotation(manager)")
    public void manager(JoinPoint joinPoint, Manager manager) {
        Integer admNo = null;
        Monitor monitor = null;
        String currentTime = null;
        LoginState loginState = null;
        // 獲取當前HTTP請求的Session
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                // 從Session中獲取loginState對象
                loginState = (LoginState) session.getAttribute("loginState");
                if (loginState != null) {
                    // 獲取當前時間
                    admNo = loginState.getAdmNo();
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String messageId = "message:" + UUID.randomUUID().toString();
                    monitor = new Monitor(messageId, loginState.getAdmNo(), loginState.getAdmName(), loginState.getTitleNo(), manager.message(), currentTime, false);
                }
            }
        }

        rabbitTemplate.convertAndSend("adminHeadersExchange", "", monitor, message -> {
            message.getMessageProperties().setHeader("title", manager.title());
            return message;
        });

    }

    @AfterReturning(value = "@annotation(boss)")
    public void boss(JoinPoint joinPoint, Boss boss) {
        Integer admNo = null;
        Monitor monitor = null;
        String currentTime = null;
        LoginState loginState = null;
        // 獲取當前HTTP請求的Session
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                // 從Session中獲取loginState對象
                loginState = (LoginState) session.getAttribute("loginState");
                if (loginState != null) {
                    // 獲取當前時間
                    admNo = loginState.getAdmNo();
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String messageId = "message:" + UUID.randomUUID().toString();
                    monitor = new Monitor(messageId, loginState.getAdmNo(), loginState.getAdmName(), loginState.getTitleNo(), boss.message(), currentTime, false);
                }
            }
        }

        rabbitTemplate.convertAndSend("adminHeadersExchange", "", monitor, message -> {
            message.getMessageProperties().setHeader("title", boss.title());
            return message;
        });
    }

    @AfterReturning(value = "@annotation(job)")
    public void fullTime(JoinPoint joinPoint, Job job) {
        Integer admNo = null;
        Monitor monitor = null;
        String currentTime = null;
        LoginState loginState = null;
        // 獲取當前HTTP請求的Session
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                // 從Session中獲取loginState對象
                loginState = (LoginState) session.getAttribute("loginState");
                if (loginState != null) {
                    // 獲取當前時間
                    admNo = loginState.getAdmNo();
                    currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String messageId = "message:" + UUID.randomUUID().toString();
                    monitor = new Monitor(messageId, loginState.getAdmNo(), loginState.getAdmName(), loginState.getTitleNo(), job.detail(), currentTime, false);
                }
            }
        }

        rabbitTemplate.convertAndSend("adminHeadersExchange", "", monitor, message -> {
            message.getMessageProperties().setHeader("type", job.type());
            return message;
        });
    }

    @AfterReturning(value = "@annotation(updateRedisDB)")
    public void redisDBMonitor(JoinPoint joinPoint, UpdateRedisDB updateRedisDB ) {
        String message = updateRedisDB.message();
        rabbitTemplate.convertAndSend("topicExchangeName", "updateRedis", message);
    }

}
