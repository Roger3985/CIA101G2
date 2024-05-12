
//
//import com.iting.cart.dao.CartRepository;
//import com.iting.cart.entity.Cart;
//import com.iting.cart.service.CartService;
//import com.iting.productorderdetail.entity.ProductOrderDetail;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//@Service("CartService")
//public class CartServiceImpl implements CartService {
//
//@Autowired
//CartRepository repository;
//
//
//
//    @Override
//    public void addCart(Cart cart){
//
//        repository.save(cart);
//    }
//    @Override
//    public void updateCart(Cart cart) {
//        // 查询数据库中的订单详情对象
//        List<Cart> existingCarts = repository.findByCompositeKeyProductNoAndCompositeKeyMemNo(
//                cart.getCompositeKey().getProductNo(),
//                cart.getCompositeKey().getMemNo()
//        );
//
//        if (!existingCarts.isEmpty()) {
//            // 获取第一个购物车条目进行数量相加
//            Cart existingCart = existingCarts.get(0);
//            int updatedQuantity = existingCart.getProductBuyQty() + cart.getProductBuyQty();
//            existingCart.setProductBuyQty(updatedQuantity);
//            repository.save(existingCart); // 更新购物车条目
//        } else {
//            // 如果不存在相同编号的产品，则直接保存新的购物车条目
//            repository.save(cart);
//        }
//    }

//    @Override
//    public void deleteBymemNoAndProductNo(Integer productNo,Integer memNo){
//        repository.deleteByCompositeKey_MemNoAndproductNo(productNo, memNo);
//    }
//
//
//
//
//    @Override
//    public List<Cart> findByCompositeKeyProductNoAndCompositeKeyMemNo(Integer productNo, Integer memNo) {
//        return repository.findByCompositeKeyProductNoAndCompositeKeyMemNo(productNo, memNo);
//    }
//    @Override
//    public void deleteBymemNo(Integer memNo) {
//        repository.deleteByCompositeKey_MemNo(memNo);
//    }
//
//
//    @Override
//    public List<Cart> findByCompositeKey(Integer memNo){
//        return repository.findByCompositeKey(memNo);
//    }
//}
package com.iting.cart.service.impl;

import com.iting.cart.dao.CartRepository;
import com.iting.cart.entity.CartRedis;
import com.iting.cart.redis.JedisPoolUtil;
import com.iting.cart.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.math.BigDecimal;

@Service("CartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository repository;

    private final JedisPool jedisPool = JedisPoolUtil.getJedisPool();
    @Override
    public void addCart(CartRedis cart) {
        try (Jedis jedis = jedisPool.getResource()) {
            String cartKey = "cart:" + cart.getMemNo();
            String productKey = cart.generateId(cart.getProductNo(), cart.getMemNo());
            jedis.hset(productKey, "memNo", cart.getMemNo().toString());
            jedis.hset(productKey, "productNo", cart.getProductNo().toString());
            jedis.hset(productKey, "productName", cart.getProductName());
            jedis.hset(productKey, "productSize", cart.getProductSize().toString());
            jedis.hset(productKey, "productColor", cart.getProductColor());
            jedis.hset(productKey, "productPrice", cart.getProductPrice().toString());
            jedis.hset(productKey, "productBuyQty", cart.getProductBuyQty().toString());

            jedis.sadd(cartKey, productKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCart(CartRedis cartRedis) {
        try (Jedis jedis = jedisPool.getResource()) {
            String productKey = cartRedis.generateId(cartRedis.getProductNo(), cartRedis.getMemNo());

            if (!jedis.exists(productKey)) {
                addCart(cartRedis);
            } else {
                String currentQuantityStr = jedis.hget(productKey, "productBuyQty");
                int currentQuantity = currentQuantityStr != null ? Integer.parseInt(currentQuantityStr) : 0;

                int newQuantity = currentQuantity + cartRedis.getProductBuyQty();
                jedis.hset(productKey, "productBuyQty", String.valueOf(newQuantity));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


}
//    @Override
//    public void deleteBymemNo(Integer memNo) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            // 删除整个用户的购物车，需要知道用户的 id
//            String idPrefix = generateIdPrefix(memNo);
//            Set<String> keys = jedis.keys(idPrefix + "*");
//            for (String key : keys) {
//                jedis.del(key);
//            }
//        }
//    }
//
@Override
public void deleteBymemNoAndProductNo(Integer memNo, Integer productNo) {

    try (Jedis jedis = jedisPool.getResource()) {
        // 生成 memNo 和 productNo 鍵
        String productKey = "cart:" + memNo + ":" + productNo;
        // 從購物車中刪除商品資訊

        jedis.del(productKey);

    } catch (Exception e) {

        e.printStackTrace();
    }
}


    @Override
    public List<CartRedis> findByCompositeKey(Integer memNo) {
        List<CartRedis> cartItems = new ArrayList<>();

        try (Jedis jedis = jedisPool.getResource()) {
            String cartKey = "cart:" + memNo; // 使用传入的会员编号构建购物车键

            Set<String> productKeys = jedis.smembers(cartKey);

            for (String productKey : productKeys) {
                Map<String, String> productDetails = jedis.hgetAll(productKey);
                CartRedis cartItem = new CartRedis();

                cartItem.setMemNo(memNo); // 使用传入的会员编号
                cartItem.setProductNo(Integer.parseInt(productDetails.get("productNo"))); // 获取商品编号
                cartItem.setProductName(productDetails.get("productName")); // 获取商品名称
                cartItem.setProductSize(Integer.parseInt(productDetails.get("productSize"))); // 获取商品尺寸
                cartItem.setProductColor(productDetails.get("productColor")); // 获取商品颜色
                cartItem.setProductPrice(new BigDecimal(productDetails.get("productPrice"))); // 获取商品价格
                cartItem.setProductBuyQty(Integer.parseInt(productDetails.get("productBuyQty"))); // 获取购买数量
                cartItems.add(cartItem);
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }

        return cartItems;
    }

//    @Override
//    public void createOrderFromCart(Integer memNo) {
//        // Retrieve cart items from Redis
//        List<CartRedis> cartItems = findByCompositeKey(memNo);
//        // Iterate over cart items and save them as order details
//        for (CartRedis cartItem : cartItems) {
//            // Create a composite key for the order detail
//            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
//            compositeKey.setProductOrdNo(compositeKey.getProductOrdNo()); // Set the order number (assuming you have it)
//            compositeKey.setProductNo(cartItem.getProductNo());
//
//            // Create a new ProductOrderDetail object
//            ProductOrderDetail orderDetail = new ProductOrderDetail();
//            orderDetail.setCompositeKey(compositeKey);
//            orderDetail.setProductPrice(cartItem.getProductPrice());
//            orderDetail.setProductOrdQty(cartItem.getProductBuyQty());
//
//            repository.save(orderDetail);
//        }
//    }



        private String generateProductKey(Integer productNo) {
            return "product:" + productNo;
        }

}
//    }
//
//
//}
