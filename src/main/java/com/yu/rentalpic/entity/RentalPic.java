package com.yu.rentalpic.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yu.rental.entity.Rental;

import javax.persistence.*;

@Entity  //標示類別為"永續類別"
@Table(name = "rentalpic")  //此"永續類別"對應到的表格
public class RentalPic implements java.io.Serializable {

    @Id //標示為PK
    @Column(name="rentalpicno", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //有設立AUTO_INCREMENT
    private Integer rentalPicNo;

    @Column(name="rentalfile", columnDefinition = "longblob")
    private byte[] rentalFile;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name="rentalno", referencedColumnName="rentalno")
    private Rental rental;

    public RentalPic(){}

    public RentalPic(Integer rentalPicNo, byte[] rentalFile, Rental rental) {
        this.rentalPicNo = rentalPicNo;
        this.rentalFile = rentalFile;
        this.rental = rental;
    }

    public Integer getRentalPicNo() {
        return rentalPicNo;
    }

    public void setRentalPicNo(Integer rentalPicNo) {
        this.rentalPicNo = rentalPicNo;
    }

    public byte[] getRentalFile() {
        return rentalFile;
    }

    public void setRentalFile(byte[] rentalFile) {
        this.rentalFile = rentalFile;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }


}
