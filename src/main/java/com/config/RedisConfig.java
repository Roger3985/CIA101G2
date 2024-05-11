package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.iting.cart.entity.CartRedis;
import com.ren.administrator.dto.LoginState;
import com.roger.member.dto.LoginStateMember;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 變更RedisTemplate默認配置
     * 序列化器默認為JdkSerializationRedisSerializer，序列化後會回傳byte[]
     * 1.將key的序列化器設置為Integer序列化器
     * 2.將value的序列化器設置為Json序列化器
     * 泛型設置為<Integer, LoginState>，用於根據管理員編號操作LoginState(CRUD)
     * Bean 名稱設計成integerLoginState (調用@Qualifier("integerLoginState")即可使用)
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回redisTemplate
     */
    @Bean("integerLoginState")
    public RedisTemplate<Integer, LoginState> integerLoginStateRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, LoginState> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

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
    @Bean("integerLoginStateMember")
    public RedisTemplate<Integer, LoginStateMember> integerLoginStateRedisTemplateMember(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, LoginStateMember> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置 Serializer
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * 變更RedisTemplate默認配置
     * 序列化器默認為JdkSerializationRedisSerializer，序列化後會回傳byte[]
     * 1.將key的序列化器設置為String序列化器
     * 2.將value的序列化器設置為Json序列化器
     * 泛型設置為<String, LoginState>，用於根據管理員編號操作LoginState(CRUD)
     * Bean 名稱設計成stringLoginState (調用@Qualifier("stringLoginState")即可使用)
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回redisTemplate
     */
    @Bean("stringLoginState")
    public RedisTemplate<String, LoginState> stringLoginStateRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LoginState> redisTemplate = new RedisTemplate<>();
        // 設置連線
        redisTemplate.setConnectionFactory(connectionFactory);
        // 設置Serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * Spring Data Redis預設處理字串的RedisTemplate，泛型為<String, String>
     * 如果沒有設置Bean仍可直接調用，這裡為專案練習設置
     *
     * @param connectionFactory 配置RedisConnectionFactory，與資料庫建立連線
     * @return 返回StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
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
    @Bean("stringInteger")
    public RedisTemplate<String, Integer> stringIntegerRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return redisTemplate;
    }

    @Bean("cart")
    public RedisTemplate<String, CartRedis> cartRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
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

}
