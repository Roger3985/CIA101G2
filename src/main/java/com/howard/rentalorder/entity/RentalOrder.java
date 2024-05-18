package com.howard.rentalorder.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.howard.rentalorderdetails.entity.RentalOrderDetails;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "rentalorder")
public class RentalOrder implements Serializable {

    public RentalOrder() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalordno", updatable = false)
    private Integer rentalOrdNo; // -> 租借品訂單編號
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member;
    @Column(name = "rentalbyrname")
    private String rentalByrName; // -> 訂購人姓名
    @Column(name = "rentalbyrphone")
    private String rentalByrPhone; // -> 訂購人手機號碼
    @Column(name = "rentalbyremail")
    private String rentalByrEmail; // -> 訂購人Email
    @Column(name = "rentalrcvname")
    private String rentalRcvName; // -> 收件人姓名
    @Column(name = "rentalrcvphone")
    private String rentalRcvPhone; // -> 收件人手機號碼
    @Column(name = "rentaltakemethod")
    private byte rentalTakeMethod; // -> 取貨方式
    @Column(name = "rentaladdr")
    private String rentalAddr; // -> 宅配住址
    @Column(name = "rentalpaymethod")
    private byte rentalPayMethod; // -> 付款方式
    @Column(name = "rentalallprice")
    private BigDecimal rentalAllPrice; // -> 訂單總金額
    @Column(name = "rentalalldepprice")
    private BigDecimal rentalAllDepPrice; // -> 押金總金額
    @Column(name = "rentalordtime")
    private Timestamp rentalOrdTime; // -> 下單時間
    @Column(name = "rentaldate")
    private Timestamp rentalDate; // -> 預計租借日期
    @Column(name = "rentalbackdate")
    private Timestamp rentalBackDate; // -> 預計歸還日期
    @Column(name = "rentalrealbackdate")
    private Timestamp rentalRealBackDate; // -> 實際歸還日期
    @Column(name = "rentalpaystat")
    private byte rentalPayStat; // -> 付款狀態
    @Column(name = "rentalordstat")
    private byte rentalOrdStat; // -> 訂單狀態
    @Column(name = "rtnstat")
    private byte rtnStat; // -> 歸還狀態
    @Column(name = "rtnremark")
    private String rtnRemark; // -> 歸還註記
    @Column(name = "rtncompensation")
    private BigDecimal rtnCompensation; // -> 賠償金額
    @JsonBackReference
    @OneToMany(mappedBy = "rentalOrder", cascade = CascadeType.ALL)
    private Set<RentalOrderDetails> rentalOrderDetailses;

    /*----------------------getter、setter--------------------------*/

    public Integer getrentalOrdNo() {
        return rentalOrdNo;
    }

