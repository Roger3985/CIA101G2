//package com.yu.rentSet.service;
//
//import com.yu.rentSet.dao.RentSetRepository;
//import com.yu.rentSet.entity.RentSet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class RentSetServiceImpl implements RentSetService {
//
//    @Autowired //自動裝配
//    private RentSetRepository repository;
//
//
//    //全部查詢(RentSet)
//    @Override
//    public List<RentSet> findAll() {
//        return repository.findAll();
//    }
//
//    //單筆查詢(rentalOrdNo)
//    @Override
//    public RentSet findByRentalOrdNo(Integer rentalOrdNo) {
//        return repository.findByRentalOrder_RentalOrdNo(rentalOrdNo);
//    }
//
//    //單筆查詢(rentalSetName)
//    @Override
//    public RentSet findByRentalSetName(String rentalSetName) {
//        return repository.findByRentalSetName(rentalSetName);
//    }
//
//    //單筆查詢(rentalSetDays)
//    @Override
//    public RentSet findByRentalSetDays(Byte rentalSetDays) {
//        return repository.findByRentalSetDays(rentalSetDays);
//    }
//
//
////----------------------------------------------------------------------------------------------------------------------
////主要為後端使用：增查改
//
//    //新增 (PK為null，save方法插入數據)
//    @Override
//    public RentSet addRentSet(RentSet rentSet) {
//        return repository.save(rentSet);
//    }
//
//    //修改 (PK有值，save方法修改數據)
//    @Override
//    public RentSet updateRentSet(RentSet rentSet) {
//        return repository.save(rentSet);
//    }
//
//    //複合查詢
//    @Override
//    public List<RentSet> searchRentSets(Map<String, Object> map) {
//
//        if (map.isEmpty()) {
//            return repository.findAll();
//        }
//
//        Integer rentalOrdNo = null;
//        String rentalSetName = null;
//        Byte rentalSetDays = null;
//
//        if (map.containsKey("rentalOrdNo")) {
//            rentalOrdNo = (Integer) map.get("rentalOrdNo");
//        } else if (map.containsKey("rentalSetName")) {
//            rentalSetName = (String) map.get("rentalSetName");
//        } else if (map.containsKey("rentalSetDays")) {
//            rentalSetDays = (Byte) map.get("rentalSetDays");
//        }
//
//        return repository.searchRentSets(rentalOrdNo, rentalSetName, rentalSetDays);
//    }
//
//
//
//}
