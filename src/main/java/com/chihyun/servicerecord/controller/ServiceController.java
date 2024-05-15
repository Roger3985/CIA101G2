package com.chihyun.servicerecord.controller;

import com.chihyun.servicerecord.entity.ChatMessage;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{userName}")
public class ServiceController {

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    Gson gson = new Gson();

    @OnOpen
    public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
        sessionMap.put(userName, userSession);
        System.out.println("websocket連線成功");
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        System.out.println(message);
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        String sender = chatMessage.getSender();
        String receiver = chatMessage.getReceiver();
    }

}
