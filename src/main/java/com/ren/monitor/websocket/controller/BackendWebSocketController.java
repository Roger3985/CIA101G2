package com.ren.monitor.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class BackendWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 使用原子操作整數，用來確保線上人數的正確性
    private final AtomicInteger onlineUsers = new AtomicInteger(0);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        int currentUsers = onlineUsers.incrementAndGet();
        messagingTemplate.convertAndSend("/topic/onlineUsers", currentUsers);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        int currentUsers = onlineUsers.decrementAndGet();
        messagingTemplate.convertAndSend("/topic/onlineUsers", currentUsers);
    }
}
