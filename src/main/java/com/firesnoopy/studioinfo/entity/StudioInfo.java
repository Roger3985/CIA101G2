package com.firesnoopy.studioinfo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.firesnoopy.studioorder.entity.StudioOrder;
import com.firesnoopy.studiotimebooking.entity.StudioTimeBooking;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "studioinfo")
public class StudioInfo {
    @Id
    @Column(name = "studiono")
    private Integer studioNo;
    @Column(name = "studioname")
    private String studioName;
    @Column(name = "studioinfo")
    private String studioInfo;
    @Column(name = "studioprice")
    private BigDecimal studioPrice;
    @JsonBackReference
    @OneToMany(mappedBy = "studioInfo", cascade = CascadeType.ALL)
    private Set<StudioOrder> studioOrders;
    @JsonBackReference
    @OneToMany(mappedBy = "studioInfo", cascade = CascadeType.ALL)
    private Set<StudioTimeBooking> studioTimeBookings;

    public StudioInfo() {
    }

    public StudioInfo(Integer studioNo) {
        this.studioNo = studioNo;
    }

    public StudioInfo(String studioName, String studioInfo, BigDecimal studioPrice) {
        this.studioName = studioName;
        this.studioInfo = studioInfo;
        this.studioPrice = studioPrice;
    }

    public StudioInfo(Integer studioNo, String studioName, String studioInfo, BigDecimal studioPrice) {
        this.studioNo = studioNo;
        this.studioName = studioName;
        this.studioInfo = studioInfo;
        this.studioPrice = studioPrice;
    }

    public Integer getStudioNo() {
        return studioNo;
    }

    public void setStudioNo(Integer studioNo) {
        this.studioNo = studioNo;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getStudioInfo() {
        return studioInfo;
    }

    public void setStudioInfo(String studioInfo) {
        this.studioInfo = studioInfo;
    }

    public BigDecimal getStudioPrice() {
        return studioPrice;
    }

    public void setStudioPrice(BigDecimal studioPrice) {
        this.studioPrice = studioPrice;
    }

    public Set<StudioOrder> getStudioOrders() {
        return studioOrders;
    }

    public void setStudioOrders(Set<StudioOrder> studioOrders) {
        this.studioOrders = studioOrders;
    }

    public Set<StudioTimeBooking> getStudioTimeBookings() {
        return studioTimeBookings;
    }

    public void setStudioTimeBookings(Set<StudioTimeBooking> studioTimeBookings) {
        this.studioTimeBookings = studioTimeBookings;
    }
}
