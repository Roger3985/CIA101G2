package com.listener;

import com.ren.administrator.dto.LoginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InitializerListener implements ServletContextListener {

    // 重啟時載入線上人數資源用
    private static final String ONLINE_USERS_FILE = "onlineUsers.dat";

    // RedisTemplate，用來計算線上人數用
    @Autowired
    @Qualifier("admStrLogin")
    private RedisTemplate<String, LoginState> admRedisTemplate;

    /**
     * 當ServletContext啟動時，執行以下功能同步應用程式資料:
     * 1.同步在線人數(當應用程式因某些原因重啟時不會遺失資料)
     * (1)讀取上次應用程式關閉時統計的在線人數，存入ServletContext同步在線人數
     * (2)讀取Redis內儲存的在線人數資料，存入ServletContext同步在線人數
     *
     * @param sce Information about the ServletContext that was initialized
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 獲得ServletContext物件
        ServletContext context = sce.getServletContext();
        // 載入線上人數，存入ServletContext
        AtomicInteger onlineUsers = loadOnlineUsersCounter();
        context.setAttribute("onlineUsers", onlineUsers);
        // 統計放入Redis資料庫內的登入狀態數量(登入人數)，存入ServletContext
        AtomicInteger onlineAdms = new AtomicInteger(admRedisTemplate.keys("*").size());
        context.setAttribute("onlineAdms", onlineAdms);
    }

    /**
     * 當ServletContext關閉時，執行以下功能同步資料或關閉資源，或儲存檔案留作下次開始應用程式使用:
     * 1.儲存線上人數檔案，於下次開始時讀取在線人數而不會導致資料遺失
     *
     * @param sce Information about the ServletContext that was destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        // 將在線人數存入，當應用程式重啟時可讀取而不遺失資料
        AtomicInteger onlineUsers = (AtomicInteger) context.getAttribute("onlineUsers");
        saveOnlineUsersCounter(onlineUsers);
    }

    /**
     * 讀取上次關閉應用程式時儲存的資料
     *
     * @return 返回線上人數
     */
    private AtomicInteger loadOnlineUsersCounter() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ONLINE_USERS_FILE))) {
            return (AtomicInteger) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new AtomicInteger(0);
        }
    }

    /**
     * 當應用程式關閉時，存入線上人數資料
     *
     * @param onlineUsers 傳入線上人數，做儲存用
     */
    private void saveOnlineUsersCounter(AtomicInteger onlineUsers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ONLINE_USERS_FILE))) {
            oos.writeObject(onlineUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}