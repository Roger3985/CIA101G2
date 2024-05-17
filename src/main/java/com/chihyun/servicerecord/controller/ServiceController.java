package com.chihyun.servicerecord.controller;

import com.chihyun.servicerecord.dto.ChatMessage;

import com.chihyun.servicerecord.dto.State;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{userName}")
public class ServiceController {

    private static Map<String, Session> sessionsMap = new ConcurrentHashMap<>();
    //private static final Set<Session> connectedSessions = Collections.synchronizedSet(new HashSet<>());
    Gson gson = new Gson();

    @OnOpen
    public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
        sessionsMap.put(userName, userSession);
        System.out.println("websocket連線成功");
        System.out.println("我在onopen取得userName: " + userName);
        System.out.println("我在onopen取得userSession: " + userSession);

        Set<String> userNames = sessionsMap.keySet();
        State stateMessage = new State("open", userName, userNames);
        System.out.println("我在onopen取得userNames: " + userNames);
        System.out.println("我在onopen取得stateMessage: " + stateMessage);

        String stateMessageJson = gson.toJson(stateMessage);
        System.out.println("我在onopen取得stateMessageJson: " + stateMessage);

        Collection<Session> sessions = sessionsMap.values();
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(stateMessageJson);
            }
        }
    }


    @OnMessage
    public void onMessage(Session userSession, String message) {
        System.out.println("我在onMessage: " + userSession);
        System.out.println("我在onMessage: " + message);
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        String sender = chatMessage.getSender();
        String receiver = chatMessage.getReceiver();
        System.out.println("我在onMessage sender: " + sender);
        System.out.println("我在onMessage receiver: " + receiver);
        Session receiverSession = sessionsMap.get(receiver);
        if (receiverSession != null && receiverSession.isOpen()) {
            // 向目標用戶發送消息
            receiverSession.getAsyncRemote().sendText(message);
        } else {
            // 目標用戶不存在或連接已關閉，可以選擇處理這種情況
        }
    }
}




