package com.Cia101G2.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

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

    public Integer getstudioNo() {
        return studioNo;
    }

    public void setstudioNo(Integer studioNo) {
        this.studioNo = studioNo;
    }

    public String getstudioName() {
        return studioName;
    }

    public void setstudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getstudioInfo() {
        return studioInfo;
    }

    public void setstudioInfo(String studioInfo) {
        this.studioInfo = studioInfo;
    }

    public BigDecimal getstudioPrice() {
        return studioPrice;
    }

    public void setstudioPrice(BigDecimal studioPrice) {
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
