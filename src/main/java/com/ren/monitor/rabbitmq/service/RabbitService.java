package com.ren.monitor.rabbitmq.service;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitService {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    public void createQueueForUser(String userAdmNo) {
        String queueName = "userQueue_" + userAdmNo;
        Queue queue = new Queue(queueName, true, false, true);
        amqpAdmin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue).to(new HeadersExchange(exchange)).where("userAdmNo").matches(userAdmNo);
        amqpAdmin.declareBinding(binding);
    }

    public void notifyUploadProgress(String userAdmNo, int progress) {
        Map<String, Object> message = new HashMap<>();
        message.put("progress", progress);
        message.put("userAdmNo", userAdmNo);
        rabbitTemplate.convertAndSend(exchange, null, message);
    }
}
