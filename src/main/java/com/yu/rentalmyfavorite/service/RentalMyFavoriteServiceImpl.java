package com.yu.rentalmyfavorite.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.howard.util.JedisUtil;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import com.yu.rentalmyfavorite.dao.RentalMyFavoriteRepository;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Service
@Getter
@Setter
public class RentalMyFavoriteServiceImpl implements RentalMyFavoriteService {

    @Autowired //自動裝配
    private RentalMyFavoriteRepository repository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memRepository;

    //redis使用
    @Autowired
    @Qualifier("rentalWish")
    private RedisTemplate<Integer, Map<String, String>> rentalWishRedisTemplate;




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

    //查詢加入最愛的會員
    @Override
    public List<RentalMyFavorite> getFAVByMemNo(Integer memNo){
        return repository.getFAVByMemNo(memNo);
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

    //新增 (若點擊加入最愛，匯入至清單內顯示)
//    @Override
//    public Integer addRentalFav(Integer rentalNo, Integer memeNo) {
//
//        //取得商品資訊(找出對應的rentalNo)
//        //取得點擊的會員編號
//        //前端抓取即時時間
//
//        RentalMyFavorite rentalMyFavorite = repository.();
//        Timestamp time = rentalMyFavorite.getRentalFavTime();
//        rentalMyFavorite.setRentalFavTime(Timestamp.valueOf(String.valueOf(time)));
//        return ;
//    }

    //新增 (PK為null，save方法插入數據)
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
    //加入最愛清單 (redis儲存 "內部由json格式傳遞")
    public void addToWishList(Integer memNo, Map<String, String> rentalDetails) {
        rentalWishRedisTemplate.opsForHash().put(memNo, rentalDetails.get("rentalNo"), rentalDetails);
    }


    //取出最愛清單 (redis取出物件，"取出為json格式，須轉型態")
    public Map<Object, Object> getWishList(Integer memNo) {
        return rentalWishRedisTemplate.opsForHash().entries(memNo);
    }

    public Map<Object, RentalMyFavorite> findRedisByMemNo(Integer memNo) {
        // 从数据库获取最爱清单的逻辑...
        return null;
    }





}




