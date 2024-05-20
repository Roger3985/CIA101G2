package com.howard.rentalorder.dto;

import java.util.List;

public class SetToCart {

    private Integer rentalNo;
    private Integer memNo;

    // 給再租一次用的
    private List<Integer> rentalNos;

    public Integer getRentalNo() {
        return rentalNo;
    }

    public void setRentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public List<Integer> getRentalNos() {
        return rentalNos;
    }

    public void setRentalNos(List<Integer> rentalNos) {
        this.rentalNos = rentalNos;
    }

}
