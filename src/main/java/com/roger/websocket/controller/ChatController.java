package com.roger.websocket.controller;

import com.roger.websocket.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    /**
     * 前往聊天室
     *
     * @return 前往 websocket.html 的試圖
     */
    @GetMapping("/websocket")
    public String websocket() {
        return "/frontend/notice/websocket";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        return chatMessage;
    }

    @MessageMapping("/chat.sendImage")
    @SendTo("/topic/public")
    public ChatMessage sendImage(@Payload ChatMessage chatMessage) {
        // 在這裡處理接收到的圖片數據，例如保存到文件系統或數據庫中

        // 返回接收到的消息
        return chatMessage;
    }


}