    public void setrentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
    }

    public String getrentalByrName() {
        return rentalByrName;
    }

    public void setrentalByrName(String rentalByrName) {
        this.rentalByrName = rentalByrName;
    }

    public String getrentalByrPhone() {
        return rentalByrPhone;
    }

    public void setrentalByrPhone(String rentalByrPhone) {
        this.rentalByrPhone = rentalByrPhone;
    }

    public String getrentalByrEmail() {
        return rentalByrEmail;
    }

    public void setrentalByrEmail(String rentalByrEmail) {
        this.rentalByrEmail = rentalByrEmail;
    }

    public String getrentalRcvName() {
        return rentalRcvName;
    }

    public void setrentalRcvName(String rentalRcvName) {
        this.rentalRcvName = rentalRcvName;
    }

    public String getrentalRcvPhone() {
        return rentalRcvPhone;
    }

    public void setrentalRcvPhone(String rentalRcvPhone) {
        this.rentalRcvPhone = rentalRcvPhone;
    }

    public byte getrentalTakeMethod() {
        return rentalTakeMethod;
    }

    public void setrentalTakeMethod(byte rentalTakeMethod) {
        this.rentalTakeMethod = rentalTakeMethod;
    }

    public String getrentalAddr() {
        return rentalAddr;
    }

    public void setrentalAddr(String rentalAddr) {
        this.rentalAddr = rentalAddr;
    }

    public byte getrentalPayMethod() {
        return rentalPayMethod;
    }

    public void setrentalPayMethod(byte rentalPayMethod) {
        this.rentalPayMethod = rentalPayMethod;
    }

    public BigDecimal getrentalAllPrice() {
        return rentalAllPrice;
    }

    public void setrentalAllPrice(BigDecimal rentalAllPrice) {
        this.rentalAllPrice = rentalAllPrice;
    }

    public BigDecimal getrentalAllDepPrice() {
        return rentalAllDepPrice;
    }

    public void setrentalAllDepPrice(BigDecimal rentalAllDepPrice) {
        this.rentalAllDepPrice = rentalAllDepPrice;
    }

    public Timestamp getrentalOrdTime() {
        return rentalOrdTime;
    }

    public void setrentalOrdTime(Timestamp rentalOrdTime) {
        this.rentalOrdTime = rentalOrdTime;
    }

    public Timestamp getrentalDate() {
        return rentalDate;
    }

    public void setrentalDate(Timestamp rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Timestamp getrentalBackDate() {
        return rentalBackDate;
    }

    public void setrentalBackDate(Timestamp rentalBackDate) {
        this.rentalBackDate = rentalBackDate;
    }

    public Timestamp getrentalRealBackDate() {
        return rentalRealBackDate;
    }

    public void setrentalRealBackDate(Timestamp rentalRealBackDate) {
        this.rentalRealBackDate = rentalRealBackDate;
    }

    public byte getrentalPayStat() {
        return rentalPayStat;
    }

    public void setrentalPayStat(byte rentalPayStat) {
        this.rentalPayStat = rentalPayStat;
    }

    public byte getrentalOrdStat() {
        return rentalOrdStat;
    }

    public void setrentalOrdStat(byte rentalOrdStat) {
        this.rentalOrdStat = rentalOrdStat;
    }

    public byte getRtnStat() {
        return rtnStat;
    }

    public void setRtnStat(byte rtnStat) {
        this.rtnStat = rtnStat;
    }

    public String getRtnRemark() {
        return rtnRemark;
    }

    public void setRtnRemark(String rtnRemark) {
        this.rtnRemark = rtnRemark;
    }

    public BigDecimal getRtnCompensation() {
        return rtnCompensation;
    }

    public void setRtnCompensation(BigDecimal rtnCompensation) {
        this.rtnCompensation = rtnCompensation;
    }

    /*--------------------------聯合映射用的 getter、setter( rentalorder 是主表)------------------------------*/
    public Set<RentalOrderDetails> getRentalOrderDetailses() {
        return rentalOrderDetailses;
    }

    public void setRentalOrderDetailses(Set<RentalOrderDetails> rentalOrderDetailses) {
        this.rentalOrderDetailses = rentalOrderDetailses;
    }

    /*--------------------------聯合映射用的 getter、setter( member 是主表)------------------------------*/
    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


    @Override
    public String toString() {
        return "RentalOrderVo_ORM{" +
                "rentalOrdNo=" + rentalOrdNo +
                ", memNo=" + member.getMemNo() +
                ", rentalByrName='" + rentalByrName + '\'' +
                ", rentalByrPhone='" + rentalByrPhone + '\'' +
                ", rentalByrEmail='" + rentalByrEmail + '\'' +
                ", rentalRcvName='" + rentalRcvName + '\'' +
                ", rentalRcvPhone='" + rentalRcvPhone + '\'' +
                ", rentalTakeMethod=" + rentalTakeMethod +
                ", rentalAddr='" + rentalAddr + '\'' +
                ", rentalPayMethod=" + rentalPayMethod +
                ", rentalAllPrice=" + rentalAllPrice +
                ", rentalAllDepPrice=" + rentalAllDepPrice +
                ", rentalOrdTime=" + rentalOrdTime +
                ", rentalDate=" + rentalDate +
                ", rentalBackDate=" + rentalBackDate +
                ", rentalRealBackDate=" + rentalRealBackDate +
                ", rentalPayStat=" + rentalPayStat +
                ", rentalOrdStat=" + rentalOrdStat +
                ", rtnStat=" + rtnStat +
                ", rtnRemark='" + rtnRemark + '\'' +
                ", rtnCompensation=" + rtnCompensation +
                ", rentalOrderDetailses=" + rentalOrderDetailses +
                '}';

    }

}