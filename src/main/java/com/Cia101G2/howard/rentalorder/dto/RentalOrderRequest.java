package com.Cia101G2.howard.rentalorder.dto;

import com.Cia101G2.CustomTimestampDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class RentalOrderRequest {

    private Integer rentalOrdNo; // -> 租借訂單編號
    @NotNull
    private Integer memNo; // -> 會員編號
    @NotBlank
    private String rentalByrName; // -> 訂購人姓名
    @NotBlank
    private String rentalByrPhone; // -> 訂購人手機號碼
    @NotBlank
    @Email
    private String rentalByrEmail; // -> 訂購人Email
    private String rentalRcvName; // -> 收件人姓名
    private String rentalRcvPhone; // -> 收件人手機號碼
    @NotNull
    private byte rentalTakeMethod; // -> 取貨方式
    private String rentalAddr; // -> 宅配住址
    @NotNull
    private byte rentalPayMethod; // -> 付款方式
    @NotNull
    private BigDecimal rentalAllPrice; // -> 訂單總金額
    @NotNull
    private BigDecimal rentalAllDepPrice; // -> 押金總金額
    private Timestamp rentalOrdTime; // -> 下單時間

    @JsonDeserialize(using = CustomTimestampDeserializer.class)
    private Timestamp rentalDate; // -> 預計租借日期
    private Byte rentSet; // -> 租借方案
    private Timestamp rentalBackDate; // -> 預計歸還日期
    private Timestamp rentalRealBackDate; // -> 實際歸還日期

    private byte rentalPayStat; // -> 付款狀態

    private byte rentalOrdStat; // -> 訂單狀態

    private byte rtnStat; // -> 歸還狀態
    private String rtnRemark; // -> 歸還註記
    private BigDecimal rtnCompensation; // -> 賠償金額

    @NotEmpty
    private List<String> buyItems; // -> 購買明細



    /*--------------------------getter、setter-----------------------------*/



    public Integer getrentalOrdNo() {
        return rentalOrdNo;
    }

    public void setrentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public String getrentalByrName() {
        return rentalByrName;
    }

    public void setrentalByrName(String rentalByrName) {
        this.rentalByrName = rentalByrName;
    }

    public String getrentalByrPhone() {
        return rentalByrPhone;
    }

    public void setrentalByrPhone(String rentalByrPhone) {
        this.rentalByrPhone = rentalByrPhone;
    }

    public String getrentalByrEmail() {
        return rentalByrEmail;
    }

    public void setrentalByrEmail(String rentalByrEmail) {
        this.rentalByrEmail = rentalByrEmail;
    }

    public String getrentalRcvName() {
        return rentalRcvName;
    }

    public void setrentalRcvName(String rentalRcvName) {
        this.rentalRcvName = rentalRcvName;
    }

    public String getrentalRcvPhone() {
        return rentalRcvPhone;
    }

    public void setrentalRcvPhone(String rentalRcvPhone) {
        this.rentalRcvPhone = rentalRcvPhone;
    }

    public byte getrentalTakeMethod() {
        return rentalTakeMethod;
    }

    public void setrentalTakeMethod(byte rentalTakeMethod) {
        this.rentalTakeMethod = rentalTakeMethod;
    }

    public String getrentalAddr() {
        return rentalAddr;
    }

    public void setrentalAddr(String rentalAddr) {
        this.rentalAddr = rentalAddr;
    }

    public byte getrentalPayMethod() {
        return rentalPayMethod;
    }

    public void setrentalPayMethod(byte rentalPayMethod) {
        this.rentalPayMethod = rentalPayMethod;
    }

    public BigDecimal getrentalAllPrice() {
        return rentalAllPrice;
    }

    public void setrentalAllPrice(BigDecimal rentalAllPrice) {
        this.rentalAllPrice = rentalAllPrice;
    }

    public BigDecimal getrentalAllDepPrice() {
        return rentalAllDepPrice;
    }

    public void setrentalAllDepPrice(BigDecimal rentalAllDepPrice) {
        this.rentalAllDepPrice = rentalAllDepPrice;
    }

    public Timestamp getrentalOrdTime() {
        return rentalOrdTime;
    }

    public void setrentalOrdTime(Timestamp rentalOrdTime) {
        this.rentalOrdTime = rentalOrdTime;
    }

    public Timestamp getrentalDate() {
        return rentalDate;
    }

    public void setrentalDate(Timestamp rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Timestamp getrentalBackDate() {
        return rentalBackDate;
    }

    public void setrentalBackDate(Timestamp rentalBackDate) {
        this.rentalBackDate = rentalBackDate;
    }

    public Timestamp getrentalRealBackDate() {
        return rentalRealBackDate;
    }

    public void setrentalRealBackDate(Timestamp rentalRealBackDate) {
        this.rentalRealBackDate = rentalRealBackDate;
    }

    public byte getrentalPayStat() {
        return rentalPayStat;
    }

    public void setrentalPayStat(byte rentalPayStat) {
        this.rentalPayStat = rentalPayStat;
    }

    public byte getrentalOrdStat() {
        return rentalOrdStat;
    }

    public void setrentalOrdStat(byte rentalOrdStat) {
        this.rentalOrdStat = rentalOrdStat;
    }

    public byte getRtnStat() {
        return rtnStat;
    }

    public void setRtnStat(byte rtnStat) {
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

    public List<String> getBuyItems() {
        return buyItems;
    }

    public void setBuyItems(List<String> buyItems) {
        this.buyItems = buyItems;
    }

    public Byte getRentSet() {
        return rentSet;
    }

    public void setRentSet(Byte rentSet) {
        this.rentSet = rentSet;
    }

}
