package com.ren.monitor.rabbitmq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ren.product.service.impl.ProductServiceImpl;
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

    @Autowired
    private ProductServiceImpl productSvc;

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

    @RabbitListener(queues = "queue_Job")
    public void listenJob(String message) {
        System.out.println("job執行~");
        messagingTemplate.convertAndSend("/topic/job", message);
    }

    @RabbitListener(queues = "#{rabbitService.queueNames}")
    public void listenDynamicQueue(String message) throws JsonProcessingException {
        System.out.println("在自定義佇列傳送消息~");
        JsonNode messageObject = new ObjectMapper().readTree(message);
        String userAdmNo = messageObject.get("userAdmNo").asText();
        messagingTemplate.convertAndSend("/queue/user-" + userAdmNo, message);
    }

    @RabbitListener(queues = "queue_Redis")
    public void listenDataBase(String message) throws JsonProcessingException {
        productSvc.redisDataStored();
        System.out.println("Redis已完成同步更新");
    }
}
