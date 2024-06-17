package com.yu.rentSet.service;

import com.yu.rentSet.entity.RentSet;

import java.util.List;
import java.util.Map;

public interface RentSetService {

    List<RentSet> findAll();  //全部查詢(RentSet)

    RentSet findByRentalOrdNo(Integer rentalOrdNo);//單筆查詢(rentalOrdNo)

    RentSet addRentSet(RentSet rentSet); //新增

    RentSet updateRentSet(RentSet rentSet); //修改

    public List<RentSet> searchRentSets(Map<String, Object> map); //複合查詢

}
