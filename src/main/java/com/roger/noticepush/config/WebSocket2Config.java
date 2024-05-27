//package com.roger.noticepush.config;
//
//import com.roger.noticepush.server.WebSocketServer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
///**
// * WebSocket 配置類
// */
//@Configuration
//@EnableWebSocket
//public class WebSocket2Config implements WebSocketConfigurer {
//
//    @Autowired
//    private WebSocketServer webSocketServer;
//
//
//    /**
//     * 註冊 WebSocket 處理器和路徑映射
//     * @param registry
//     */
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(webSocketServer, "/websocket").setAllowedOrigins("*");
//    }
//
//}
