//
////
////import com.iting.cart.dao.CartRepository;
////import com.iting.cart.entity.Cart;
////import com.iting.cart.service.CartService;
////import com.iting.productorderdetail.entity.ProductOrderDetail;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.data.redis.core.RedisTemplate;
////import org.springframework.stereotype.Service;
////
////import java.util.List;
////@Service("CartService")
////public class CartServiceImpl implements CartService {
////
////@Autowired
////CartRepository repository;
////
////
////
////    @Override
////    public void addCart(Cart cart){
////
////        repository.save(cart);
////    }
////    @Override
////    public void updateCart(Cart cart) {
////        // 查询数据库中的订单详情对象
////        List<Cart> existingCarts = repository.findByCompositeKeyProductNoAndCompositeKeyMemNo(
////                cart.getCompositeKey().getProductNo(),
////                cart.getCompositeKey().getMemNo()
////        );
////
////        if (!existingCarts.isEmpty()) {
////            // 获取第一个购物车条目进行数量相加
////            Cart existingCart = existingCarts.get(0);
////            int updatedQuantity = existingCart.getProductBuyQty() + cart.getProductBuyQty();
////            existingCart.setProductBuyQty(updatedQuantity);
////            repository.save(existingCart); // 更新购物车条目
////        } else {
////            // 如果不存在相同编号的产品，则直接保存新的购物车条目
////            repository.save(cart);
////        }
////    }
//
////    @Override
////    public void deleteBymemNoAndProductNo(Integer productNo,Integer memNo){
////        repository.deleteByCompositeKey_MemNoAndproductNo(productNo, memNo);
////    }
////
////
////
////
////    @Override
////    public List<Cart> findByCompositeKeyProductNoAndCompositeKeyMemNo(Integer productNo, Integer memNo) {
////        return repository.findByCompositeKeyProductNoAndCompositeKeyMemNo(productNo, memNo);
////    }
////    @Override
////    public void deleteBymemNo(Integer memNo) {
////        repository.deleteByCompositeKey_MemNo(memNo);
////    }
////
////
////    @Override
////    public List<Cart> findByCompositeKey(Integer memNo){
////        return repository.findByCompositeKey(memNo);
////    }
////}
//package com.iting.cart.service.impl;
//
//import com.iting.cart.entity.CartRedis;
//import com.iting.cart.service.CartService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service("CartService")
//public class CartServiceImpl3 implements CartService {
//
//    public Map<String, String> convertCartRedisToMap(CartRedis cart) {
//        Map<String, String> map = new HashMap<>();
//        map.put("memNo", cart.getMemNo().toString());
//        map.put("productNo", cart.getProductNo().toString());
//      map.put( "productName", cart.getProductName());
//      map.put( "productSize", cart.getProductSize().toString());
//      map.put( "productColor", cart.getProductColor());
//      map.put( "productPrice", cart.getProductPrice().toString());
//      map.put( "productBuyQty", cart.getProductBuyQty().toString());
//        // 其他屬性類似地放入 map 中
//        return map;
//    }
//
//    @Autowired
//    @Qualifier("cartList")
//    private RedisTemplate<String, Map<String, String>> cartListRedisRedisTemplate;
//
//    @Override
//    public void addCart(Integer memNo, CartRedis cart) {
//        // 直接将购物车数据存储到Redis，以会员编号作为键
//        cartListRedisRedisTemplate.opsForHash().putAll("cart:" + memNo, convertCartRedisToMap(cart));
//    }
//
//
//
//
//    @Override
//    public void updateCart(Integer memNo, CartRedis cart) {
//        // 将 CartRedis 对象转换为 Map<String, String>
//        Map<String, String> cartData = convertCartRedisToMap(cart);
//
//        // 检查 Redis 中是否已经存在购物车数据
//        String cartKey = "cart:" + memNo + ":" + cart.getProductNo();
//        if (!cartListRedisRedisTemplate.hasKey(cartKey)) {
//            // 如果不存在，直接将购物车数据存入 Redis 中
//            cartListRedisRedisTemplate.opsForHash().putAll(cartKey, cartData);
//        } else {
//            // 如果已存在，更新购买数量
//            String currentQuantityStr = (String) cartListRedisRedisTemplate.opsForHash().get(cartKey, "productBuyQty");
//            int currentQuantity = currentQuantityStr != null ? Integer.parseInt(currentQuantityStr) : 0;
//
//            int newQuantity = currentQuantity + Integer.parseInt(cartData.get("productBuyQty"));
//            cartListRedisRedisTemplate.opsForHash().put(cartKey, "productBuyQty", String.valueOf(newQuantity));
//        }
//    }
//
//
//    //
////
////    //    @Override
//////    public void deleteBymemNo(Integer memNo) {
//////        try (Jedis jedis = jedisPool.getResource()) {
//////            // 删除整个用户的购物车，需要知道用户的 id
//////            String idPrefix = generateIdPrefix(memNo);
//////            Set<String> keys = jedis.keys(idPrefix + "*");
//////            for (String key : keys) {
//////                jedis.del(key);
//////            }
//////        }
//////    }
//////
////    @Override
////    public void deleteBymemNoAndProductNo(Integer memNo, Integer productNo) {
////
////        cartListRedisRedisTemplate.opsForHash().delete(memNo, productNo.toString());
////
////
////    }
////
////
//@Override
//public Map<String, String> findByCompositeKey(Integer memNo) {
//    // 使用 opsForHash().entries() 方法获取购物车项的所有字段和值
//    Map<Object, Object> rawMap = cartListRedisRedisTemplate.opsForHash().entries("cart:" + memNo);
//
//    // 创建一个新的 Map<String, String> 用于存储转换后的数据
//    Map<String, String> resultMap = new HashMap<>();
//    for (Map.Entry<Object, Object> entry : rawMap.entrySet()) {
//        resultMap.put(entry.getKey().toString(), entry.getValue().toString());
//    }
//
//    return resultMap;
//}
//
////
////
////
////
////
//////    @Override
//////    public void createOrderFromCart(Integer memNo) {
//////        // Retrieve cart items from Redis
//////        List<CartRedis> cartItems = findByCompositeKey(memNo);
//////        // Iterate over cart items and save them as order details
//////        for (CartRedis cartItem : cartItems) {
//////            // Create a composite key for the order detail
//////            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
//////            compositeKey.setProductOrdNo(compositeKey.getProductOrdNo()); // Set the order number (assuming you have it)
//////            compositeKey.setProductNo(cartItem.getProductNo());
//////
//////            // Create a new ProductOrderDetail object
//////            ProductOrderDetail orderDetail = new ProductOrderDetail();
//////            orderDetail.setCompositeKey(compositeKey);
//////            orderDetail.setProductPrice(cartItem.getProductPrice());
//////            orderDetail.setProductOrdQty(cartItem.getProductBuyQty());
//////
//////            repository.save(orderDetail);
//////        }
//////    }
//
//
//
//}
////    }
////
////
////}
