
package com.yu.rentalpic.service;

import com.yu.rentalpic.entity.RentalPic;

import java.util.List;
import java.util.Map;

public interface RentalPicService {

    List<RentalPic> findAll();  //全部查詢(RentalPic)

    RentalPic findByRentalPicNo(Integer rentalPicNo);//單筆查詢(rentalPicNo)

    RentalPic addRentalPic(RentalPic rentalPic); //新增

    RentalPic updateRentalPic(RentalPic rentalPic); //修改

    List<RentalPic> searchRentalPics(Map<String, Object> map); //複合查詢
}
