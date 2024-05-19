//package com.ren.websocket;
//
//import com.roger.websocket.model.ChatMessage;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class BackendWebSocketController {
//
//    @GetMapping("/webTest")
//    public String webSocketPage() {
//        return "backend/webTest";
//    }
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/jobHint/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/jobHint/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.sendImage")
//    @SendTo("/jobHint/public")
//    public ChatMessage sendImage(@Payload ChatMessage chatMessage) {
//        // 在這裡處理接收到的圖片數據，例如保存到文件系統或數據庫中
//
//        // 返回接收到的消息
//        return chatMessage;
//    }
//}
