package com.ren.monitor.rabbitmq.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ隊列的監聽器，用於執行當消息被放入到隊列時執行相對應的方法，
 * 這個監聽器用於當消息被放入隊列內時利於WebSocket將其傳送到使用者客戶端
 */
@Service
public class RabbitMQListener {

    /**
     * WebSocket使用傳送訊息的Template
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 用於監聽打工仔的隊列，當消息被放入時會執行監聽器內的方法
     *
     * @param message queue內的訊息，傳入方法內將其傳給客戶端
     */
    @RabbitListener(queues = "queue_Employee")
    public void listenEmployee(String message) {
        messagingTemplate.convertAndSend("/topic/employee", message);
    }

    /**
     * 用於監聽全職員工的隊列，當消息被放入時會執行監聽器內的方法
     *
     * @param message queue內的訊息，傳入方法內將其傳給客戶端
     */
    @RabbitListener(queues = "queue_FullTime")
    public void listenFullTime(String message) {
        messagingTemplate.convertAndSend("/topic/fullTime", message);
    }

    /**
     * 用於監聽經理的隊列，當消息被放入時會執行監聽器內的方法
     *
     * @param message queue內的訊息，傳入方法內將其傳給客戶端
     */
    @RabbitListener(queues = "queue_Manager")
    public void listenManager(String message) {
        messagingTemplate.convertAndSend("/topic/manager", message);
    }

    /**
     * 用於監聽老闆的隊列，當消息被放入時會執行監聽器內的方法
     *
     * @param message queue內的訊息，傳入方法內將其傳給客戶端
     */
    @RabbitListener(queues = "queue_Boss")
    public void listenBoss(String message) {
        messagingTemplate.convertAndSend("/topic/boss", message);
    }

    @RabbitListener(queues = "#{dynamicQueueService.queueNames}")
    public void listenDynamicQueue(String message) {
        JsonNode messageObject = new ObjectMapper().readTree(message);
        String userAdmNo = messageObject.get("userAdmNo").asText();
        messagingTemplate.convertAndSend("/queue/user-" + userAdmNo, message);
    }
}
