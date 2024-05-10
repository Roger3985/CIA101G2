package com.Cia101G2.howard.rentalmytrack.dto;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class UpdateTrack {

    private Integer rentalNo;
    private Integer memNo;
    @NotNull
    private Date expRentalDate;

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

    public Date getExpRentalDate() {
        return expRentalDate;
    }

    public void setExpRentalDate(Date expRentalDate) {
        this.expRentalDate = expRentalDate;
    }

}
