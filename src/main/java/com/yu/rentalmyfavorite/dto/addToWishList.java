package com.yu.rentalmyfavorite.dto;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("AddToWishList")
public class AddToWishList {

    private Integer rentalNo;
    private Integer memNo;
    private String rentalFavTime;

    public void setRentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public void setRentalFavTime(String rentalFavTime) {
        this.rentalFavTime = rentalFavTime;
    }

}
