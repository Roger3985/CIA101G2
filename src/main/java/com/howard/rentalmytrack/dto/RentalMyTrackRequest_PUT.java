package com.howard.rentalmytrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

public class RentalMyTrackRequest_PUT {

    @NotBlank
    private Integer rentalNo; // -> 租借品編號
    @NotBlank
    private Integer memNo; // -> 會員編號
    private Timestamp rTrackTime; // -> 加入追蹤時間
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 讓請求的 json 資料符合 sql.Date 格式
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

    public Timestamp getrTrackTime() {
        return rTrackTime;
    }

    public void setrTrackTime(Timestamp rTrackTime) {
        this.rTrackTime = rTrackTime;
    }

    public Date getExpRentalDate() {
        return expRentalDate;
    }

    public void setExpRentalDate(Date expRentalDate) {
        this.expRentalDate = expRentalDate;
    }

}
