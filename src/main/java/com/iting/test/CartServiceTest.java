package com.iting.test;

import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.impl.CartServiceImpl;

import com.iting.productmyfavorite.entity.ProductMyFavoriteRedis;
import com.iting.productmyfavorite.service.ProductMyFavoriteService;
import com.iting.productmyfavorite.service.impl.ProductMyFavoriteImpl;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

public class CartServiceTest {

    public static void main(String[] args) {
        ProductMyFavoriteImpl productMyFavoriteService = new ProductMyFavoriteImpl();

        // 创建 ProductMyFavoriteRedis 对象
        ProductMyFavoriteRedis productMyFavoriteRedis = new ProductMyFavoriteRedis();
        productMyFavoriteRedis.setMemNo(123); // 设置会员编号
        productMyFavoriteRedis.setProductNo(333); // 设置商品编号

        // 调用 addProductMyFavorite 方法
        productMyFavoriteService.addProductMyFavorite(productMyFavoriteRedis);

    }


    // Method to initialize and return the JedisPool
    private static JedisPool initializeJedisPool() {
        // Initialize JedisPoolConfig
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Set appropriate configuration options if needed
        // For example: poolConfig.setMaxTotal(10);

        // Create JedisPool
        return new JedisPool(poolConfig, "localhost", 6379);
    }
}
