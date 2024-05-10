package com.howard.rentalmytrack.dto;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

public class RentalMyTrackRequest {

    @NotBlank
    private Integer rentalNo; // -> 租借品編號
    @NotBlank
    private Integer memNo; // -> 會員編號
    private Timestamp rentalTrackTime; // -> 加入追蹤時間
    private Date expRentalDate; // -> 期望租借日期

    public Integer getrentalNo() {
        return rentalNo;
    }

    public void setrentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public Timestamp getrentalTrackTime() {
        return rentalTrackTime;
    }

    public void setrentalTrackTime(Timestamp rentalTrackTime) {
        this.rentalTrackTime = rentalTrackTime;
    }

    public Date getExpRentalDate() {
        return expRentalDate;
    }

    public void setExpRentalDate(Date expRentalDate) {
        this.expRentalDate = expRentalDate;
    }

}
