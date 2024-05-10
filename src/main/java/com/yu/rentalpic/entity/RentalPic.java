package com.yu.rentalpic.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yu.rental.entity.Rental;

import javax.persistence.*;

@Entity  //標示類別為"永續類別"
@Table(name = "rentalpic")  //此"永續類別"對應到的表格
public class RentalPic {

    @Id //標示為PK
    @Column(name="rentalpicno", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rentalPicNo;

    @Column(name="rentalpic", columnDefinition = "longblob")
    private byte[] rentalPic;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name="rentalno", referencedColumnName="rentalno")
    private Rental rental;

    public Integer getRentalPicNo() {
        return rentalPicNo;
    }

    public void setRentalPicNo(Integer rentalPicNo) {
        this.rentalPicNo = rentalPicNo;
    }

    public byte[] getRentalPic() {
        return rentalPic;
    }

    public void setRentalPic(byte[] rentalPic) {
        this.rentalPic = rentalPic;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }
}
