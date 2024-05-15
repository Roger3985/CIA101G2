package com.howard.rentalorder.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class GetOrderOnAny {

    Integer rentalOrdNo;
    Integer memNo;
    String rentalByrName;
    String rentalByrPhone;
    String rentalByrEmail;
    String rentalRcvName;
    String rentalRcvPhone;
    Byte rentalTakeMethod;
    String rentalAddr;
    Byte rentalPayMethod;
    BigDecimal rentalAllPrice;
    BigDecimal rentalAllDepPrice;
    Timestamp rentalOrdTime;
    Timestamp rentalDate;
    Timestamp rentalBackDate;
    Timestamp rentalRealBackDate;
    Byte rentalPayStat;
    Byte rentalOrdStat;
    Byte rtnStat;
    String rtnRemark;
    BigDecimal rtnCompensation;

    public Integer getRentalOrdNo() {
        return rentalOrdNo;
    }

    public void setRentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public String getRentalByrName() {
        return rentalByrName;
    }

    public void setRentalByrName(String rentalByrName) {
        this.rentalByrName = rentalByrName;
    }

    public String getRentalByrPhone() {
        return rentalByrPhone;
    }

    public void setRentalByrPhone(String rentalByrPhone) {
        this.rentalByrPhone = rentalByrPhone;
    }

    public String getRentalByrEmail() {
        return rentalByrEmail;
    }

    public void setRentalByrEmail(String rentalByrEmail) {
        this.rentalByrEmail = rentalByrEmail;
    }

    public String getRentalRcvName() {
        return rentalRcvName;
    }

    public void setRentalRcvName(String rentalRcvName) {
        this.rentalRcvName = rentalRcvName;
    }

    public String getRentalRcvPhone() {
        return rentalRcvPhone;
    }

    public void setRentalRcvPhone(String rentalRcvPhone) {
        this.rentalRcvPhone = rentalRcvPhone;
    }

    public Byte getRentalTakeMethod() {
        return rentalTakeMethod;
    }

    public void setRentalTakeMethod(Byte rentalTakeMethod) {
        this.rentalTakeMethod = rentalTakeMethod;
    }

    public String getRentalAddr() {
        return rentalAddr;
    }

    public void setRentalAddr(String rentalAddr) {
        this.rentalAddr = rentalAddr;
    }

    public Byte getRentalPayMethod() {
        return rentalPayMethod;
    }

    public void setRentalPayMethod(Byte rentalPayMethod) {
        this.rentalPayMethod = rentalPayMethod;
    }

    public BigDecimal getRentalAllPrice() {
        return rentalAllPrice;
    }

    public void setRentalAllPrice(BigDecimal rentalAllPrice) {
        this.rentalAllPrice = rentalAllPrice;
    }

    public BigDecimal getRentalAllDepPrice() {
        return rentalAllDepPrice;
    }

    public void setRentalAllDepPrice(BigDecimal rentalAllDepPrice) {
        this.rentalAllDepPrice = rentalAllDepPrice;
    }

    public Timestamp getRentalOrdTime() {
        return rentalOrdTime;
    }

    public void setRentalOrdTime(Timestamp rentalOrdTime) {
        this.rentalOrdTime = rentalOrdTime;
    }

    public Timestamp getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Timestamp rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Timestamp getRentalBackDate() {
        return rentalBackDate;
    }

    public void setRentalBackDate(Timestamp rentalBackDate) {
        this.rentalBackDate = rentalBackDate;
    }

    public Timestamp getRentalRealBackDate() {
        return rentalRealBackDate;
    }

    public void setRentalRealBackDate(Timestamp rentalRealBackDate) {
        this.rentalRealBackDate = rentalRealBackDate;
    }

    public Byte getRentalPayStat() {
        return rentalPayStat;
    }

    public void setRentalPayStat(Byte rentalPayStat) {
        this.rentalPayStat = rentalPayStat;
    }

    public Byte getRentalOrdStat() {
        return rentalOrdStat;
    }

    public void setRentalOrdStat(Byte rentalOrdStat) {
        this.rentalOrdStat = rentalOrdStat;
    }

    public Byte getRtnStat() {
        return rtnStat;
    }

    public void setRtnStat(Byte rtnStat) {
        this.rtnStat = rtnStat;
    }

    public String getRtnRemark() {
        return rtnRemark;
    }

    public void setRtnRemark(String rtnRemark) {
        this.rtnRemark = rtnRemark;
    }

    public BigDecimal getRtnCompensation() {
        return rtnCompensation;
    }

    public void setRtnCompensation(BigDecimal rtnCompensation) {
        this.rtnCompensation = rtnCompensation;
    }
}
