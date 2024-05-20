//package com.yu.rentSet.entity;
//
//import javax.persistence.*;
//import java.awt.print.Book;
//
//@Entity  //標示類別為"永續類別"
//@Table(name = "rentsetid")  //此"永續類別"對應到的表格
//public class RentSetId implements java.io.Serializable {
//
//    @Id
//    @Column(name = "rentid")
//    private Integer rentid;
//
//    @OneToOne
//    @MapsId // 代表關聯的RentSetId也同時是自己的ID
//    @JoinColumn(name = "rentid", referencedColumnName = "rentalOrdNo")
//    private RentSet rentSet;
//
//    public Integer getRentid() {
//        return rentid;
//    }
//
//    public void setRentid(Integer rentid) {
//        this.rentid = rentid;
//    }
//
//    public RentSet getRentSet() {
//        return rentSet;
//    }
//
//    public void setRentSet(RentSet rentSet) {
//        this.rentSet = rentSet;
//    }
//}
