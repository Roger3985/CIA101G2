package com.yu.rentalmyfavorite.service;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalset.entity.RentSet;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

    public interface RentalMyFAVService {

        RentalMyFavorite addRentalFav(Integer rNo, Integer memNo, Timestamp rFavTime);

        RentalMyFavorite updateRentalFav(Integer rNo, Integer memNo, Timestamp rFavTime);


        RentalMyFavorite getOneRentalFav(Integer rNo);//單筆查詢

        List<RentalMyFavorite> getAll();  //萬用複合查詢

        List<RentalMyFavorite> getByCompositeQuery(Map<String, String[]> map); //複合查詢


        RentSet findByIdRentalNoAndIdMemNo(Integer rentalNo, Integer memNo);

    }
