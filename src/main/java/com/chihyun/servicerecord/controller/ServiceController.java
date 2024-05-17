package com.chihyun.servicerecord.controller;

import com.chihyun.servicerecord.dto.ChatMessage;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{userName}")
public class ServiceController {

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    //private static final Set<Session> connectedSessions = Collections.synchronizedSet(new HashSet<>());
    Gson gson = new Gson();

    @OnOpen
    public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
        System.out.println("websocket連線成功");
        System.out.println("我在onopen取得userName: " + userName);
        sessionMap.put(userName, userSession);
        Set<String> userNames = sessionMap.keySet();
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        System.out.println("我在onMessage: " + userSession);
        System.out.println("我在onMessage: " + message);
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        String sender = chatMessage.getSender();
//        String receiver = chatMessage.getReceiver();
        System.out.println("我在onMessage: " + sender);
//        System.out.println(receiver);

    }


}


