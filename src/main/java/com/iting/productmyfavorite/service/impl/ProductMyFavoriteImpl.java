package com.iting.productmyfavorite.service.impl;

import com.iting.cart.entity.CartRedis;
import com.iting.cart.redis.JedisPoolUtil;
import com.iting.productmyfavorite.dao.ProductMyFavoriteRepository;
import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productmyfavorite.entity.ProductMyFavoriteRedis;
import com.iting.productmyfavorite.service.ProductMyFavoriteService;
import com.iting.productorder.entity.ProductOrder;
import com.ren.product.dao.ProductRepository;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("productMyFavoriteService")
public class ProductMyFavoriteImpl implements ProductMyFavoriteService {
    //單筆查詢(rentalNo)
    @Autowired //自動裝配
    private ProductMyFavoriteRepository repository;
    @Autowired
    private ProductRepository productRepository;

@Autowired
ProductServiceImpl productService;
    private final JedisPool jedisPool = JedisPoolUtil.getJedisPool();

    @Override
    public void addProductMyFavorite(ProductMyFavoriteRedis productMyFavoriteRedis) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(11);
            String favoriteKey = "member:" + productMyFavoriteRedis.getMemNo(); // 使用会员编号构建收藏夹键
            String Key = favoriteKey + ":" + productMyFavoriteRedis.getProductNo(); // 使用会员编号和商品编号构建商品键

            // 使用hmset一次将多个字段存储到哈希中
            Map<String, String> fields = new HashMap<>();
            fields.put("productNo", productMyFavoriteRedis.getProductNo().toString());
            fields.put("FavTime", getCurrentDateTime());
            jedis.hmset(Key, fields); // 将商品信息存储到哈希中

        } catch (Exception e) {
            // 处理异常，例如记录日志或者抛出自定义异常
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    private String getCurrentDateTime() {
        // 获取当前时间并格式化为字符串
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);
    }


    @Override
    public List<ProductMyFavoriteRedis> findByKey(Integer memNo) {
        List<ProductMyFavoriteRedis> productMyFavoriteRedisList = new ArrayList<>();

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(11);
            String favoriteKey = "member:" + memNo; // 使用传入的会员编号构建收藏夹键

            // 获取收藏夹中的所有商品
            Set<String> favorites = jedis.keys(favoriteKey + ":*");
            for (String key : favorites) {
                Map<String, String> favoriteData = jedis.hgetAll(key);

                // 创建一个新的 ProductMyFavoriteRedis 对象，并设置相应的属性
                ProductMyFavoriteRedis item = new ProductMyFavoriteRedis();
                item.setMemNo(memNo); // 使用传入的会员编号

                if (favoriteData.containsKey("productNo")) {
                    Integer productNo = Integer.parseInt(favoriteData.get("productNo"));
                    item.setProductNo(productNo);
                }

                if (favoriteData.containsKey("FavTime")) {
                    item.setFavTime(favoriteData.get("FavTime"));
                }

                // 将对象添加到列表中
                productMyFavoriteRedisList.add(item);
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }

        return productMyFavoriteRedisList;
    }



    @Override
    public ProductMyFavorite findByProductNo(Integer  productNo) {
        return repository.findByProduct_ProductNo( productNo);
    }

    //單筆查詢
    @Override
    public List<ProductMyFavorite> findByMemNo(Integer memNo) {
        return repository.findByMember_MemNo(memNo);
    }

    //複合主鍵查詢
    @Override
    public ProductMyFavorite findByProductNoAndMemNo(Integer productNo, Integer memNo) {
        return repository.findByProduct_ProductNoAndMember_MemNo(productNo, memNo);
    }

    //複合主鍵查詢
    @Override
    public List<ProductMyFavorite> findByProductProductNoAndMemberMemNo(Integer productNo, Integer memNo) {
        return repository.findByProductProductNoAndMemberMemNo(productNo, memNo);
    }

    @Override
    public List<ProductMyFavorite> findByCompositeKey(Integer productNo) {
        return repository.findByCompositeKey(productNo);
    }

    //全部查詢
    @Override
    public List<ProductMyFavorite> findAll() {
        return repository.findAll();
    }


    @Override
    public void delete(Integer memNo, Integer productNo) {

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(11);
            // 生成 memNo 和 productNo 鍵
            String Key = "member:" + memNo + ":" + productNo;
            // 從購物車中刪除商品資訊

            jedis.del(Key);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
