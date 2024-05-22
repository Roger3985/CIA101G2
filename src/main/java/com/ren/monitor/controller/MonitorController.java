package com.ren.monitor.controller;

import com.ren.administrator.dto.LoginState;
import com.ren.monitor.dto.Monitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class MonitorController {

    @Autowired
    @Qualifier("monitorIntMon")
    private RedisTemplate<Integer, Monitor> redisTemplate;

    /**
     * 獲取儲存在Redis內的歷史消息(更換頁面時會觸發saveMessages的方法將傳到前端的訊息儲存在Redis)
     *
     * @param session HTTP會話
     * @return 歷史消息列表
     */
    @GetMapping("/backend/getMessages")
    public List<Map<String, Object>> getMessages(HttpSession session) {
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        Integer userKey = loginState.getAdmNo();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(userKey);
        List<Map<String, Object>> messages = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Map<String, Object> messageWithId = new HashMap<>();
            messageWithId.put("id", entry.getKey());
            messageWithId.put("message", entry.getValue());
            messages.add(messageWithId);
        }
        System.out.println("歷史訊息Get!");
        return messages;
    }

    /**
     * 更新消息的已讀狀態
     *
     * @param session HTTP會話
     * @return 成功消息
     */
    @PostMapping("/backend/markAllAsRead")
    public Map<String, String> markAllAsRead(HttpSession session) {
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        if (loginState == null) {
            return Map.of("status", "fail");
        }
        Integer userKey = loginState.getAdmNo();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(userKey);
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Monitor monitor = (Monitor) entry.getValue();
            monitor.setRead(true);
            redisTemplate.opsForHash().put(userKey, entry.getKey(), monitor);
        }
        System.out.println("已讀不回");
        return Map.of("status", "success");
    }

    /**
     * 保存當前頁面的消息
     *
     * @param session HTTP會話
     * @param messages 消息列表
     * @return 成功消息
     */
    @PostMapping("/backend/saveMessages")
    public Map<String, String> saveMessages(HttpSession session, @RequestBody List<Monitor> messages) {
        System.out.println("saveMessages方法被調用");
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        if (loginState == null) {
            System.out.println("LoginState 為空");
            return Map.of("status", "fail");
        }
        Integer userKey = loginState.getAdmNo();
        for (Monitor message : messages) {
            String messageId = "message:" + UUID.randomUUID().toString();
            redisTemplate.opsForHash().put(userKey, messageId, message);
        }
        System.out.println("儲存成功");
        return Map.of("status", "success");
    }


}
