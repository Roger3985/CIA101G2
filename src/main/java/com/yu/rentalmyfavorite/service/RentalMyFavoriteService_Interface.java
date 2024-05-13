package com.yu.rentalmyfavorite.service;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

    public interface RentalMyFavoriteService_Interface {

        RentalMyFavorite addRentalFav(Integer rNo, Integer memNo, Timestamp rFavTime);

        RentalMyFavorite updateRentalFav(Integer rNo, Integer memNo, Timestamp rFavTime);

        void deleteRentalFav(Integer rNo);

        RentalMyFavorite getOneRentalFav(Integer rNo);//單筆查詢

        List<RentalMyFavorite> getAll();  //萬用複合查詢

        List<RentalMyFavorite> getAllRentalFavs(int currentPage); //查詢當前頁面

        List<RentalMyFavorite> getByCompositeQuery(Map<String, String[]> map); //複合查詢

        int getPageTotal();
    }
