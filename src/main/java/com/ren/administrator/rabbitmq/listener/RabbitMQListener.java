package com.ren.administrator.rabbitmq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "queue_PartTime")
    public void listenPartTime(String message) {
        messagingTemplate.convertAndSend("/topic/parttime", message);
    }

    @RabbitListener(queues = "queue_FullTime")
    public void listenFullTime(String message) {
        messagingTemplate.convertAndSend("/topic/fulltime", message);
    }

    @RabbitListener(queues = "queue_Manager")
    public void listenManager(String message) {
        messagingTemplate.convertAndSend("/topic/manager", message);
    }

    @RabbitListener(queues = "queue_Boss")
    public void listenBoss(String message) {
        messagingTemplate.convertAndSend("/topic/boss", message);
    }
}
