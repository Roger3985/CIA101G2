package com.howard.rentalorderdetails.service.impl;

import com.changhoward.cia10108springboot.Entity.RentalOrderDetails;
import com.changhoward.cia10108springboot.howard.rentalorderdetails.dao.RentalOrderDetailsRepository;
import com.changhoward.cia10108springboot.howard.rentalorderdetails.service.RentalOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RentalOrderDetailsServiceImpl implements RentalOrderDetailsService {

    @Autowired
    private RentalOrderDetailsRepository repository;

    @Transactional
    @Override
    public void insert(RentalOrderDetails rentalOrderDetails) {
        repository.save(rentalOrderDetails);
    }

    @Transactional
    @Override
    public void update(RentalOrderDetails rod) {

        // 取出 PK 、單價、押金
        Integer rentalOrdNo = rod.getCompositeDetail().getrentalOrdNo();
        Integer rentalNo = rod.getCompositeDetail().getrentalNo();
        BigDecimal rentalPrice = rod.getrentalPrice();
        BigDecimal rentalDesPrice = rod.getrentalDesPrice();
        // 裝進內部類別
        RentalOrderDetails.CompositeDetail compositeDetail = new RentalOrderDetails.CompositeDetail(rentalOrdNo, rentalNo);
        // 裝進類別
        RentalOrderDetails rentalOrderDetails = new RentalOrderDetails(compositeDetail, rentalPrice, rentalDesPrice);
        // 執行更新
        repository.save(rentalOrderDetails);

    }

    @Override
    public RentalOrderDetails findById(Integer rentalOrdNo, Integer rentalNo) {

        // 把 PK 裝進內部類別
        RentalOrderDetails.CompositeDetail compositeDetail = new RentalOrderDetails.CompositeDetail(rentalOrdNo, rentalNo);
        Optional<RentalOrderDetails> optional = repository.findById(compositeDetail);
        //  有找到 ? 取出物件 : 回傳 null
        RentalOrderDetails detail = optional.orElse(null);
        return detail;

    }

    @Override
    public List<RentalOrderDetails> getAll() {
        return repository.findAll();
    }


    public List<RentalOrderDetails> getByAttributes(Map<String, Object> map) {

        if (map.isEmpty()) {
            return repository.findAll();
        }

        Integer rentalOrdNo = null;
        Integer rentalNo = null;
        BigDecimal rentalPrice = null;
        BigDecimal rentalDesPrice = null;
        if (map.containsKey("rentalOrdNo")) {
            rentalOrdNo = (Integer) map.get("rentalOrdNo");
        }
        if (map.containsKey("rentalNo")) {
            rentalNo = (Integer) map.get("rentalNo");
        }
        if (map.containsKey("rentalPrice")) {
            rentalPrice = (BigDecimal) map.get("rentalPrice");
        }
        if (map.containsKey("rentalDesPrice")) {
            rentalDesPrice = (BigDecimal) map.get("rentalDesPrice");
        }

        return repository.findByAttributes(rentalOrdNo, rentalNo, rentalPrice, rentalDesPrice);

    }


}
