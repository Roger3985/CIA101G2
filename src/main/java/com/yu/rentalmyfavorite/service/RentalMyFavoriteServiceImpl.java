package com.yu.rentalmyfavorite.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.roger.member.repository.MemberRepository;
import com.yu.rental.dao.RentalRepository;
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
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memRepository;


    //redis使用
    @Autowired
    @Qualifier("rentalWish")
    private RedisTemplate<String, Map<String, String>> rentalWishRedisTemplate;

    //----------------------------------------------------------------------------------------------------------------------
    //主要為redis使用：

//    // 從 Redis 取得最愛清單 (依據key值搜索)
//    @Override
//    public List<Map<String, String>> getWishFromRedis(String memNo) {
//
//        Set<String> keys = rentalWishRedisTemplate.keys("member:" + memNo + ":myFAVItem:*");
//        List<Map<String, String>> resultList = new ArrayList<>();
//        if (keys != null) {
//            for (String key : keys) {
//                Map<Object, Object> result = rentalWishRedisTemplate.opsForHash().entries(key);
//                Map<String, String> resultMap = new HashMap<>();
//                result.forEach((k, v) -> resultMap.put(k.toString(), v.toString()));
//                resultList.add(resultMap);
//            }
//        }
//        return resultList;
//    }

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
            String memNo = addToWishList.getMemNo().toString();
            String rentalNo = addToWishList.getRentalNo().toString();
            String key = "member:" + memNo + ":myFAVItem:" + rentalNo; // 顯示分層

            Map<String, String> fields = new HashMap<>();
            fields.put("rentalNo", rentalNo);
            fields.put("rentalFavTime", getCurrentDateTime());

            rentalWishRedisTemplate.opsForHash().putAll(key, fields);// 將我的最愛清單存入
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

//    // 加入最愛清單到 Redis
//    @Override
//    public void addWish(String memNo, Map<String, String> wishDetails) {
//        rentalWishRedisTemplate.opsForHash().putAll("member : " + memNo.toString()
//                          + ": myFAVItem : " + wishDetails.get("rentalNo"), wishDetails);
//    }


//    //查詢(memNo)
//    @Override
//    public List<AddToWishList> getWishFromRedis(Integer memNo) {
//        // 初始化ArrayList，存取所有的 AddToWishList 對象
//        List<AddToWishList> addToWishData = new ArrayList<>();
//
//        // 取得收藏的所有品項 (從 Redis 中獲取與該會員編號相關的所有收藏)
//        Set<String> keys = rentalWishRedisTemplate.keys(memNo.toString() + ":*");
//        if (keys != null) {
//            for (String key : keys) {
//                Map<Object, Object> hashEntries = rentalWishRedisTemplate.opsForHash().entries(key);
//                AddToWishList wishItem = new AddToWishList();
//                wishItem.setMemNo(Integer.valueOf(memNo));
//                wishItem.setRentalNo(Integer.valueOf(hashEntries.get("rentalNo").toString()));
//                wishItem.setRentalFavTime(hashEntries.get("rentalFavTime").toString());
//                addToWishData.add(wishItem);
//            }
//        }
//        return addToWishData;
//    }
//}
    //查詢(memNo)
    @Override
    public List<AddToWishList> getWishFromRedis(Integer memNo) {
        // 初始化ArrayList，存取所有的 AddToWishList 對象
        List<AddToWishList> addToWishData = new ArrayList<>();

        try {
            // 取得收藏的所有品項 (從 Redis 中獲取與該會員編號相關的所有收藏)
            Set<String> favorites = rentalWishRedisTemplate.keys(memNo.toString() + ":*");

            // null 檢查
            if (favorites == null) {
                System.err.println("No favorites found for memNo: " + memNo);
                return addToWishData; // 回傳空的列表
            }

            // 遍歷內容，對應 key.value
            for (String key : favorites) {
                //從 Redis 中取得儲存在Hash結構中的資料
                Map<Object, Object> favoriteData = rentalWishRedisTemplate.opsForHash().entries(memNo.toString() + ":" + key.toString());
//                Map<Object, Object> favoriteData = rentalWishRedisTemplate.opsForHash().entries(key);

                // 創建 AddToWishList 對象，設置相關屬性
                AddToWishList item = new AddToWishList();
                item.setMemNo(memNo); // 設置 memNo 屬性

                // 設置 rentalNo 屬性 (檢查 favoriteData 是否包含 rentalNo 鍵，並確認值為字串形式後，再轉換為 Integer 並設置到 item 對象中)
                if (favoriteData.containsKey("rentalNo")) {
                    Object rentalNoObj = favoriteData.get("rentalNo");
                    if (rentalNoObj instanceof String) {
                        Integer rentalNo = Integer.parseInt((String) rentalNoObj);
                        item.setRentalNo(rentalNo);
                    }
                }
                addToWishData.add(item); // 將處理好的資料放入列表
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("錯誤訊息: " + e.getMessage());
        }
        return addToWishData; // 回傳列表
    }

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

    //複合主鍵查詢
    @Override
    public List<RentalMyFavorite> findByRental_RentalNoAndMember_MemNo(Integer rentalNo, Integer memNo) {
        return repository.findByRentalRentalNoAndMemberMemNo(rentalNo, memNo);
    }

    //單筆查詢(rentalFavTime)
    @Override
    public List<RentalMyFavorite> findByRentalFavTime(Timestamp rentalFavTime) {
        return repository.findByRentalFavTime(rentalFavTime);
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

//    @Override
//    public List<RentalMyFavorite> searchRentalMyFAVs(Map<String, Object> map) {
//        if (map.isEmpty()) {
//            return repository.findAll();
//        }
//
//        Integer rentalNo = null;
//        Integer memNo = null;
//        Timestamp rentalFavTime = null;
//
//        if (map.containsKey("rentalNo")) {
//            rentalNo = (Integer) map.get("rentalNo");
//        }
//        if (map.containsKey("memNo")) {
//            memNo = (Integer) map.get("memNo");
//        }
//        if (map.containsKey("rentalFavTime")) {
//            rentalFavTime = (Timestamp) map.get("rentalFavTime");
//        }
//
//        return repository.searchRentalMyFAVs(rentalNo, memNo, rentalFavTime);
//    }



}




