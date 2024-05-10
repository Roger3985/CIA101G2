package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

    @Entity  //標示類別為"永續類別"
    @Table(name = "rentalpic")  //此"永續類別"對應到的表格
    public class RentalPic {

        @Id //標示為PK
        @Column(name="rentalpicno", nullable=false)
        private Integer rentalPicNo;

        @Column(name="rentalpic", columnDefinition = "longblob")
        private byte[] rentalPic;

        @ManyToOne
        @JsonManagedReference
        @JoinColumn(name="rentalno", referencedColumnName="rentalno")
        private Rental rental;

        public Integer getrentalPicNo() {
            return rentalPicNo;
        }

        public void setrentalPicNo(Integer rentalPicNo) {
            this.rentalPicNo = rentalPicNo;
        }

        public byte[] getrentalPic() {
            return rentalPic;
        }

        public void setrentalPic(byte[] rentalPic) {
            this.rentalPic = rentalPic;
        }

        public Rental getRental() {
            return rental;
        }

        public void setRental(Rental rental) {
            this.rental = rental;
        }
    }
