package com.yu.rentalset.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity  //標示類別為"永續類別"
@Table(name = "rentset")  //此"永續類別"對應到的表格
public class RentSet {

    @Id //標示為PK
    @Column(name = "rordno")
    private Integer rOrdNo;

    @Column(name = "rsetname", length = 20)
    private String rSetName;

    @Column(name = "rsetdays", columnDefinition = "tinyint")
    private Byte rSetDays;

    public Integer getrOrdNo() {
        return rOrdNo;
    }

    public void setrOrdNo(Integer rOrdNo) {
        this.rOrdNo = rOrdNo;
    }

    public String getrSetName() {
        return rSetName;
    }

    public void setrSetName(String rSetName) {
        this.rSetName = rSetName;
    }

    public Byte getrSetDays() {
        return rSetDays;
    }

    public void setrSetDays(Byte rSetDays) {
        this.rSetDays = rSetDays;
    }

    @Override
    public String toString() {
        return "RentalCategory [rOrdNo=" + rOrdNo + ", rSetName=" + rSetName + "," + " rSetDays=" + rSetDays + "]";
    }

}

