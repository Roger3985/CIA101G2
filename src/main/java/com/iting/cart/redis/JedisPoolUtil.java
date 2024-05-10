package com.iting.cart.redis;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {

    private static final JedisPool jedisPool = createJedisPool();

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    // 沒有使用Double-checked locking(小吳老師的範例)的原因：
    //  JedisPool的創建交由監聽器管理,服務器(Tomcat)啟動時則創建
    //  在contextInitialized裡創建,可以保證不會出現多個執行序同時創建資源的狀況
    private static JedisPool createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 連線池中最大的連接數
        config.setMaxTotal(8);
        // 連線池中最大的待機連接數
        config.setMaxIdle(8);
        // 等待連接的最大時間(毫秒)
        //  超過等待時間的連接要求會失敗並拋出異常
        config.setMaxWaitMillis(10000);
        JedisPool jedisPool = new JedisPool(config,"localhost",6379);
        return jedisPool;
    }

    public static void shutdown() {
        jedisPool.destroy();
    }
}
