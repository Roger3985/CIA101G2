//package com.roger.websocket.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.roger.websocket.model.ChatMessage;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//public class ChatController {
//
//    /**
//     * 前往聊天室
//     *
//     * @return 前往 websocket.html 的試圖
//     */
//    @GetMapping("/websocket")
//    public String websocket() {
//        return "/frontend/notice/websocket";
//    }
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
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
//    @SendTo("/topic/public")
//    public ChatMessage sendImage(@Payload ChatMessage chatMessage) {
//
//        // 在你的類中定義一個 ObjectMapper 實例
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        if (chatMessage.getType() == ChatMessage.MessageType.IMAGE_AND_CHAT) {
//            try {
//                // 將 content 字符串轉換為 Map 物件
//                Map<String, Object> contentMap = objectMapper.readValue(chatMessage.getContent(), Map.class);
//
//                // 從 Map 對象中獲取文字和圖片資訊
//                String text = (String) contentMap.get("text");
//                String image = (String) contentMap.get("image");
//
//                // 創建一個包含文字和圖片資訊的 Map 物件並設置到 chatMessage.content 中
//                Map<String, String> messageContent = new HashMap<>();
//                messageContent.put("text", text);
//                messageContent.put("image", image);
//                chatMessage.setContent(String.valueOf(messageContent));
//            } catch (IOException e) {
//                // 處理 JSON 解析異常
//                e.printStackTrace();
//            }
//        }
//
//        if (chatMessage.getType() == ChatMessage.MessageType.IMAGE) {
//            // 在這裡處理接收到的圖片數據，例如保存到文件系統或數據庫中
//        }
//
//        // 返回接收到的消息
//        return chatMessage;
//    }
//}
