package com.ren.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iting.cart.entity.Cart;
import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.ren.productcategory.entity.ProductCategory;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productreview.entity.ProductReview;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
    @NotNull(message="請選擇商品尺寸")
    @Min(value = 0, message = "商品尺寸只能是XS、S、M、L、XL、2L")
    @Max(value = 5, message = "商品尺寸只能是XS、S、M、L、XL、2L")
    @Column(name = "productsize")
    private Integer productSize;
    @NotEmpty(message="商品顏色: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "商品顏色: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    @Column(name = "productcolor")
    private String productColor;
    @NotEmpty(message="商品價格: 請勿空白")
    @DecimalMin(value = "100.00", message = "商品價格: 不能小於{value}")
    @DecimalMax(value = "99999.99", message = "商品價格: 不能超過{value}")
    @Column(name = "productprice")
    private BigDecimal productPrice;
    @NotNull(message="請選擇商品狀態")
    @Column(name = "productstat")
    private Byte productStat;
    @NotNull(message = "請輸入商品售出數量")
    @Min(value = 0, message = "請輸入商品售出數量")
    @Column(name = "productsalqty")
    private Integer productSalQty;
    @NotNull(message = "請輸入評價人數")
    @Min(value = 0, message = "請輸入評價人數")
    @Column(name = "productcompeople")
    private Integer productComPeople;
    @NotNull(message = "請輸入商品評價分數")
    @Digits(integer = 1, fraction = 2)
    @DecimalMin(value = "0.01", message = "評分請填0.00 ~ 5.00的分數")
    @DecimalMax(value = "5.00", message = "評分請填0.00 ~ 5.00的分數")
    @Column(name = "productcomscore")
    private BigDecimal productComScore;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "上架時間不能為過去時間")
    @Column(name = "productonshelf")
    private Timestamp productOnShelf;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "下架時間不能為過去時間")
    @Column(name = "productoffshelf")
    private Timestamp productOffShelf;

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
    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductReview> productReviews;

    public Product() {
    }

    public Product(Integer productNo) {
        this.productNo = productNo;
    }

    public Product(ProductCategory productCategory, String productName, String productInfo, Integer productSize, String productColor, BigDecimal productPrice, Byte productStat, Integer productSalQty, Integer productComPeople, BigDecimal productComScore) {
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

    public Product(Integer productNo, ProductCategory productCategory, String productName, String productInfo, Integer productSize, String productColor, BigDecimal productPrice, Byte productStat, Integer productSalQty, Integer productComPeople, BigDecimal productComScore) {
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

    public Product(ProductCategory productCategory, String productName, String productInfo, Integer productSize, String productColor, BigDecimal productPrice, Byte productStat, Integer productSalQty, Integer productComPeople, BigDecimal productComScore, Timestamp productOnShelf, Timestamp productOffShelf) {
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
        this.productOnShelf = productOnShelf;
        this.productOffShelf = productOffShelf;
    }

    public Product(Integer productNo, ProductCategory productCategory, String productName, String productInfo, Integer productSize, String productColor, BigDecimal productPrice, Byte productStat, Integer productSalQty, Integer productComPeople, BigDecimal productComScore, Timestamp productOnShelf, Timestamp productOffShelf) {
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
        this.productOnShelf = productOnShelf;
        this.productOffShelf = productOffShelf;
    }

    public Timestamp getProductOnShelf() {
        return productOnShelf;
    }

    public void setProductOnShelf(Timestamp productOnShelf) {
        this.productOnShelf = productOnShelf;
    }

    public Timestamp getProductOffShelf() {
        return productOffShelf;
    }

    public void setProductOffShelf(Timestamp productOffShelf) {
        this.productOffShelf = productOffShelf;
    }

    public Set<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(Set<ProductReview> productReviews) {
        this.productReviews = productReviews;
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

    public BigDecimal getProductComScore() {
        return productComScore;
    }

    public void setProductComScore(BigDecimal productComScore) {
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