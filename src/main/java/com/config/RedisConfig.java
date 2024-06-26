package com.config;

import com.chihyun.servicerecord.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.iting.cart.entity.CartRedis;
import com.ren.administrator.dto.LoginState;
import com.ren.monitor.dto.Monitor;
import com.ren.product.dto.ProductDTO;
import com.roger.member.dto.LoginStateMember;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
public class RedisConfig {

    /**
     * 根據管理員編號(Integer)操作LoginState(CRUD)
     *
     * 變更RedisTemplate默認配置
     * 序列化器默認為JdkSerializationRedisSerializer，序列化後會回傳byte[]
     * 1.將key的序列化器設置為Integer序列化器
     * 2.將value的序列化器設置為Json序列化器
     * 泛型設置為<Integer, LoginState>，用於根據管理員編號操作LoginState(CRUD)
     * Bean 名稱設計成admIntLogin (調用@Qualifier("admIntLogin")即可使用)
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回redisTemplate
     */
    @Bean("admIntLogin")
    public RedisTemplate<Integer, LoginState> admIntLoginRedisTemplate(
            @Qualifier("admDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, LoginState> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean("proIntDTO")
    public RedisTemplate<Integer, ProductDTO> proIntDTORedisTemplate(
            @Qualifier("proDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, ProductDTO> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * 用來存放後台管理員監控消息，透過管理員編號獲取他的消息列表
     * 資料結構: 每個管理員一個Map，用來存放監控消息
     * Integer admNo(假設為1號管理員): {hashKey(假設為10分鐘前 + message), message},
     *                              {hashKey(假設為3分鐘前 + message), message},
     *                              {hashKey(假設為目前時間 + 當前訊息), message},
     *
     * @param connectionFactory 配置Redis連線
     * @return 返回泛型為<Integer, Monitor>的RedisTemplate，方便後續操作不用轉型
     */
    @Bean("monitorIntMon")
    public RedisTemplate<Integer, Monitor> monitorRedisTemplate(
            @Qualifier("monitorDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, Monitor> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // HashKey設計為 當前時間 + 對話內容，使用上只有要get全部消息，用來當前端換頁時可以從資料庫提取更新
        // 因此用作區別內容(只要HashKey不重複就好)
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 預計存入Monitor DTO
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * 變更 RedisTemplate 默認配置
     * 序列化器默認為 JdkSerializationRedisSerializer，序列化後會回傳 byte[]
     * 1. 將 key 的序列化器設置為 Integer 序列化器
     * 2. 將 value 的序列化器設置為 Json 序列化器
     * 泛型設置為 <Integer, LoginStateMember>，用於根據會員編號操作 LoginStateMember(CRUD)
     * Bean 名稱設計成 integerLoginState (調用 @Qualifier("integerLoginState") 即可使用)
     *
     * @param connectionFactory 配置 RedisConnectionFactory，與資料庫建立連線
     * @return 返回 redisTemplate
     */
    @Bean("memIntLogin")
    public RedisTemplate<Integer, LoginStateMember> integerLoginStateRedisTemplateMember(
            @Qualifier("memDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, LoginStateMember> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置 Serializer
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean("chatMemStrMsg")
    public RedisTemplate<String, ChatMessage> chatMemStrMsgRedisTemplate(
            @Qualifier("chatMemDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ChatMessage> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean("proStrDTO")
    public RedisTemplate<String, ProductDTO> proStrDTORedisTemplate(
            @Qualifier("proDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ProductDTO> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * 根據管理員編號(String)操作LoginState
     *
     * 變更RedisTemplate默認配置
     * 序列化器默認為JdkSerializationRedisSerializer，序列化後會回傳byte[]
     * 1.將key的序列化器設置為String序列化器
     * 2.將value的序列化器設置為Json序列化器
     * 泛型設置為<String, LoginState>，用於根據管理員編號操作LoginState(CRUD)
     * Bean 名稱設計成admStrLogin (調用@Qualifier("admStrLogin")即可使用)
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回redisTemplate
     */
    @Bean("admStrLogin")
    public RedisTemplate<String, LoginState> admStrLoginRedisTemplate(
            @Qualifier("admDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LoginState> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * 用於獲取Cookie
     *
     * Spring Data Redis預設處理字串的RedisTemplate，泛型為<String, String>
     * 如果沒有設置Bean仍可直接調用，這裡為專案練習設置
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回StringRedisTemplate
     */
    @Bean("cookieStrStr")
    public StringRedisTemplate admStrRedisTemplate(
            @Qualifier("cookieDataBase") RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean("memStrStr")
    public StringRedisTemplate memStrRedisTemplate(
            @Qualifier("memDataBase") RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean("proStrStr")
    public StringRedisTemplate proStrRedisTemplate(
            @Qualifier("proDataBase") RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean("colStrStr")
    public StringRedisTemplate colStrRedisTemplate(
            @Qualifier("colDataBase") RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean("chatStrStr")
    public StringRedisTemplate chatStrRedisTemplate(
            @Qualifier("chatDataBase") RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    /**
     * 透過CookieValue(String)來將存在Redis資料庫內的存有使用者編號的資料取出
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回StringRedisTemplate
     */
    @Bean("cookieStrInt")
    public RedisTemplate<String, Integer> admStrIntRedisTemplate(
            @Qualifier("cookieDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return redisTemplate;
    }

    @Bean("failIP")
    public RedisTemplate<String, Integer> failIPRedisTemplate(
            @Qualifier("failIPDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return redisTemplate;
    }

    /**
     * 透過CookieValue(String)來將存在Redis資料庫內的存有使用者編號的資料取出
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回StringRedisTemplate
     */
    @Bean("memStrInt")
    public RedisTemplate<String, Integer> memStrIntRedisTemplate(
            @Qualifier("memDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return redisTemplate;
    }

    @Bean("cart")
    public RedisTemplate<String, CartRedis> cartRedisTemplate(
            @Qualifier("cartDataBase") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, CartRedis> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    @Bean("cartList")
    public RedisTemplate<String, Map<String, String>> cartListRedisTemplate(
            @Qualifier("cartDataBase") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Map<String, String>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer()); // 使用StringRedisSerializer来序列化内部的Map

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 功能：存放租借品中，已加入的願望品項之清單
     *
     * 產生一個叫 "rentalWish" 的 Spring Bean。
     * 返回的類型宣告為 RedisTemplate<String, Map<String, String>>
     * "rentalDataBase" 為指定連接 Redis 的倉庫
     * "new RedisTemplate" 創建一個 RedisTemplate 實例。用於與 Redis 交互
     *
     * @return 返回配置好的 RedisTemplate
     */
    @Bean("rentalWish")
    public RedisTemplate<String, Map<String, String>> rentalWishRedisTemplate(
            @Qualifier("rentalDataBase") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Map<String, String>> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置 鍵的序列化器 StringRedisSerializer ([鍵] 會被序列化為字符串)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 設置 值的序列化器 Jackson2JsonRedisSerializer，指定類型Map<String, String> ([值] 會被序列化為JSON格式的Map)
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Map.class));
        return redisTemplate;
    }




//    @Bean("rentalWish")
//    public RedisTemplate<Integer, Map<String, String>> rentalWishRedisTemplate(
//            @Qualifier("rentalDataBase") RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Integer, Map<String, String>> redisTemplate = new RedisTemplate<>();
//        // 設置連線
//        redisTemplate.setConnectionFactory(connectionFactory);
//        // 設置Serializer
//        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return redisTemplate;
//    }


//    @Bean("loginState")
//    public JedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        // 設置Redis服務主機與端口
//        jedisConnectionFactory.setHostName("localhost");
//        jedisConnectionFactory.setPort(6379);
//        // 設置連接的數據庫索引
//        jedisConnectionFactory.setDatabase(1);
//        return jedisConnectionFactory;
//    }

    /**
     * 用來存放LoginState
     *
     * 顯示寫入Lettuce連線工廠，RedisTemplate預設就是使用Lettuce客戶端的連線工廠，
     * 此Bean主要是用來設定存入的資料庫索引。
     * 補充:也可以使用Jedis客戶端連線，但因此專案的pom檔依賴引入為4.3.0版的Jedis，
     * 與此Spring Data Redis 2.7.0衝突，無法註冊連線，所以不使用
     * 用於存放管理員連線資料，索引1
     * 
     * @return 返回連線工廠
     */
    @Bean("admDataBase")
    public LettuceConnectionFactory loginStateRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(1);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    @Bean("proDataBase")
    public LettuceConnectionFactory productRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(4);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    @Bean("memDataBase")
    public LettuceConnectionFactory memberRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(0);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    @Bean("colDataBase")
    public LettuceConnectionFactory columnRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(7);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    @Bean("cartDataBase")
    public LettuceConnectionFactory cartRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(6);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    @Bean("chatDataBase")
    public LettuceConnectionFactory chatRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(8);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    // 用於存放自動登入相關資料
    @Bean("chatMemDataBase")
    public LettuceConnectionFactory chatMemRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(9);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    // 用於存放登入失敗的IP相關資料
    @Bean("failIPDataBase")
    public LettuceConnectionFactory failTimesRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(14);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    // 用於存放自動登入相關資料
    @Bean("cookieDataBase")
    public LettuceConnectionFactory autoLoginRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(15);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    //用於存放租借品相關資料
    @Bean("rentalDataBase")
    public LettuceConnectionFactory rentalRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(3);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    //用於存放後台推播消息
    @Bean("monitorDataBase")
    public LettuceConnectionFactory monitorConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setDatabase(2);  // 設定使用的 Redis database 索引
        return new LettuceConnectionFactory(config);
    }

    /**
     * 給預設RedisTemplate使用的連線，加上Primary優先使用這個bean
     *
     * @return 返回Lettuce預設連線
     */
    @Bean
    @Primary
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

}
