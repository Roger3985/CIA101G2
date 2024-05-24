package com.yu.rentalmyfavorite.dto;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("AddToWishList")
public class AddToWishList {

    private Integer rentalNo;
    private Integer memNo;
    private String rentalFavTime;

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

    public String getRentalFavTime() {
        return rentalFavTime;
    }

    public void setRentalFavTime(String rentalFavTime) {
        this.rentalFavTime = rentalFavTime;
    }

    public AddToWishList(Integer rentalNo, Integer memNo, String rentalFavTime) {
        this.rentalNo = rentalNo;
        this.memNo = memNo;
        this.rentalFavTime = rentalFavTime;
    }
}
