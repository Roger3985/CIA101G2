package com.Cia101G2.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    public Integer getstudioOrdNo() {
        return studioOrdNo;
    }

    public void setstudioOrdNo(Integer studioOrdNo) {
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

    public Timestamp getstudioOrdTime() {
        return studioOrdTime;
    }

    public void setstudioOrdTime(Timestamp studioOrdTime) {
        this.studioOrdTime = studioOrdTime;
    }

    public Byte getstudioOrdStat() {
        return studioOrdStat;
    }

    public void setstudioOrdStat(Byte studioOrdStat) {
        this.studioOrdStat = studioOrdStat;
    }

    public BigDecimal getstudioTtlPrice() {
        return studioTtlPrice;
    }

    public void setstudioTtlPrice(BigDecimal studioTtlPrice) {
        this.studioTtlPrice = studioTtlPrice;
    }

    public BigDecimal getstudioDepPrice() {
        return studioDepPrice;
    }

    public void setstudioDepPrice(BigDecimal studioDepPrice) {
        this.studioDepPrice = studioDepPrice;
    }

    public String getstudioByrName() {
        return studioByrName;
    }

    public void setstudioByrName(String studioByrName) {
        this.studioByrName = studioByrName;
    }

    public String getstudioByrPhone() {
        return studioByrPhone;
    }

    public void setstudioByrPhone(String studioByrPhone) {
        this.studioByrPhone = studioByrPhone;
    }

    public String getstudioByrEmail() {
        return studioByrEmail;
    }

    public void setstudioByrEmail(String studioByrEmail) {
        this.studioByrEmail = studioByrEmail;
    }

    public Byte getstudioPayMethod() {
        return studioPayMethod;
    }

    public void setstudioPayMethod(Byte studioPayMethod) {
        this.studioPayMethod = studioPayMethod;
    }

    public Byte getstudioPayStat() {
        return studioPayStat;
    }

    public void setstudioPayStat(Byte studioPayStat) {
        this.studioPayStat = studioPayStat;
    }

    public Byte getCheckInStat() {
        return checkInStat;
    }

    public void setCheckInStat(Byte checkInStat) {
        this.checkInStat = checkInStat;
    }

    public String getstudioReturnMark() {
        return studioReturnMark;
    }

    public void setstudioReturnMark(String studioReturnMark) {
        this.studioReturnMark = studioReturnMark;
    }

    public BigDecimal getstudioCompensation() {
        return studioCompensation;
    }

    public void setstudioCompensation(BigDecimal studioCompensation) {
        this.studioCompensation = studioCompensation;
    }

    public Set<StudioTimeBooking> getStudioTimeBookings() {
        return studioTimeBookings;
    }

    public void setStudioTimeBookings(Set<StudioTimeBooking> studioTimeBookings) {
        this.studioTimeBookings = studioTimeBookings;
    }
}
