package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;


@Entity
@Table(name = "productorder")
public class ProductOrder {
    @Id
    @Column(name = "productordno", updatable = false)
    private Integer productOrdNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member;
    @Column(name = "productbyrname")
    private String productByrName;
    @Column(name = "productbyrphone")
    private Integer productByrPhone;
    @Column(name = "productbyremail")
    private String productByrEmail;
    @Column(name = "productrcvname")
    private String productRcvName;
    @Column(name = "productrcvphone")
    private String productRcvPhone;
    @Column(name = "producttakemethod")
    private Byte productTakeMethod;
    @Column(name = "productaddr")
    private String productAddr;
    @Column(name = "productpaymethod")
    private Byte productPayMethod;
    @Column(name = "productallprice")
    private BigDecimal productAllPrice;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "coupno", referencedColumnName = "coupno")
    private Coupon coupon;
    @Column(name = "productdisc")
    private BigDecimal productDisc;
    @Column(name = "productrealprice")
    private BigDecimal productRealPrice;
    @Column(name = "productordtime")
    private Timestamp productOrdTime;
    @Column(name = "productordstat")
    private Byte productOrdStat;
    @Column(name = "productstat")
    private Byte productStat;
    @JsonBackReference
    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Set<ProductOrderDetail> productOrderDetails;

    public Integer getproductOrdNo() {
        return productOrdNo;
    }

    public void setproductOrdNo(Integer productOrdNo) {
        this.productOrdNo = productOrdNo;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getproductByrName() {
        return productByrName;
    }

    public void setproductByrName(String productByrName) {
        this.productByrName = productByrName;
    }

    public Integer getproductByrPhone() {
        return productByrPhone;
    }

    public void setproductByrPhone(Integer productByrPhone) {
        this.productByrPhone = productByrPhone;
    }

    public String getproductByrEmail() {
        return productByrEmail;
    }

    public void setproductByrEmail(String productByrEmail) {
        this.productByrEmail = productByrEmail;
    }

    public String getproductRcvName() {
        return productRcvName;
    }

    public void setproductRcvName(String productRcvName) {
        this.productRcvName = productRcvName;
    }

    public String getproductRcvPhone() {
        return productRcvPhone;
    }

    public void setproductRcvPhone(String productRcvPhone) {
        this.productRcvPhone = productRcvPhone;
    }

    public Byte getproductTakeMethod() {
        return productTakeMethod;
    }

    public void setproductTakeMethod(Byte productTakeMethod) {
        this.productTakeMethod = productTakeMethod;
    }

    public String getproductAddr() {
        return productAddr;
    }

    public void setproductAddr(String productAddr) {
        this.productAddr = productAddr;
    }

    public Byte getproductPayMethod() {
        return productPayMethod;
    }

    public void setproductPayMethod(Byte productPayMethod) {
        this.productPayMethod = productPayMethod;
    }

    public BigDecimal getproductAllPrice() {
        return productAllPrice;
    }

    public void setproductAllPrice(BigDecimal productAllPrice) {
        this.productAllPrice = productAllPrice;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getproductDisc() {
        return productDisc;
    }

    public void setproductDisc(BigDecimal productDisc) {
        this.productDisc = productDisc;
    }

    public BigDecimal getproductRealPrice() {
        return productRealPrice;
    }

    public void setproductRealPrice(BigDecimal productRealPrice) {
        this.productRealPrice = productRealPrice;
    }

    public Timestamp getproductOrdTime() {
        return productOrdTime;
    }

    public void setproductOrdTime(Timestamp productOrdTime) {
        this.productOrdTime = productOrdTime;
    }

    public Byte getproductOrdStat() {
        return productOrdStat;
    }

    public void setproductOrdStat(Byte productOrdStat) {
        this.productOrdStat = productOrdStat;
    }

    public Byte getproductStat() {
        return productStat;
    }

    public void setproductStat(Byte productStat) {
        this.productStat = productStat;
    }

    public Set<ProductOrderDetail> getProductOrderDetails() {
        return productOrderDetails;
    }

    public void setProductOrderDetails(Set<ProductOrderDetail> productOrderDetails) {
        this.productOrderDetails = productOrderDetails;
    }
}
