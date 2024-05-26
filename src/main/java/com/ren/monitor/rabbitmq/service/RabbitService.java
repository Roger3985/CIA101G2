package com.ren.monitor.rabbitmq.service;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RabbitService {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HeadersExchange exchange;

    private final Set<String> queueNames = new HashSet<>();

    public Set<String> getQueueNames() {
        return queueNames;
    }

    public void createQueueForUser(String userAdmNo) {
        String queueName = "queue_" + userAdmNo;
        // 建立會自動銷毀的queue
        Queue queue = new Queue(queueName, true, false, true);
        amqpAdmin.declareQueue(queue);
        // 建立綁定規則
        Binding binding = BindingBuilder.bind(queue).to(exchange).where("userAdmNo").matches(userAdmNo);
        amqpAdmin.declareBinding(binding);
        System.out.println("已建立專屬佇列");
    }

    public void notifyUploadProgress(String userAdmNo, int progress) {
        Map<String, Object> message = new HashMap<>();
        message.put("progress", progress);
        message.put("userAdmNo", userAdmNo);
        rabbitTemplate.convertAndSend(exchange.getName(), null, message);
    }
}
