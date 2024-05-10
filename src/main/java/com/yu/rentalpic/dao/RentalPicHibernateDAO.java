package com.yu.rentalpic.dao;

import com.yu.rentalpic.entity.RentalPic;

import java.util.List;
import java.util.Map;

public interface RentalPicHibernateDAO {

        int add(RentalPic RentalPic);  //若是使用Boolean，即可判斷是否有新增成功

        int update(RentalPic RentalPic); //修改

        int delete(Integer rPicNo); //刪除

    RentalPic getByPK(Integer rPicNo); //使用PK去搜尋處理

        List<RentalPic> getAll(); //萬用複合查詢

        List<RentalPic> getByCompositeQuery(Map<String, String> map); //複合查詢

        List<RentalPic> getAllRentalPics(int currentPage); //查詢當前頁面

        int getPageTotal();
    }
