package com.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @NotEmpty(message="商品名稱: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "商品名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    @Column(name = "productname")
    private String productName;
    @NotEmpty(message="商品資訊: 請勿空白")
    @Column(name = "productinfo")
    private String productInfo;
    @Column(name = "productsize")
    private Integer productSize;
    @NotEmpty(message="商品顏色: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "商品顏色: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    @Column(name = "productcolor")
    private String productColor;
    @NotNull(message="商品價格: 請勿空白")
    @DecimalMin(value = "100.00", message = "商品價格: 不能小於{value}")
    @DecimalMax(value = "99999.99", message = "商品價格: 不能超過{value}")
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

    public Product(Integer productNo) {
        this.productNo = productNo;
    }

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

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
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

    public Byte getProductStat() {
        return productStat;
    }

    public void setProductStat(Byte productStat) {
        this.productStat = productStat;
    }

    public Integer getProductSalQty() {
        return productSalQty;
    }

    public void setProductSalQty(Integer productSalQty) {
        this.productSalQty = productSalQty;
    }

    public Integer getProductComPeople() {
        return productComPeople;
    }

    public void setProductComPeople(Integer productComPeople) {
        this.productComPeople = productComPeople;
    }

    public Integer getProductComScore() {
        return productComScore;
    }

    public void setProductComScore(Integer productComScore) {
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