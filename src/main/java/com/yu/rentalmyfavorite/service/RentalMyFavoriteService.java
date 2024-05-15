package com.yu.rentalmyfavorite.service;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

    public interface RentalMyFavoriteService {

        List<RentalMyFavorite> findAll();  //全部查詢(RentalMyFavorite)

        RentalMyFavorite findByNo(Integer rentalNo); //單筆查詢(rentalNo)

        RentalMyFavorite findByMemNo(Integer memNo); //單筆查詢(memNo)

        RentalMyFavorite findByRentalFavTime(DateTimeFormat rentalFavTime); //單筆查詢(rentalFavTime)

        RentalMyFavorite findByIdRentalNoAndIdMemNo(Integer rentalNo, Integer memNo);  //複合主鍵查詢

//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：增查改

        RentalMyFavorite addRentalFav(RentalMyFavorite rentalMyFavorite); //新增

        RentalMyFavorite updateRentalFav(RentalMyFavorite rentalMyFavorite); //修改

        List<RentalMyFavorite> getByCompositeQuery(Map<String, String[]> map); //複合查詢




    }
