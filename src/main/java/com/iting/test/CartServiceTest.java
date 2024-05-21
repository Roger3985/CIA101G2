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
        ProductMyFavoriteService service = new ProductMyFavoriteImpl();

        // 假设会员编号是 123
        Integer memNo = 123;

        // 调用 findByKey 方法获取收藏夹中的商品列表
        List<ProductMyFavoriteRedis> favorites = service.findByKey(memNo);

        // 打印收藏夹中的商品信息
        for (ProductMyFavoriteRedis favorite : favorites) {
            System.out.println("Product No: " + favorite.getProductNo() + ", FavTime: " + favorite.getFavTime());
        }
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
