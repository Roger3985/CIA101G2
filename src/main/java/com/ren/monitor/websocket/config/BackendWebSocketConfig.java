package com.ren.monitor.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * 建立一個Stomp協議的WebSocket App
 */
@Configuration
@EnableWebSocketMessageBroker
public class BackendWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 註冊STOMP端點(/wsadmin)，並啟用SockJS後備選項，用於支援不支援WebSocket的瀏覽器
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/admin").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 定義所有發送往服務器的消息的前綴
        registry.setApplicationDestinationPrefixes("/company");
        // 啟用 STOMP 代理中繼，並配置 RabbitMQ
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }

    /**
     * 配置容量大小
     *
     * @param registration 傳入相關註冊物件
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(300 * 1024); // Set to 300 KB or any size you need
        registration.setSendBufferSizeLimit(300 * 1024);
        registration.setSendTimeLimit(20 * 10000);
    }
}
