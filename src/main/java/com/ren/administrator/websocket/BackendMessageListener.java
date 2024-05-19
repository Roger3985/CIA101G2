//package com.ren.administrator.websocket;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class BackendMessageListener {
//
//    private final List<WebSocketSession> sessions = new ArrayList<>();
//
//    public void addSession(WebSocketSession session) {
//        sessions.add(session);
//    }
//
//    public void removeSession(WebSocketSession session) {
//        sessions.remove(session);
//    }
//
//    @RabbitListener(queues = "myQueue")
//    public void receiveMessage(String message) {
//        for (WebSocketSession session : sessions) {
//            try {
//                session.sendMessage(new TextMessage(message));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
