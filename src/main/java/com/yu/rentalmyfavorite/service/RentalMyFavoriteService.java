package com.yu.rentalmyfavorite.service;


import com.yu.rentalmyfavorite.entity.RentalMyFavorite;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface RentalMyFavoriteService {
//----------------------------------------------------------------------------------------------------------------------
//主要由redis使用

    Map<String, String> getWish(Integer memNo, Integer rentalNo);

    void deleteWish(Integer memNo, Integer rentalNo);

    void addWish(Integer memNo, Map<String, String> wishDetails);

    List<AddToWishList> findByMemNo(Integer memNo); //單筆查詢(memNo)

//----------------------------------------------------------------------------------------------------------------------
    RentalMyFavorite findByRentalNo(Integer rentalNo); //單筆查詢(rentalNo)

    RentalMyFavorite findByRentalNoAndMemNo(Integer rentalNo, Integer memNo);  //複合主鍵查詢

    List<RentalMyFavorite> findByRental_RentalNoAndMember_MemNo(Integer rentalNo, Integer memNo);  //複合主鍵查詢

    List<RentalMyFavorite> findAll();  //全部查詢(RentalMyFavorite)

    List<RentalMyFavorite> findByRentalFavTime(Timestamp rentalFavTime); //單筆查詢(rentalFavTime)

//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：增查改

    void addRentalFav(AddToWishList addToWishList); //新增

    void delete(Integer rentalNo, Integer memNo); //刪除

    List<RentalMyFavorite> findByCompositeKey(Integer rentalNo);

    List<RentalMyFavorite> searchRentalMyFAVs(Map<String, Object> map); //複合查詢
}
