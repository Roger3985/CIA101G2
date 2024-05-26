package com.chihyun.coupon.entity;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iting.productorder.entity.ProductOrder;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
@Component
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupno")
    private Integer coupNo;

    @NotBlank(message = "優惠券名稱: 請勿空白")
    @Column(name = "coupname")
    private String coupName;

    @NotBlank(message = "發放條件: 請勿空白")
    @Column(name = "coupcond")
    private String coupCond;

    @NotNull(message = "折扣:請勿空白，請填寫0~1之間小數")
    @Digits(integer = 3, fraction = 2)
    @DecimalMax(value = "0.99", message = "最大值不得高於{value}")
    @DecimalMin(value = "0.01", message = "最小值不得小於{value}")
    @Column(name = "coupdisc")
    private BigDecimal coupDisc;

    @Column(name = "coupadddate")
    private Timestamp coupAddDate;

    @NotNull(message = "失效日期: 請勿空白")
//    @Future(message = "優惠券失效日期: 不得早於今天日期")
    @Column(name = "coupexpdate")
    private Timestamp coupExpDate;

    @NotNull(message = "發放日期: 請勿空白")
//    @Future(message = "優惠券發放日期: 不得早於今天日期")
    @Column(name = "coupreldate")
    private Timestamp coupRelDate;

    @Column(name = "couprelstat")
    private Byte coupRelStat;

    @NotBlank(message = "優惠資訊: 請勿空白")
    @Column(name = "coupinfo")
    private String coupInfo;

    @JsonBackReference
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private Set<ProductOrder> productOrders;
    @JsonBackReference
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private Set<MyCoupon> myCoupons;

    public Integer getCoupNo() {
        return coupNo;
    }

    public void setCoupNo(Integer coupNo) {
        this.coupNo = coupNo;
    }

    public String getCoupName() {
        return coupName;
    }

    public void setCoupName(String coupName) {
        this.coupName = coupName;
    }

    public String getCoupCond() {
        return coupCond;
    }

    public void setCoupCond(String coupCond) {
        this.coupCond = coupCond;
    }

    public BigDecimal getCoupDisc() {
        return coupDisc;
    }

    public void setCoupDisc(BigDecimal coupDisc) {
        this.coupDisc = coupDisc;
    }

    public Timestamp getCoupAddDate() {
        return coupAddDate;
    }

    public void setCoupAddDate(Timestamp coupAddDate) {
        this.coupAddDate = coupAddDate;
    }

    public Timestamp getCoupExpDate() {
        return coupExpDate;
    }

    public void setCoupExpDate(Timestamp coupExpDate) {
        this.coupExpDate = coupExpDate;
    }

    public Timestamp getCoupRelDate() {
        return coupRelDate;
    }

    public void setCoupRelDate(Timestamp coupRelDate) {
        this.coupRelDate = coupRelDate;
    }

    public Byte getCoupRelStat() {
        return coupRelStat;
    }

    public void setCoupRelStat(Byte coupRelStat) {
        this.coupRelStat = coupRelStat;
    }

    public String getCoupInfo() {
        return coupInfo;
    }

    public void setCoupInfo(String coupInfo) {
        this.coupInfo = coupInfo;
    }

    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    public Set<MyCoupon> getMyCoupons() {
        return myCoupons;
    }

    public void setMyCoupons(Set<MyCoupon> myCoupons) {
        this.myCoupons = myCoupons;
    }
}
