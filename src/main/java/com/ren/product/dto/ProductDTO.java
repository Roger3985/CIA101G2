package com.ren.product.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productID")
public class ProductDTO {
    private String productID;
    private Integer productCatNo;
    private String productCatName;
    private String productName;
    private String productInfo;
    private Integer productScorePeople;
    private Double productScore;
    private ArrayList<Integer> productNoList;
    private ArrayList<Integer> productPicNoList;
    private HashSet<BigDecimal> productPriceSet;
    private HashSet<Integer> productSizeSet;
    private HashSet<String> productColorSet;
    private HashSet<Timestamp> productOnShelfSet;

    public ProductDTO() {
    }

    public ProductDTO(String productID) {
        this.productID = productID;
    }

    public ProductDTO(Integer productCatNo, String productCatName, String productName, String productInfo, Integer productScorePeople, Double productScore, ArrayList<Integer> productNoList, ArrayList<Integer> productPicNoList, HashSet<BigDecimal> productPriceSet, HashSet<Integer> productSizeSet, HashSet<String> productColorSet, HashSet<Timestamp> productOnShelfSet) {
        this.productCatNo = productCatNo;
        this.productCatName = productCatName;
        this.productName = productName;
        this.productInfo = productInfo;
        this.productScorePeople = productScorePeople;
        this.productScore = productScore;
        this.productNoList = productNoList;
        this.productPicNoList = productPicNoList;
        this.productPriceSet = productPriceSet;
        this.productSizeSet = productSizeSet;
        this.productColorSet = productColorSet;
        this.productOnShelfSet = productOnShelfSet;
    }

    public ProductDTO(String productID, Integer productCatNo, String productCatName, String productName, String productInfo, Integer productScorePeople, Double productScore, ArrayList<Integer> productNoList, ArrayList<Integer> productPicNoList, HashSet<BigDecimal> productPriceSet, HashSet<Integer> productSizeSet, HashSet<String> productColorSet, HashSet<Timestamp> productOnShelfSet) {
        this.productID = productID;
        this.productCatNo = productCatNo;
        this.productCatName = productCatName;
        this.productName = productName;
        this.productInfo = productInfo;
        this.productScorePeople = productScorePeople;
        this.productScore = productScore;
        this.productNoList = productNoList;
        this.productPicNoList = productPicNoList;
        this.productPriceSet = productPriceSet;
        this.productSizeSet = productSizeSet;
        this.productColorSet = productColorSet;
        this.productOnShelfSet = productOnShelfSet;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getProductCatNo() {
        return productCatNo;
    }

    public void setProductCatNo(Integer productCatNo) {
        this.productCatNo = productCatNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public HashSet<BigDecimal> getProductPriceSet() {
        return productPriceSet;
    }

    public void setProductPriceSet(HashSet<BigDecimal> productPriceSet) {
        this.productPriceSet = productPriceSet;
    }

    public HashSet<Integer> getProductSizeSet() {
        return productSizeSet;
    }

    public void setProductSizeSet(HashSet<Integer> productSizeSet) {
        this.productSizeSet = productSizeSet;
    }

    public HashSet<String> getProductColorSet() {
        return productColorSet;
    }

    public void setProductColorSet(HashSet<String> productColorSet) {
        this.productColorSet = productColorSet;
    }

    public HashSet<Timestamp> getProductOnShelfSet() {
        return productOnShelfSet;
    }

    public void setProductOnShelfSet(HashSet<Timestamp> productOnShelfSet) {
        this.productOnShelfSet = productOnShelfSet;
    }

    public String getProductCatName() {
        return productCatName;
    }

    public void setProductCatName(String productCatName) {
        this.productCatName = productCatName;
    }

    public Double getProductScore() {
        return productScore;
    }

    public void setProductScore(Double productScore) {
        this.productScore = productScore;
    }

    public Integer getProductScorePeople() {
        return productScorePeople;
    }

    public void setProductScorePeople(Integer productScorePeople) {
        this.productScorePeople = productScorePeople;
    }

    public ArrayList<Integer> getProductNoList() {
        return productNoList;
    }

    public void setProductNoList(ArrayList<Integer> productNoList) {
        this.productNoList = productNoList;
    }

    public ArrayList<Integer> getProductPicNoList() {
        return productPicNoList;
    }

    public void setProductPicNoList(ArrayList<Integer> productPicNoList) {
        this.productPicNoList = productPicNoList;
    }
}
