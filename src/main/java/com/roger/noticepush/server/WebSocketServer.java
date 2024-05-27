//package com.roger.noticepush.server;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import org.springframework.web.socket.server.standard.SpringConfigurator;
//
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//@Component
//@ServerEndpoint(value = "/websocket", configurator = SpringConfigurator.class)
//public class WebSocketServer extends TextWebSocketHandler {
//
//    /**
//     * 消息佇列，用於儲存待發送的消息。
//     */
//    private static final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
//
//    static {
//        // 模擬一些初始消息
//        messageQueue.add("歡迎你來到fallElove官網");
//        messageQueue.add("我們有購買男女式禮服，歡迎挑選");
//        messageQueue.add("也歡迎加入我們的會員，這樣就可以進行一些優惠跟福利喔!");
//    }
//
//    /**
//     * WebSocket 連接建立時觸發的方法。
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("首頁已被造訪");
//        // 發送初始消息
//        sendNextMessage(session);
//    }
//
//    /**
//     * 接收到瀏覽器端消息觸發的方法。
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("來自首頁的消息:" + message);
//        // 以下可以處理其他的業務邏輯
//    }
//
//    private void sendNextMessage(Session session) {
//        // 從消息佇列中取出下一條消息並發送給前台
//        String message = messageQueue.poll();
//        if (message != null) {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
