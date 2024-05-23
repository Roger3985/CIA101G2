package com.iting.productorder.entity;

import com.chihyun.coupon.entity.Coupon;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.roger.member.entity.Member;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "productorder")
public class ProductOrder  implements Serializable {
    @Id
    @Column(name = "productordno", updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productOrdNo;
    @ManyToOne
    @JoinColumn(name = "memno", referencedColumnName = "memno",insertable = false, updatable = false)
    private Member member;

    @Column(name = "productbyrname")
//    @NotBlank(message = "訂購人姓名: 請勿空白")
//    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_\\s)]{4,40}$", message = "姓名: 只能是中、英文字母、數字、_ 和空格")
    private String productByrName;


    @Column(name = "productbyrphone")
//    @NotBlank(message = "訂購人手機: 請勿空白")
//    @Pattern(regexp = "^[0-9]{4,20}$", message = "只能是數字")
    private String productByrPhone;


    @Column(name = "productbyremail")
//    @Email(message="請填入正確信箱格式")
//    @NotBlank(message="訂購人信箱: 請勿空白")
    private String productByrEmail;

    @Column(name = "productrcvname")
//    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_\\s)]{4,40}$", message = "姓名: 只能是中、英文字母、數字、_ 和空格")
    private String productRcvName;

    @Column(name = "productrcvphone")
//    @Pattern(regexp = "^(|\\d{4,20})$", message = "只能是數字")
    private String productRcvPhone;

    @Column(name = "producttakemethod")
    private Byte productTakeMethod;

    @Column(name = "productaddr")
//    @Pattern(regexp = "^(|[(|\\u4e00-\\u9fa5)(a-zA-Z0-9_\\s)]{4,40})$", message = "地址: 只能是中、英文字母、數字、_ 和空格")
    private String productAddr;

    @Column(name = "productpaymethod")
    private Byte productPayMethod;

    @Column(name = "productallprice")
    private BigDecimal productAllPrice;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @JoinColumn(name = "coupno", referencedColumnName = "coupno")
    private Coupon coupon;


    @Column(name = "productdisc")
    private BigDecimal productDisc;

    @Column(name = "productrealprice")
    private BigDecimal productRealPrice;

    @Column(name = "productordtime")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp productOrdTime;


    @Column(name = "productordstat")
    private Byte productOrdStat;

    @Column(name = "productstat")
    private Byte productStat;

    @JsonBackReference
    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Set<ProductOrderDetail> productOrderDetails;
    @Column(name = "memno") // 对应数据库表中的列名，并指定insertable和updatable为false
    private Integer memNo; // 添加一个与数据库列对应的属性

    // 在getter和setter方法中添加
    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public ProductOrder() {
    }

    public ProductOrder(Integer productOrdNo) {
        this.productOrdNo = productOrdNo;
    }



    public ProductOrder(Integer productOrdNo, Member member, String productByrName, String productByrPhone, String productByrEmail, String productRcvName, String productRcvPhone, Byte productTakeMethod, String productAddr, Byte productPayMethod, BigDecimal productAllPrice, Coupon coupon, BigDecimal productDisc, BigDecimal productRealPrice, Timestamp productOrdTime, Byte productOrdStat, Byte productStat) {
        this.productOrdNo = productOrdNo;
        this.member = member;
        this.productByrName = productByrName;
        this.productByrPhone = productByrPhone;
        this.productByrEmail = productByrEmail;
        this.productRcvName = productRcvName;
        this.productRcvPhone = productRcvPhone;
        this.productTakeMethod = productTakeMethod;
        this.productAddr = productAddr;
        this.productPayMethod = productPayMethod;
        this.productAllPrice = productAllPrice;
        this.coupon = coupon;
        this.productDisc = productDisc;
        this.productRealPrice = productRealPrice;
        this.productOrdTime = productOrdTime;
        this.productOrdStat = productOrdStat;
        this.productStat = productStat;
    }

    public Integer getProductOrdNo() {
        return productOrdNo;
    }

    public void setProductOrdNo(Integer productOrdNo) {
        this.productOrdNo = productOrdNo;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getProductByrName() {
        return productByrName;
    }

    public void setProductByrName(String productByrName) {
        this.productByrName = productByrName;
    }

    public String getProductByrPhone() {
        return productByrPhone;
    }

    public void setProductByrPhone(String productByrPhone) {
        this.productByrPhone = productByrPhone;
    }

    public String getProductByrEmail() {
        return productByrEmail;
    }

    public void setProductByrEmail(String productByrEmail) {
        this.productByrEmail = productByrEmail;
    }

    public String getProductRcvName() {
        return productRcvName;
    }

    public void setProductRcvName(String productRcvName) {
        this.productRcvName = productRcvName;
    }

    public String getProductRcvPhone() {
        return productRcvPhone;
    }

    public void setProductRcvPhone(String productRcvPhone) {
        this.productRcvPhone = productRcvPhone;
    }

    public Byte getProductTakeMethod() {
        return productTakeMethod;
    }

    public void setProductTakeMethod(Byte productTakeMethod) {
        this.productTakeMethod = productTakeMethod;
    }

    public String getProductAddr() {
        return productAddr;
    }

    public void setProductAddr(String productAddr) {
        this.productAddr = productAddr;
    }

    public Byte getProductPayMethod() {
        return productPayMethod;
    }

    public void setProductPayMethod(Byte productPayMethod) {
        this.productPayMethod = productPayMethod;
    }

    public BigDecimal getProductAllPrice() {
        return productAllPrice;
    }

    public void setProductAllPrice(BigDecimal productAllPrice) {
        this.productAllPrice = productAllPrice;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getProductDisc() {
        if (getCoupon() != null && getCoupon().getCoupDisc() != null && getCoupon().getCoupNo() == 1) {
            return BigDecimal.ZERO;
        } else if (getCoupon() != null && getCoupon().getCoupDisc() != null) {
            BigDecimal discount = getCoupon().getCoupDisc().multiply(getProductAllPrice());
            return getProductAllPrice().subtract(discount);
        } else {
            return BigDecimal.ZERO;
        }
    }


    public void setProductDisc(BigDecimal productDisc) {
        this.productDisc = productDisc;
    }

    public BigDecimal getProductRealPrice() {
        if (this.getCoupon() != null && this.getCoupon().getCoupDisc() != null && this.getCoupon().getCoupNo() == 1) {
            return this.getProductAllPrice();
        } else if (this.getCoupon() != null && this.getCoupon().getCoupDisc() != null) {
            return this.getCoupon().getCoupDisc().multiply(this.getProductAllPrice());
        } else {
            return this.getProductAllPrice();
        }
    }

    public void setProductRealPrice(BigDecimal productRealPrice) {

        this.productRealPrice = productRealPrice;
    }

    public Timestamp getProductOrdTime() {
        return productOrdTime;
    }

    public void setProductOrdTime(Timestamp productOrdTime) {
        this.productOrdTime = productOrdTime;
    }

    public Byte getProductOrdStat() {
        return productOrdStat;
    }

    public void setProductOrdStat(Byte productOrdStat) {
        this.productOrdStat = productOrdStat;
    }

    public Byte getProductStat() {
        return productStat;
    }

    public void setProductStat(Byte productStat) {
        this.productStat = productStat;
    }

    public Set<ProductOrderDetail> getProductOrderDetails() {
        return productOrderDetails;
    }

    public void setProductOrderDetails(Set<ProductOrderDetail> productOrderDetails) {
        this.productOrderDetails = productOrderDetails;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "productOrdNo=" + productOrdNo +
                ", member=" + member +
                ", productByrName='" + productByrName + '\'' +
                ", productByrPhone='" + productByrPhone + '\'' +
                ", productByrEmail='" + productByrEmail + '\'' +
                ", productRcvName='" + productRcvName + '\'' +
                ", productRcvPhone='" + productRcvPhone + '\'' +
                ", productTakeMethod=" + productTakeMethod +
                ", productAddr='" + productAddr + '\'' +
                ", productPayMethod=" + productPayMethod +
                ", productAllPrice=" + productAllPrice +
                ", coupon=" + coupon +
                ", productDisc=" + productDisc +
                ", productRealPrice=" + productRealPrice +
                ", productOrdTime=" + productOrdTime +
                ", productOrdStat=" + productOrdStat +
                ", productStat=" + productStat +
                ", productOrderDetails=" + productOrderDetails +
                '}';
    }
}
