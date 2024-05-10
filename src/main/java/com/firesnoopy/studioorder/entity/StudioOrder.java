package com.firesnoopy.studioorder.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.firesnoopy.studioinfo.entity.StudioInfo;
import com.firesnoopy.studiotimebooking.entity.StudioTimeBooking;
import com.ren.administrator.entity.Administrator;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "studioorder")
public class StudioOrder {
    @Id
    @Column(name = "studioordno")
    private Integer studioOrdNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "studiono", referencedColumnName = "studiono")
    private StudioInfo studioInfo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "admno", referencedColumnName = "admno")
    private Administrator administrator;
    @Column(name = "bookeddate")
    private Date bookedDate;
    @Column(name = "bookedtimemorning")
    private Byte bookedTimeMorning;
    @Column(name = "bookedtimeafternoon")
    private Byte bookedTimeAfternoon;
    @Column(name = "bookedtimenight")
    private Byte bookedTimeNight;
    @Column(name = "studioordtime")
    private Timestamp studioOrdTime;
    @Column(name = "studioordstat")
    private Byte studioOrdStat;
    @Column(name = "studiottlprice")
    private BigDecimal studioTtlPrice;
    @Column(name = "studiodepprice")
    private BigDecimal studioDepPrice;
    @Column(name = "studiobyrname")
    private String studioByrName;
    @Column(name = "studiobyrphone")
    private String studioByrPhone;
    @Column(name = "studiobyremail")
    private String studioByrEmail;
    @Column(name = "studiopaymethod")
    private Byte studioPayMethod;
    @Column(name = "studiopaystat")
    private Byte studioPayStat;
    @Column(name = "checkinstat")
    private Byte checkInStat;
    @Column(name = "studioreturnmark")
    private String studioReturnMark;
    @Column(name = "studiocompensation")
    private BigDecimal studioCompensation;
    @JsonBackReference
    @OneToMany(mappedBy = "studioOrder", cascade = CascadeType.ALL)
    private Set<StudioTimeBooking> studioTimeBookings;

    public StudioOrder() {
    }

    public StudioOrder(Integer studioOrdNo) {
        this.studioOrdNo = studioOrdNo;
    }

    public StudioOrder(Member member, StudioInfo studioInfo, Administrator administrator, Date bookedDate, Byte bookedTimeMorning, Byte bookedTimeAfternoon, Byte bookedTimeNight, Timestamp studioOrdTime, Byte studioOrdStat, BigDecimal studioTtlPrice, BigDecimal studioDepPrice, String studioByrName, String studioByrPhone, String studioByrEmail, Byte studioPayMethod, Byte studioPayStat, Byte checkInStat, String studioReturnMark, BigDecimal studioCompensation) {
        this.member = member;
        this.studioInfo = studioInfo;
        this.administrator = administrator;
        this.bookedDate = bookedDate;
        this.bookedTimeMorning = bookedTimeMorning;
        this.bookedTimeAfternoon = bookedTimeAfternoon;
        this.bookedTimeNight = bookedTimeNight;
        this.studioOrdTime = studioOrdTime;
        this.studioOrdStat = studioOrdStat;
        this.studioTtlPrice = studioTtlPrice;
        this.studioDepPrice = studioDepPrice;
        this.studioByrName = studioByrName;
        this.studioByrPhone = studioByrPhone;
        this.studioByrEmail = studioByrEmail;
        this.studioPayMethod = studioPayMethod;
        this.studioPayStat = studioPayStat;
        this.checkInStat = checkInStat;
        this.studioReturnMark = studioReturnMark;
        this.studioCompensation = studioCompensation;
    }

    public StudioOrder(Integer studioOrdNo, Member member, StudioInfo studioInfo, Administrator administrator, Date bookedDate, Byte bookedTimeMorning, Byte bookedTimeAfternoon, Byte bookedTimeNight, Timestamp studioOrdTime, Byte studioOrdStat, BigDecimal studioTtlPrice, BigDecimal studioDepPrice, String studioByrName, String studioByrPhone, String studioByrEmail, Byte studioPayMethod, Byte studioPayStat, Byte checkInStat, String studioReturnMark, BigDecimal studioCompensation) {
        this.studioOrdNo = studioOrdNo;
        this.member = member;
        this.studioInfo = studioInfo;
        this.administrator = administrator;
        this.bookedDate = bookedDate;
        this.bookedTimeMorning = bookedTimeMorning;
        this.bookedTimeAfternoon = bookedTimeAfternoon;
        this.bookedTimeNight = bookedTimeNight;
        this.studioOrdTime = studioOrdTime;
        this.studioOrdStat = studioOrdStat;
        this.studioTtlPrice = studioTtlPrice;
        this.studioDepPrice = studioDepPrice;
        this.studioByrName = studioByrName;
        this.studioByrPhone = studioByrPhone;
        this.studioByrEmail = studioByrEmail;
        this.studioPayMethod = studioPayMethod;
        this.studioPayStat = studioPayStat;
        this.checkInStat = checkInStat;
        this.studioReturnMark = studioReturnMark;
        this.studioCompensation = studioCompensation;
    }

    public Integer getStudioOrdNo() {
        return studioOrdNo;
    }

    public void setStudioOrdNo(Integer studioOrdNo) {
        this.studioOrdNo = studioOrdNo;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public StudioInfo getStudioInfo() {
        return studioInfo;
    }

    public void setStudioInfo(StudioInfo studioInfo) {
        this.studioInfo = studioInfo;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Date getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(Date bookedDate) {
        this.bookedDate = bookedDate;
    }

    public Byte getBookedTimeMorning() {
        return bookedTimeMorning;
    }

    public void setBookedTimeMorning(Byte bookedTimeMorning) {
        this.bookedTimeMorning = bookedTimeMorning;
    }

    public Byte getBookedTimeAfternoon() {
        return bookedTimeAfternoon;
    }

    public void setBookedTimeAfternoon(Byte bookedTimeAfternoon) {
        this.bookedTimeAfternoon = bookedTimeAfternoon;
    }

    public Byte getBookedTimeNight() {
        return bookedTimeNight;
    }

    public void setBookedTimeNight(Byte bookedTimeNight) {
        this.bookedTimeNight = bookedTimeNight;
    }

    public Timestamp getStudioOrdTime() {
        return studioOrdTime;
    }

    public void setStudioOrdTime(Timestamp studioOrdTime) {
        this.studioOrdTime = studioOrdTime;
    }

    public Byte getStudioOrdStat() {
        return studioOrdStat;
    }

    public void setStudioOrdStat(Byte studioOrdStat) {
        this.studioOrdStat = studioOrdStat;
    }

    public BigDecimal getStudioTtlPrice() {
        return studioTtlPrice;
    }

    public void setStudioTtlPrice(BigDecimal studioTtlPrice) {
        this.studioTtlPrice = studioTtlPrice;
    }

    public BigDecimal getStudioDepPrice() {
        return studioDepPrice;
    }

    public void setStudioDepPrice(BigDecimal studioDepPrice) {
        this.studioDepPrice = studioDepPrice;
    }

    public String getStudioByrName() {
        return studioByrName;
    }

    public void setStudioByrName(String studioByrName) {
        this.studioByrName = studioByrName;
    }

    public String getStudioByrPhone() {
        return studioByrPhone;
    }

    public void setStudioByrPhone(String studioByrPhone) {
        this.studioByrPhone = studioByrPhone;
    }

    public String getStudioByrEmail() {
        return studioByrEmail;
    }

    public void setStudioByrEmail(String studioByrEmail) {
        this.studioByrEmail = studioByrEmail;
    }

    public Byte getStudioPayMethod() {
        return studioPayMethod;
    }

    public void setStudioPayMethod(Byte studioPayMethod) {
        this.studioPayMethod = studioPayMethod;
    }

    public Byte getStudioPayStat() {
        return studioPayStat;
    }

    public void setStudioPayStat(Byte studioPayStat) {
        this.studioPayStat = studioPayStat;
    }

    public Byte getCheckInStat() {
        return checkInStat;
    }

    public void setCheckInStat(Byte checkInStat) {
        this.checkInStat = checkInStat;
    }

    public String getStudioReturnMark() {
        return studioReturnMark;
    }

    public void setStudioReturnMark(String studioReturnMark) {
        this.studioReturnMark = studioReturnMark;
    }

    public BigDecimal getStudioCompensation() {
        return studioCompensation;
    }

    public void setStudioCompensation(BigDecimal studioCompensation) {
        this.studioCompensation = studioCompensation;
    }

    public Set<StudioTimeBooking> getStudioTimeBookings() {
        return studioTimeBookings;
    }

    public void setStudioTimeBookings(Set<StudioTimeBooking> studioTimeBookings) {
        this.studioTimeBookings = studioTimeBookings;
    }
}