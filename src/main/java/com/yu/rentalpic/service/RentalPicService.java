package com.yu.rentalpic.service;

import com.yu.rentalpic.entity.RentalPic;

import java.util.List;
import java.util.Map;

    public interface RentalPicService {

        RentalPic addRentalPic(Integer rNo, byte[] rPic);

        RentalPic updateRentalPic(Integer rNo, byte[] rPic);


        RentalPic getOneRentalPic(Integer rPicNo);//單筆查詢

        List<RentalPic> getAll();  //萬用複合查詢


        List<RentalPic> getByCompositeQuery(Map<String, String[]> map); //複合查詢

    }