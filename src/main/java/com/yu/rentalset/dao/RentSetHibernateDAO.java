package com.yu.rentalset.dao;

import com.yu.rentalset.entity.RentSet;

import java.util.List;
import java.util.Map;

public interface RentSetHibernateDAO {

        int add(RentSet RentSet);  //若是使用Boolean，即可判斷是否有新增成功

        int update(RentSet RentSet); //修改

        int delete(Integer rOrdNo); //刪除

    RentSet getByPK(Integer rOrdNo); //使用PK去搜尋處理

//    List<RentSet> getByName(String rCatName);

        List<RentSet> getAll(); //萬用複合查詢

        List<RentSet> getByCompositeQuery(Map<String, String> map); //複合查詢

        List<RentSet> getAllRentalSets(int currentPage); //查詢當前頁面

        int getPageTotal();
    }
