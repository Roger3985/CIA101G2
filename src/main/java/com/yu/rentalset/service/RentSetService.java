//package com.yu.rentalset.service;
//
//import com.yu.rental.entity.Rental;
//import com.yu.rentalset.entity.RentSet;
//
//import java.util.List;
//import java.util.Map;
//
//    public interface RentSetService {
//
//        List<RentSet> findAll();  //全部查詢(RentSet)
//
//        RentSet findByRentalOrdNo(Integer rentalOrdNo);//單筆查詢(rentalOrdNo)
//
//        RentSet findByRentalSetName(String rentalSetName);//單筆查詢(rentalSetName)
//
//        RentSet findByRentalSetDays(Byte rentalSetDays);//單筆查詢(rentalSetDays)
//
////----------------------------------------------------------------------------------------------------------------------
////主要為後端使用：增查改
//
//        RentSet addRentSet(RentSet rentSet); //新增
//
//        RentSet updateRentSet(RentSet rentSet); //修改
//
//        List<RentSet> getByCompositeQuery(Map<String, String[]> map); //複合查詢
//
//    }
