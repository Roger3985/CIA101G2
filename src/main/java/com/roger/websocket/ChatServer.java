//package com.roger.websocket;
//
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//
//@Slf4j
//@ServerEndpoint("/websocket")
//@Component
//public class ChatServer {
//
//    @OnOpen
//    public void onOpen(){
//        log.info("---------------------------->on Open");
//    }
//
//    @OnClose
//    public void OnClose(){
//        log.info("---------------------------->on Close");
//    }
//
//    @OnMessage
//    @SneakyThrows
//    public void onMessage(String message, Session session){
//        log.info("------------------->message:{}",message);
//        for (Session se:session.getOpenSessions()){
//            //把消息转发到其他用户
//            se.getBasicRemote().sendText(message);
//        }
//    }
//}