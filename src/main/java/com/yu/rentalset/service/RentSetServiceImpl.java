//package com.yu.rentalset.service;
//
//import com.yu.rental.dao.RentalRepository;
//import com.yu.rental.service.RentalService;
//import com.yu.rentalset.dao.RentSetRepository;
//import com.yu.rentalset.entity.RentSet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class RentSetServiceImpl implements RentSetService {
//
//    @Autowired //自動裝配
//    private RentSetRepository repository;
//
//    /**
//     * PersistenceContext注解用于注入一个EntityManager对象，
//     * 使得我们可以在RentalService类中使用这个entityManager对象执行持久化操作，例如保存、更新、删除实体对象，以及执行JPQL查询等。
//     */
//    @PersistenceContext
//    private EntityManager entityManager;
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
//        return repository.findByRentalOrdNo(rentalOrdNo);
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
//    public List<RentSet> getByCompositeQuery(Map<String, String[]> map) {
//        return null;
//    }
//}
