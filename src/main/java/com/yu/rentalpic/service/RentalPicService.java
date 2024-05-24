
package com.yu.rentalpic.service;

import com.yu.rentalpic.entity.RentalPic;

import java.util.List;
import java.util.Map;

public interface RentalPicService {

    List<RentalPic> findAll();  //全部查詢(RentalPic)

    RentalPic findByRentalPicNo(Integer rentalPicNo);//單筆查詢(rentalPicNo)

    RentalPic findByRentalRentalNo(Integer rentalNo); //單筆查詢(rentalNo)

//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：增查改

    RentalPic addRentalPic(RentalPic rentalPic); //新增
//    RentalPic addRentalPic(Integer rNo, byte[] rPic); //新增

    RentalPic updateRentalPic(RentalPic rentalPic); //新增
//    RentalPic updateRentalPic(Integer rNo, byte[] rPic); //修改


    public void updatePicture(RentalPic rentalPic, byte[] rentalFile); //修改照片


    List<RentalPic> searchRentalPics(Map<String, Object> map); //複合查詢

}
