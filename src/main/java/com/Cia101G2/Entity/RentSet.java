package com.Cia101G2.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity  //標示類別為"永續類別"
@Table(name = "rentset")  //此"永續類別"對應到的表格
public class RentSet {

    @Id //標示為PK
    @Column(name = "rentalordno")
    private Integer rentalOrdNo;

    @Column(name = "rentalsetname", length = 20)
    private String rentalSetName;

    @Column(name = "rentalsetdays", columnDefinition = "tinyint")
    private Byte rentalSetDays;

    public Integer getrentalOrdNo() {
        return rentalOrdNo;
    }

    public void setrentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
    }

    public String getrentalSetName() {
        return rentalSetName;
    }

    public void setrentalSetName(String rentalSetName) {
        this.rentalSetName = rentalSetName;
    }

    public Byte getrentalSetDays() {
        return rentalSetDays;
    }

    public void setrentalSetDays(Byte rentalSetDays) {
        this.rentalSetDays = rentalSetDays;
    }

    @Override
    public String toString() {
        return "RentalCategory [rentalOrdNo=" + rentalOrdNo + ", rentalSetName=" + rentalSetName + "," + " rentalSetDays=" + rentalSetDays + "]";
    }

}

