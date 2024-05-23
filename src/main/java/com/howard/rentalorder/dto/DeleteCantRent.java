package com.howard.rentalorder.dto;

import java.util.List;

public class DeleteCantRent {

    List<Integer> rentalNos;
    Integer memNo;

    public List<Integer> getRentalNos() {
        return rentalNos;
    }

    public void setRentalNos(List<Integer> rentalNos) {
        this.rentalNos = rentalNos;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

}
