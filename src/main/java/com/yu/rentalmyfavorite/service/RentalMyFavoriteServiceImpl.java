package com.yu.rentalmyfavorite.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.yu.rentalmyfavorite.dao.RentalMyFavoriteRepository;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class RentalMyFavoriteServiceImpl implements RentalMyFavoriteService {

    @Autowired //自動裝配
    private RentalMyFavoriteRepository repository;

    /**
     * PersistenceContext注解用于注入一个EntityManager对象，
     * 使得我们可以在RentalService类中使用这个entityManager对象执行持久化操作，例如保存、更新、删除实体对象，以及执行JPQL查询等。
     */
    @PersistenceContext
    private EntityManager entityManager;

    //單筆查詢(rentalNo)
    @Override
    public RentalMyFavorite findByNo(Integer rentalNo) {
        return repository.findByRentalNo(rentalNo);
    }

    //單筆查詢(memNo)
    @Override
    public RentalMyFavorite findByMemNo(Integer memNo) {
        return repository.findByMemNo(memNo);
    }

    //複合主鍵查詢
    @Override
    public RentalMyFavorite findByIdRentalNoAndIdMemNo(Integer rentalNo, Integer memNo) {
        return repository.findByIdRentalNoAndIdMemNo(rentalNo, memNo);
    }

    //單筆查詢(rentalFavTime)
    @Override
    public RentalMyFavorite findByRentalFavTime(DateTimeFormat rentalFavTime){
        return repository.findByRentalFavTime(rentalFavTime);
    }

    //全部查詢(RentalMyFavorite)
    @Override
    public List<RentalMyFavorite> findAll() {
        return repository.findAll();
    }

//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：增查改

    //新增 (PK為null，save方法插入數據)
    @Override
    public RentalMyFavorite addRentalFav(RentalMyFavorite rentalMyFavorite) {
        return repository.save(rentalMyFavorite);
    }

    //修改 (PK有值，save方法修改數據)
    @Override
    public RentalMyFavorite updateRentalFav(RentalMyFavorite rentalMyFavorite) {
        return repository.save(rentalMyFavorite);
    }







//        @Override
//        public RentalMyFavorite addRentalCat(String rCatName, Integer rStockQty, Integer rRentedQty, BigDecimal rDesPrice) {
//
//            RentalMyFavorite RentalMyFavorite = new RentalMyFavorite();
//            RentalMyFavorite.setrCatName(rCatName);
//            RentalMyFavorite.setrStockQty(rStockQty);
//            RentalMyFavorite.setrRentedQty(rRentedQty);
//            RentalMyFavorite.setrDesPrice(rDesPrice);
//            dao.add(RentalMyFavorite);// 將VO放入DAO的方法內執行資料庫操作
//
//            return RentalMyFavorite;
//        }
//        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// updateRentalCat
//        @Override
//        public RentalMyFavorite updateRentalCat(Integer rCatNo,String rCatName, Integer rStockQty, Integer rRentedQty, BigDecimal rDesPrice) {
//
//            RentalMyFavorite RentalMyFavorite = new RentalMyFavorite();
//            RentalMyFavorite.setrCatNo(rCatNo);
//            RentalMyFavorite.setrCatName(rCatName);
//            RentalMyFavorite.setrStockQty(rStockQty);
//            RentalMyFavorite.setrRentedQty(rRentedQty);
//            RentalMyFavorite.setrDesPrice(rDesPrice);
//
//            dao.update(RentalMyFavorite);
//            return RentalMyFavorite;
//        }
//
//        @Override
//        public void deleteRentalCat(Integer rCatNo) {
//            dao.delete(rCatNo);
//        }
//
//
//        @Override //單筆查詢(PK)
//        public RentalMyFavorite getOneRentalCat(Integer rCatNo) {
//            return dao.getByPK(rCatNo);
//        }
//
//        @Override   //萬用複合查詢
//        public List<RentalMyFavorite> findAll() {
//            return dao.getAll();
//        }
    //複合查詢
        @Override
        public List<RentalMyFavorite> getByCompositeQuery(Map<String, String[]> map) {
//            Map<String, String> query = new HashMap<>();
//            // Map.Entry即代表一組key-value
//            Set<Map.Entry<String, String[]>> entry = map.entrySet();
//
//            for (Map.Entry<String, String[]> row : entry) {
//                String key = row.getKey();
//                // 因為請求參數裡包含了action，做個去除動作
//                if ("action".equals(key)) {
//                    continue;
//                }
//                // 若是value為空即代表沒有查詢條件，做個去除動作
//                String value = row.getValue()[0]; // getValue拿到一個String陣列, 接著[0]取得第一個元素檢查
//                if (value == null || value.isEmpty()) {
//                    continue;
//                }
//                query.put(key, value);
//            }
//
//            System.out.println(query);

//            return dao.getByCompositeQuery(query);
            return null;
        }




}


