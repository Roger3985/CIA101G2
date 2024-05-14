package com.iting.test;

import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.impl.CartServiceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

public class CartServiceTest {

    public static void main(String[] args) {
        // Initialize Jedis connection
        Jedis jedis = new Jedis("localhost", 6379);

        // Assuming you have initialized jedisPool somewhere in your application
        JedisPool jedisPool = initializeJedisPool(); // Initialize your JedisPool
        CartServiceImpl cart=new CartServiceImpl();
        // Assuming you want to get cart items for member with memNo 11
        List<CartRedis> cartItems = cart.findByCompositeKey(3);

        // Print out the cart items
        for (CartRedis cartItem : cartItems) {
            System.out.println(cartItem);
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
