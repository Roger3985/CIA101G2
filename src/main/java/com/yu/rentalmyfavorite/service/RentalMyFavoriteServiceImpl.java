package com.yu.rentalmyfavorite.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.howard.util.JedisUtil;
import com.yu.rental.dao.RentalRepository;
import com.yu.rentalmyfavorite.dao.RentalMyFavoriteRepository;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class RentalMyFavoriteServiceImpl implements RentalMyFavoriteService {

    @Autowired //自動裝配
    private RentalMyFavoriteRepository repository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    @Qualifier("rentalWish")
    private RedisTemplate<String, Map<String, String>> rentalWishRedisTemplate;

    //redis使用
    JedisPool jedisPool = null;
    public RentalMyFavoriteServiceImpl() {
        jedisPool = JedisUtil.getJedisPool();
    }


//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：

    //單筆查詢(rentalNo)
    @Override
    public RentalMyFavorite findByRentalNo(Integer rentalNo) {
        return repository.findByRental_RentalNo(rentalNo);
    }

    //單筆查詢(memNo)
    @Override
    public RentalMyFavorite findByMemNo(Integer memNo) {
        return repository.findByMember_MemNo(memNo);
    }

    //複合主鍵查詢
    @Override
    public RentalMyFavorite findByIdRentalNoAndIdMemNo(Integer rentalNo, Integer memNo) {
        return repository.findByRental_RentalNoAndMember_MemNo(rentalNo, memNo);
    }

    //複合主鍵查詢
    @Override
    public List<RentalMyFavorite> findByRentalRentalNoAndMemberMemNo(Integer rentalNo, Integer memNo) {
        return repository.findByRentalRentalNoAndMemberMemNo(rentalNo, memNo);
    }

    //單筆查詢(rentalFavTime)
    @Override
    public List<RentalMyFavorite> findByRentalFavTime(Timestamp rentalFavTime){
        return repository.findByRentalFavTime(rentalFavTime);
    }

    //全部查詢(RentalMyFavorite)
    @Override
    public List<RentalMyFavorite> findAll() {
        return repository.findAll();
    }

//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：增查改

    //新增 (PK為null，save方法插入數據)
//    @Override
//    public RentalMyFavorite addRentalFav(RentalMyFavorite rentalMyFavorite) {
//        Timestamp time = rentalMyFavorite.getRentalFavTime();
//        rentalMyFavorite.setRentalFavTime(Timestamp.valueOf(String.valueOf(time)));
//        return repository.save(rentalMyFavorite);
//    }
    @Override
    public RentalMyFavorite addRentalFav(RentalMyFavorite rentalMyFavorite) {
        return repository.save(rentalMyFavorite);
    }


    //修改 (PK有值，save方法修改數據)
    @Override
    public RentalMyFavorite updateRentalFav(RentalMyFavorite rentalMyFavorite) {
        return repository.save(rentalMyFavorite);
    }

    @Override
    public List<RentalMyFavorite> findByCompositeKey(Integer rentalNo) {
        return repository.findByCompositeKey(rentalNo);
    }

    @Override
    public List<RentalMyFavorite> searchRentalMyFAVs(Map<String, Object> map) {

        if (map.isEmpty()) {
            return repository.findAll();
        }

        Integer rentalNo = null;
        Integer memNo = null;
        Timestamp rentalFavTime = null;

        if (map.containsKey("rentalNo")) {
            rentalNo = (Integer) map.get("rentalNo");
        } else if (map.containsKey("memNo")) {
            memNo = (Integer) map.get("memNo");
        } else if (map.containsKey("rentalFavTime")) {
            rentalFavTime = (Timestamp) map.get("rentalFavTime");
        }

        return repository.searchRentalMyFAVs(rentalNo, memNo, rentalFavTime);
    }


    ///////////////////////////////////////////////////////////////////////
    //加入最愛清單 (redis儲存)
    public void addToWishList(Integer memNo, Map<String, String> map) {

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(2); //選擇儲存的redis DB位置
            String itemKey = "member : " + memNo + " : wishList : " + map.get("rentalNo");
            jedis.hmset(itemKey, map);

            // 測試 redis 同步更新
//            Map<String, String> map1 = new HashMap<>();
//            map1.put("rentalNo", String.valueOf(5001));
//            map1.put("rentalCatNo", String.valueOf(1));
//            map1.put("rentalName", "格紋成套西裝");
//            map1.put("rentalPrice", String.valueOf(15500));
//            map1.put("rentalDesPrice", String.valueOf(4000));
//            map1.put("rentalSize", String.valueOf(2));
//            map1.put("rentalColor", "黑色");
//            map1.put("rentalInfo", "款式：劍領 單排釦。適用場合：婚攝 婚宴 宴客 求婚");
//            map1.put("rentalStat", String.valueOf(0));
//            jedis.hmset("member : 2 : cartItem : 5001", map1);
//
//            Map<String, String> map2 = new HashMap<>();
//            map2.put("rentalNo", String.valueOf(5011));
//            map2.put("rentalCatNo", String.valueOf(1));
//            map2.put("rentalName", "雙排扣格紋成套西裝");
//            map2.put("rentalPrice", String.valueOf(12500));
//            map2.put("rentalDesPrice", String.valueOf(4000));
//            map2.put("rentalSize", String.valueOf(3));
//            map2.put("rentalColor", "深卡其色");
//            map2.put("rentalInfo", "款式：標準領 單排釦 雙扣。適用場合：婚攝 婚宴 求婚 日常");
//            map2.put("rentalStat", String.valueOf(0));
//            jedis.hmset("member : 2 : cartItem : 5011", map2);


        }

    }

}


