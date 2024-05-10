package com.Cia101G2.howard.rentalmytrack.service.impl;

import com.Cia101G2.howard.rentalmytrack.entiy.RentalMyTrack;
import com.Cia101G2.howard.rentalmytrack.dao.RentalMyTrackRepository;
import com.Cia101G2.howard.rentalmytrack.service.RentalMyTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RentalMyTrackServiceImpl implements RentalMyTrackService {

    @Autowired
    RentalMyTrackRepository rentalMyTrackRepository;

    @Transactional
    @Override
    public void insert(RentalMyTrack rentalMyTrack) {
        rentalMyTrackRepository.save(rentalMyTrack);
    }
    @Transactional
    @Override
    public void update(RentalMyTrack rmt) {

        // 取出 PK、期望租借日期
        Integer rentalNo = rmt.getCompositeTrack().getrentalNo();
        Integer memNo = rmt.getCompositeTrack().getMemNo();
        Date expRentalDate = rmt.getExpRentalDate();
        // 把 PK 存進內部類別
        RentalMyTrack.CompositeTrack compositeTrack = new RentalMyTrack.CompositeTrack(rentalNo, memNo);
        // 用 PK 取得該筆資料(物件) (因為 rTrackTime 是 NotNull)
        RentalMyTrack rentalMyTrack2 = findById(rentalNo, memNo);
        // 用該資料(物件)去取得加入追蹤時間
        Timestamp rentalTrackTime = rentalMyTrack2.getrentalTrackTime();
        // 把得到的資料裝進物件裡
        RentalMyTrack rentalMyTrack = new RentalMyTrack(compositeTrack, rentalTrackTime, expRentalDate);
        // 執行更新
        rentalMyTrackRepository.save(rentalMyTrack);

    }
    @Override
    public RentalMyTrack findById(Integer rentalNo, Integer memNo) {

        // 先把 PK 裝進內部類別
        RentalMyTrack.CompositeTrack compositeTrack = new RentalMyTrack.CompositeTrack(rentalNo, memNo);
        Optional<RentalMyTrack> rentalMyTrackOptional = rentalMyTrackRepository.findById(compositeTrack);
        // 有找到 ? 取出物件 : 回傳 null
        RentalMyTrack rmt = rentalMyTrackOptional.orElse(null);
        return rmt;

    }
    @Override
    public List<RentalMyTrack> getAll() {
        return rentalMyTrackRepository.findAll();
    }


    public List<RentalMyTrack> getByAttributes(Map<String, Object> map) {

        if (map.isEmpty()) {
            return rentalMyTrackRepository.findAll();
        }

        Integer rentalNo = null;
        Integer memNo = null;
        Timestamp rentalTrackTime = null;
        Date expRentalDate = null;
        if (map.containsKey("rentalNo")) {
            rentalNo = (Integer) map.get("rentalNo");
        }
        if (map.containsKey("memNo")) {
            memNo = (Integer) map.get("memNo");
        }
        if (map.containsKey("rentalTrackTime")) {
            rentalTrackTime = (Timestamp) map.get("rentalTrackTime");
        }
        if (map.containsKey("expRentalDate")) {
            expRentalDate = (Date) map.get("expRentalDate");
        }

        return rentalMyTrackRepository.findByAttributes(rentalNo, memNo, rentalTrackTime, expRentalDate);

    }

}
