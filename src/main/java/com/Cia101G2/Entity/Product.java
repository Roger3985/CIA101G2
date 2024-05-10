package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productno")
    private Integer productNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productcatno", referencedColumnName = "productcatno")
    private ProductCategory productCategory;
    @Column(name = "productname")
    private String productName;
    @Column(name = "productinfo")
    private String productInfo;
    @Column(name = "productsize")
    private Integer productSize;
    @Column(name = "productcolor")
    private String productColor;
    @Column(name = "productprice")
    private BigDecimal productPrice;
    @Column(name = "productstat")
    private Byte productStat;
    @Column(name = "productsalqty")
    private Integer productSalQty;
    @Column(name = "productcompeople")
    private Integer productComPeople;
    @Column(name = "productcomscore")
    private Integer productComScore;
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductOrderDetail> productOrderDetails;
    @JsonBackReference
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<ProductMyFavorite> productMyFavorites;
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Cart> carts;
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductPicture> productPictures;

    public Product() {

    }

    // insert用建構子
    public Product(ProductCategory productCategory, String productName, String productInfo, Integer productSize, String productColor, BigDecimal productPrice, Byte productStat, Integer productSalQty, Integer productComPeople, Integer productComScore) {
        this.productCategory = productCategory;
        this.productName = productName;
        this.productInfo = productInfo;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.productStat = productStat;
        this.productSalQty = productSalQty;
        this.productComPeople = productComPeople;
        this.productComScore = productComScore;
    }

    // Update用建構子
    public Product(Integer productNo, ProductCategory productCategory, String productName, String productInfo, Integer productSize, String productColor, BigDecimal productPrice, Byte productStat, Integer productSalQty, Integer productComPeople, Integer productComScore) {
        this.productNo = productNo;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productInfo = productInfo;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.productStat = productStat;
        this.productSalQty = productSalQty;
        this.productComPeople = productComPeople;
        this.productComScore = productComScore;
    }

    public Integer getproductNo() {
        return productNo;
    }

    public void setproductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getproductInfo() {
        return productInfo;
    }

    public void setproductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public Integer getproductSize() {
        return productSize;
    }

    public void setproductSize(Integer productSize) {
        this.productSize = productSize;
    }

    public String getproductColor() {
        return productColor;
    }

    public void setproductColor(String productColor) {
        this.productColor = productColor;
    }

    public BigDecimal getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Byte getproductStat() {
        return productStat;
    }

    public void setproductStat(Byte productStat) {
        this.productStat = productStat;
    }

    public Integer getproductSalQty() {
        return productSalQty;
    }

    public void setproductSalQty(Integer productSalQty) {
        this.productSalQty = productSalQty;
    }

    public Integer getproductComPeople() {
        return productComPeople;
    }

    public void setproductComPeople(Integer productComPeople) {
        this.productComPeople = productComPeople;
    }

    public Integer getproductComScore() {
        return productComScore;
    }

    public void setproductComScore(Integer productComScore) {
        this.productComScore = productComScore;
    }

    public Set<ProductOrderDetail> getProductOrderDetails() {
        return productOrderDetails;
    }

    public void setProductOrderDetails(Set<ProductOrderDetail> productOrderDetails) {
        this.productOrderDetails = productOrderDetails;
    }

    public Set<ProductMyFavorite> getProductMyFavorites() {
        return productMyFavorites;
    }

    public void setProductMyFavorites(Set<ProductMyFavorite> productMyFavorites) {
        this.productMyFavorites = productMyFavorites;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public Set<ProductPicture> getProductPictures() {
        return productPictures;
    }

    public void setProductPictures(Set<ProductPicture> productPictures) {
        this.productPictures = productPictures;
    }
}