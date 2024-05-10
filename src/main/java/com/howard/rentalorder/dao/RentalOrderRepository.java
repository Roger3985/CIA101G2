package com.howard.rentalorder.dao;

import com.howard.rentalorder.entity.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Component
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Integer> {


    @Transactional
    @Query("SELECT o FROM RentalOrder o WHERE " +
            "(:rentalOrdNo IS NULL OR o.rentalOrdNo = :rentalOrdNo) AND " +
            "(:memNo IS NULL OR o.member.memNo = :memNo) AND " +
            "(:rentalByrName IS NULL OR o.rentalByrName = :rentalByrName) AND " +
            "(:rentalByrPhone IS NULL OR o.rentalByrPhone = :rentalByrPhone) AND " +
            "(:rentalByrEmail IS NULL OR o.rentalByrEmail = :rentalByrEmail) AND " +
            "(:rentalRcvName IS NULL OR o.rentalRcvName = :rentalRcvName) AND " +
            "(:rentalRcvPhone IS NULL OR o.rentalRcvPhone = :rentalRcvPhone) AND " +
            "(:rentalTakeMethod IS NULL OR o.rentalTakeMethod = :rentalTakeMethod) AND " +
            "(:rentalAddr IS NULL OR o.rentalAddr = :rentalAddr) AND " +
            "(:rentalPayMethod IS NULL OR o.rentalPayMethod = :rentalPayMethod) AND " +
            "(:rentalAllPrice IS NULL OR o.rentalAllPrice = :rentalAllPrice) AND " +
            "(:rentalAllDepPrice IS NULL OR o.rentalAllDepPrice = :rentalAllDepPrice) AND " +
            "(:rentalOrdTime IS NULL OR o.rentalOrdTime = :rentalOrdTime) AND " +
            "(:rentalDate IS NULL OR o.rentalDate = :rentalDate) AND " +
            "(:rentalBackDate IS NULL OR o.rentalBackDate = :rentalBackDate) AND " +
            "(:rentalRealBackDate IS NULL OR o.rentalRealBackDate = :rentalRealBackDate) AND " +
            "(:rentalPayStat IS NULL OR o.rentalPayStat = :rentalPayStat) AND " +
            "(:rentalOrdStat IS NULL OR o.rentalOrdStat = :rentalOrdStat) AND " +
            "(:rtnStat IS NULL OR o.rtnStat = :rtnStat) AND " +
            "(:rtnRemark IS NULL OR o.rtnRemark = :rtnRemark) AND " +
            "(:rtnCompensation IS NULL OR o.rtnCompensation = :rtnCompensation)")
    List<RentalOrder> findByAttributes(@Param("rentalOrdNo") Integer rentalOrdNo,
                                       @Param("memNo") Integer memNo,
                                       @Param("rentalByrName") String rentalByrName,
                                       @Param("rentalByrPhone") String rentalByrPhone,
                                       @Param("rentalByrEmail") String rentalByrEmail,
                                       @Param("rentalRcvName") String rentalRcvName,
                                       @Param("rentalRcvPhone") String rentalRcvPhone,
                                       @Param("rentalTakeMethod") Byte rentalTakeMethod,
                                       @Param("rentalAddr") String rentalAddr,
                                       @Param("rentalPayMethod") Byte rentalPayMethod,
                                       @Param("rentalAllPrice") BigDecimal rentalAllPrice,
                                       @Param("rentalAllDepPrice") BigDecimal rentalAllDepPrice,
                                       @Param("rentalOrdTime") Timestamp rentalOrdTime,
                                       @Param("rentalDate") Timestamp rentalDate,
                                       @Param("rentalBackDate") Timestamp rentalBackDate,
                                       @Param("rentalRealBackDate") Timestamp rentalRealBackDate,
                                       @Param("rentalPayStat") Byte rentalPayStat,
                                       @Param("rentalOrdStat") Byte rentalOrdStat,
                                       @Param("rtnStat") Byte rtnStat,
                                       @Param("rtnRemark") String rtnRemark,
                                       @Param("rtnCompensation") BigDecimal rtnCompensation);


}

