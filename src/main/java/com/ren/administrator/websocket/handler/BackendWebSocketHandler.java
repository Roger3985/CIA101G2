package com.ren.administrator.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class BackendWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 處理接收到的WebSocket消息
        String payload = message.getPayload();
        System.out.println("Received: " + payload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 當WebSocket連接建立後
        System.out.println("WebSocket connection established");
    }
}
