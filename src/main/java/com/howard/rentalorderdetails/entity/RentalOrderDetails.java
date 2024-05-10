package com.howard.rentalorderdetails.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.howard.rentalorder.entity.RentalOrder;
import com.yu.rental.entity.Rental;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "rentalorderdetails")
public class RentalOrderDetails implements Serializable{

    public RentalOrderDetails() {

    }

    /*
     * rOrdNo -> 租借品訂單編號
     * rNo -> 租借品編號
     * rPrice -> 單價
     * rDesPrice -> 押金(單件)
     */

//    @Id
//    @Column(name = "rOrdNo")
//    private Integer rOrdNo;
    @Id
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "rordno", referencedColumnName = "rordno")
    private RentalOrder rentalOrder;

//    @Id
//    @Column(name = "rNo")
//    private Integer rNo;
    @Id
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "rno", referencedColumnName = "rno")
    private Rental rental;

    @Column(name = "rprice")
    private BigDecimal rPrice;
    @Column(name = "rdesprice")
    private BigDecimal rDesPrice;

    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public BigDecimal getrPrice() {
        return rPrice;
    }

    public void setrPrice(BigDecimal rPrice) {
        this.rPrice = rPrice;
    }

    public BigDecimal getrDesPrice() {
        return rDesPrice;
    }

    public void setrDesPrice(BigDecimal rDesPrice) {
        this.rDesPrice = rDesPrice;
    }

    /*-------------------------------內部類別的 getter、setter--------------------------------------*/

    public CompositeDetail getCompositeDetail() {
        return new CompositeDetail(rentalOrder, rental);
    }

    public void setCompositeDetail(CompositeDetail key) {
        key.setRentalOrderVoOrm(this.rentalOrder);
        key.setRentalVO(this.rental);
    }

/*-------------------------------因為複合主鍵所以加上的內部類別--------------------------------------*/

    static class CompositeDetail implements Serializable {

        private RentalOrder rentalOrder;
        private Rental rental;

        public CompositeDetail() {

        }

        public CompositeDetail(RentalOrder rentalOrder, Rental rental) {
            this.rentalOrder = rentalOrder;
            this.rental = rental;
        }

        public RentalOrder getRentalOrderVoOrm() {
            return rentalOrder;
        }

        public void setRentalOrderVoOrm(RentalOrder rentalOrder) {
            this.rentalOrder = rentalOrder;
        }

        public Rental getRentalVO() {
            return rental;
        }

        public void setRentalVO(Rental rental) {
            this.rental = rental;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeDetail that)) return false;
            return Objects.equals(getRentalOrderVoOrm(), that.getRentalOrderVoOrm()) && Objects.equals(getRentalVO(), that.getRentalVO());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRentalOrderVoOrm(), getRentalVO());
        }

    } // 內部類別結束

    @Override
    public String toString() {
        return "RentalOrderDetails_ORM{" +
                "rentalOrder=" + rentalOrder +
                ", rental=" + rental +
                ", rPrice=" + rPrice +
                ", rDesPrice=" + rDesPrice +
                '}';
    }

}


