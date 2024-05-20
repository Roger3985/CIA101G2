package com.chihyun.servicerecord.controller;

import com.chihyun.servicerecord.config.SpringContext;
import com.chihyun.servicerecord.dto.ChatMessage;

import com.chihyun.servicerecord.dto.State;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    Gson gson = new Gson();

    private static StringRedisTemplate staticChatRedisTemplate;
    private static RedisTemplate staticChatMessageRedisTemplate;

    @Autowired
    @Qualifier("chatStrStr")
    private StringRedisTemplate chatRedisTemplate;

    @Autowired
    @Qualifier("chatMemStrMsg")
    private RedisTemplate chatMessageRedisTemplate;

    @PostConstruct
    public void init() {
        staticChatRedisTemplate = this.chatRedisTemplate;
        staticChatMessageRedisTemplate = this.chatMessageRedisTemplate;
    }


    @OnOpen
    public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
        sessionsMap.put(userName, userSession);

        System.out.println(staticChatRedisTemplate);

        System.out.println("websocket連線成功");

        Set<String> userNames = sessionsMap.keySet();
        State stateMessage = new State("stateA", userName, userNames);

        String stateMessageJson = gson.toJson(stateMessage);

        Collection<Session> sessions = sessionsMap.values();
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(stateMessageJson);
            }
        }

    }


    @OnMessage
    public void onMessage(Session userSession, String message) {

        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        String type = chatMessage.getType();
        String sender = chatMessage.getSender();
        String receiver = chatMessage.getReceiver();
        String msg = chatMessage.getMessage();
        System.out.println("我在onMessage sender: " + sender);
        System.out.println("我在onMessage receiver: " + receiver);
        String senderkey = new StringBuilder(sender).append(":").append(receiver).toString();
        String receiverkey = new StringBuilder(receiver).append(":").append(sender).toString();
        Session receiverSession = sessionsMap.get(receiver);
        //      如果sender:receiver有存在資料庫的key則顯示歷史訊息
        if (type.equals("history")) {
            List<String> historyData = getHistoryMsg(sender, receiver);
            String historyMsg = gson.toJson(historyData);
            System.out.println(historyMsg);
            ChatMessage cmHistory = new ChatMessage("history", sender, receiver, historyMsg);
            System.out.println(cmHistory);
            if (userSession != null && userSession.isOpen()) {
                System.out.println("我準備要送資料囉");
                userSession.getAsyncRemote().sendText(gson.toJson(cmHistory));
                System.out.println("歷史資料送給前端囉");
            }
        } else if (type.equals("chatMsgB")) {

            if (receiverSession != null && receiverSession.isOpen()) {

                // 向目標用戶發送消息
                receiverSession.getAsyncRemote().sendText(message);


                if (!sender.isEmpty() && !msg.isEmpty()) {
                    saveMessage(senderkey, message);
                    saveMessage(receiverkey, message);
                    staticChatMessageRedisTemplate.opsForList().rightPush(senderkey, chatMessage);
                    staticChatMessageRedisTemplate.opsForList().rightPush(receiverkey, chatMessage);
                    System.out.println("我有存哦");
                }
            }
        }

    }


    private void saveMessage(String key, String message) {
        staticChatRedisTemplate.opsForList().rightPush(key, message);
    }

    private List<String> getHistoryMsg(String sender, String receiver) {
        String getKey = new StringBuilder(sender).append(":").append(receiver).toString();
        List<String> historyData = staticChatRedisTemplate.opsForList().range(getKey, 0, -1);
        return historyData;
    }


}




