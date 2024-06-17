package com.yu.rentalmyfavorite.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.yu.rentalmyfavorite.dao.RentalMyFavoriteRepository;
import com.yu.rentalmyfavorite.dto.AddToWishList;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class RentalMyFavoriteServiceImpl implements RentalMyFavoriteService {

    @Autowired //自動裝配
    private RentalMyFavoriteRepository repository;

    //redis使用
    @Autowired
    @Qualifier("rentalWish")
    private RedisTemplate<String, Map<String, String>> rentalWishRedisTemplate;

//----------------------------------------------------------------------------------------------------------------------
//主要為redis使用：

    // 刪除 Redis 中的最愛清單
    @Override
    public void deleteWish(Integer memNo, Integer rentalNo) {
        String key = "member:" + memNo + ":myFAVItem:" + rentalNo;
        rentalWishRedisTemplate.delete(key);
    }


    //新增 (若點擊加入最愛，匯入至清單內顯示)
    @Override
    public void addRentalFav(AddToWishList addToWishList) {

        try {
            Integer memNo = addToWishList.getMemNo();  //須放入當分層的值
            Integer rentalNo = addToWishList.getRentalNo();  //須放入當分層的值
            String key = "member:" + memNo + ":myFAVItem:" + rentalNo; // 顯示分層(會員編號 : 1 : myFAVItem : 商品編號)

            Map<String, String> fields = new HashMap<>();
            fields.put("rentalNo", addToWishList.getRentalNo().toString());  //此處rentalNo的來源 (addToWishList.getRentalNo().toString();)
            fields.put("rentalFavTime", getCurrentDateTime());

            rentalWishRedisTemplate.opsForHash().putAll(key, fields);// 將我的最愛清單存入
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    //查詢(memNo)
    @Override
    public List<AddToWishList> getWishFromRedis(Integer memNo) {
        // 初始化ArrayList，存取所有的 AddToWishList 對象
        List<AddToWishList> addToWishData = new ArrayList<>();

        try {
            String favoriteKey = "member:" + memNo;
            // 取得收藏的所有品項 (從 Redis 中取得與該會員編號相關的所有收藏)
            Set<String> favorites = rentalWishRedisTemplate.keys(favoriteKey + ":*");

            // 遍歷內容，對應 key.value
            for (String key : favorites) {

                //從 Redis 中取得儲存在Hash結構中的資料
                Map<Object, Object> favoriteData = rentalWishRedisTemplate.opsForHash().entries(key);

                // 創建 AddToWishList 對象，設置相關屬性
                AddToWishList item = new AddToWishList();
                item.setMemNo(memNo); // 設置 memNo 屬性

                // 設置 rentalNo 屬性
                // (檢查 favoriteData 是否包含 rentalNo 鍵，並確認值為字串形式後，再轉換為 Integer 並設置到 item 對象中)
                if (favoriteData.containsKey("rentalNo")) {
                    Integer rentalNo = Integer.parseInt((String) favoriteData.get("rentalNo"));
                    item.setRentalNo(rentalNo);
                }

                // 設置加入時間
                if (favoriteData.containsKey("rentalFavTime")) {
                    item.setRentalFavTime((String) favoriteData.get("rentalFavTime"));
                }
                addToWishData.add(item); // 將處理好的資料放入列表
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addToWishData; // 回傳列表
    }

    //----------------------------------------------------------------------------------------------------------------------
    //單筆查詢(rentalNo)
    @Override
    public RentalMyFavorite findByRentalNo(Integer rentalNo) {
        return repository.findByRental_RentalNo(rentalNo);
    }

    //複合主鍵查詢
    @Override
    public RentalMyFavorite findByRentalNoAndMemNo(Integer rentalNo, Integer memNo) {
        return repository.findByRental_RentalNoAndMember_MemNo(rentalNo, memNo);
    }

    //全部查詢(RentalMyFavorite)
    @Override
    public List<RentalMyFavorite> findAll() {
        return repository.findAll();
    }


    @Override
    public List<RentalMyFavorite> findByCompositeKey(Integer rentalNo) {
        return repository.findByCompositeKey(rentalNo);
    }

}




