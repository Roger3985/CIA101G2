package com.yu.rentalmyfavorite.dao;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;

import java.util.List;
import java.util.Map;

public interface RentalMyFavoriteHibernateDAO {

    int add(RentalMyFavorite RentalMyFavorite);  //若是使用Boolean，即可判斷是否有新增成功

    int update(RentalMyFavorite RentalMyFavorite); //修改

    int delete(Integer rNo, Integer memNo); //刪除

    RentalMyFavorite getByPK(Integer rNo, Integer memNo); //使用PK去搜尋處理

//    List<RentalMyFavorite> getByName(String rCatName);

    List<RentalMyFavorite> getAll(); //萬用複合查詢

    List<RentalMyFavorite> getByCompositeQuery(Map<String, String> map); //複合查詢

    List<RentalMyFavorite> getAllRentalFavs(int currentPage); //查詢當前頁面

    int getPageTotal();
}
