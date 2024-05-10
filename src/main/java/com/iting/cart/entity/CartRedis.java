package com.iting.cart.entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;

@RedisHash("CartRedis")
public class CartRedis implements Serializable {
    @Id
    private String id; // Redis中的键
    private Integer productNo;
    private Integer memNo;
    private Integer productBuyQty;
    private String productName;
    private Integer productSize;
    private String productColor;
    private BigDecimal productPrice;

    public CartRedis() {
    }

    public CartRedis(Integer productNo, Integer memNo, Integer productBuyQty, String productName, Integer productSize, String productColor, BigDecimal productPrice) {
        this.productNo = productNo;
        this.memNo = memNo;
        this.productBuyQty = productBuyQty;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productPrice = productPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 根据用户ID和产品ID生成唯一的Redis键
    public String generateId(Integer productNo, Integer memNo) {
        return "cart:" + memNo + ":" + productNo;
    }

    public String getId() {
        return id;
    }

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

    public Integer getProductBuyQty() {
        return productBuyQty;
    }

    public void setProductBuyQty(Integer productBuyQty) {
        this.productBuyQty = productBuyQty;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductSize() {
        return productSize;
    }

    public void setProductSize(Integer productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "CartRedis{" +
                "id='" + id + '\'' +
                ", productNo=" + productNo +
                ", memNo=" + memNo +
                ", productBuyQty=" + productBuyQty +
                ", productName='" + productName + '\'' +
                ", productSize=" + productSize +
                ", productColor='" + productColor + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
