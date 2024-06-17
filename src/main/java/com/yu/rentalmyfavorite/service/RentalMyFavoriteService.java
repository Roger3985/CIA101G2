package com.yu.rentalmyfavorite.service;

import com.yu.rentalmyfavorite.dto.AddToWishList;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import java.util.List;

public interface RentalMyFavoriteService {
//----------------------------------------------------------------------------------------------------------------------
//主要由redis使用

    void addRentalFav(AddToWishList addToWishList);

    void deleteWish(Integer memNo, Integer rentalNo);

    List<AddToWishList> getWishFromRedis(Integer memNo);

//----------------------------------------------------------------------------------------------------------------------
    RentalMyFavorite findByRentalNo(Integer rentalNo); //單筆查詢(rentalNo)

    RentalMyFavorite findByRentalNoAndMemNo(Integer rentalNo, Integer memNo);  //複合主鍵查詢

    List<RentalMyFavorite> findAll();  //全部查詢(RentalMyFavorite)

    List<RentalMyFavorite> findByCompositeKey(Integer rentalNo);

}
