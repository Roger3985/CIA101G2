package com.yu.rentalmyfavorite.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.yu.rentalmyfavorite.dao.RentalMyFavoriteRepository;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RentalMyFavorite findByRentalNo(Integer rentalNo) {
        return repository.findByRental_RentalNo(rentalNo);
    }

    //單筆查詢(memNo)
    @Override
    public RentalMyFavorite findByMemNo(Integer memNo) {
        return repository.findByMember_MemNo(memNo);
    }

    //複合主鍵查詢
    @Override
    public RentalMyFavorite findByIdRentalNoAndIdMemNo(Integer rentalNo, Integer memNo) {
        return repository.findByRental_RentalNoAndMember_MemNo(rentalNo, memNo);
    }

    //單筆查詢(rentalFavTime)
    @Override
    public List<RentalMyFavorite> findByRentalFavTime(Timestamp rentalFavTime){
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
//    @Override
//    public RentalMyFavorite addRentalFav(RentalMyFavorite rentalMyFavorite) {
//        Timestamp time = rentalMyFavorite.getRentalFavTime();
//        rentalMyFavorite.setRentalFavTime(Timestamp.valueOf(String.valueOf(time)));
//        return repository.save(rentalMyFavorite);
//    }
    @Override
    public RentalMyFavorite addRentalFav(RentalMyFavorite rentalMyFavorite) {
        return repository.save(rentalMyFavorite);
    }


    //修改 (PK有值，save方法修改數據)
    @Override
    public RentalMyFavorite updateRentalFav(RentalMyFavorite rentalMyFavorite) {
        return repository.save(rentalMyFavorite);
    }

    @Override
    public List<RentalMyFavorite> searchRentalMyFAVs(Map<String, Object> map) {

        if (map.isEmpty()) {
            return repository.findAll();
        }

        Integer rentalNo = null;
        Integer memNo = null;
        Timestamp rentalFavTime = null;

        if (map.containsKey("rentalNo")) {
            rentalNo = (Integer) map.get("rentalNo");
        } else if (map.containsKey("memNo")) {
            memNo = (Integer) map.get("memNo");
        } else if (map.containsKey("rentalFavTime")) {
            rentalFavTime = (Timestamp) map.get("rentalFavTime");
        }

        return repository.searchRentalMyFAVs(rentalNo, memNo, rentalFavTime);
    }


}


