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

    @EmbeddedId
    private CompositeDetail compositeDetail;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "rentalordno", referencedColumnName = "rentalordno", insertable = false, updatable = false)
    private RentalOrder rentalOrder;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "rentalno", referencedColumnName = "rentalno", insertable = false, updatable = false)
    private Rental rental;
    @Column(name = "rentalprice")
    BigDecimal rentalPrice;
    @Column(name = "rentaldesprice")
    BigDecimal rentalDesPrice;

    @Embeddable
    public static class CompositeDetail implements Serializable {

        @Column(name = "rentalordno")
        private Integer rentalOrdNo;
        @Column(name = "rentalno")
        private Integer rentalNo;

        public Integer getrentalOrdNo() {
            return rentalOrdNo;
        }

        public void setrentalOrdNo(Integer rentalOrdNo) {
            this.rentalOrdNo = rentalOrdNo;
        }

        public Integer getrentalNo() {
            return rentalNo;
        }

        public void setrentalNo(Integer rentalNo) {
            this.rentalNo = rentalNo;
        }

        public CompositeDetail() {
        }

        public CompositeDetail(Integer rentalOrdNo, Integer rentalNo) {
            this.rentalOrdNo = rentalOrdNo;
            this.rentalNo = rentalNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeDetail that)) return false;
            return Objects.equals(getrentalOrdNo(), that.getrentalOrdNo()) && Objects.equals(getrentalNo(), that.getrentalNo());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getrentalOrdNo(), getrentalNo());
        }

    } // 內部類別結束

    public RentalOrderDetails() {
    }

    public RentalOrderDetails(CompositeDetail compositeDetail, BigDecimal rentalPrice, BigDecimal rentalDesPrice) {
        this.compositeDetail = compositeDetail;
        this.rentalPrice = rentalPrice;
        this.rentalDesPrice = rentalDesPrice;
    }

    public CompositeDetail getCompositeDetail() {
        return compositeDetail;
    }

    public void setCompositeDetail(CompositeDetail compositeDetail) {
        this.compositeDetail = compositeDetail;
    }

    public BigDecimal getrentalPrice() {
        return rentalPrice;
    }

    public void setrentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public BigDecimal getrentalDesPrice() {
        return rentalDesPrice;
    }

    public void setrentalDesPrice(BigDecimal rentalDesPrice) {
        this.rentalDesPrice = rentalDesPrice;
    }

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

    @Override
    public String toString() {
        return "RentalOrderDetails{" +
                ", rentalOrder=" + compositeDetail.getrentalOrdNo() +
                ", rental=" + compositeDetail.getrentalNo() +
                ", rentalPrice=" + rentalPrice +
                ", rentalDesPrice=" + rentalDesPrice +
                '}' + "\n";
    }
}


