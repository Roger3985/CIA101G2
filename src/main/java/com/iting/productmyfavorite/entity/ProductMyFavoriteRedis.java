package com.iting.productmyfavorite.entity;

import com.ren.product.entity.Product;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("ProductMyFavoriteRedis")
public class ProductMyFavoriteRedis {
    private Integer productNo;
    private Integer memNo;
    private String FavTime;

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public String getFavTime() {
        return FavTime;
    }

    public void setFavTime(String favTime) {
        FavTime = favTime;
    }
}