package com.Cia101G2.howard.rentalorder.dto;

import java.math.BigDecimal;

public class SetToCartRequest {

    private Integer memNo;
    private Integer rentalNo;
    private Integer rentalCatNo;
    private String rentalName;
    private BigDecimal rentalPrice;
    private Integer rentalSize;
    private String rentalColor;
    private String rentalInfo;
    private Byte rentalStat;

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public Integer getRentalNo() {
        return rentalNo;
    }

    public void setRentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public Integer getRentalCatNo() {
        return rentalCatNo;
    }

    public void setRentalCatNo(Integer rentalCatNo) {
        this.rentalCatNo = rentalCatNo;
    }

    public String getRentalName() {
        return rentalName;
    }

    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Integer getRentalSize() {
        return rentalSize;
    }

    public void setRentalSize(Integer rentalSize) {
        this.rentalSize = rentalSize;
    }

    public String getRentalColor() {
        return rentalColor;
    }

    public void setRentalColor(String rentalColor) {
        this.rentalColor = rentalColor;
    }

    public String getRentalInfo() {
        return rentalInfo;
    }

    public void setRentalInfo(String rentalInfo) {
        this.rentalInfo = rentalInfo;
    }

    public Byte getRentalStat() {
        return rentalStat;
    }

    public void setRentalStat(Byte rentalStat) {
        this.rentalStat = rentalStat;
    }

}
