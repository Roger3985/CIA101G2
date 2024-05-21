package com.yu.rentSet.entity;

import com.howard.rentalorder.entity.RentalOrder;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

@Entity  //標示類別為"永續類別"
@Table(name = "rentset")  //此"永續類別"對應到的表格
public class RentSet implements java.io.Serializable {

    @EmbeddedId   //加上@EmbeddedId 標註，必須override此類別的hashcode()、equals()
    private CompositeDetail compositeDetail;

    @OneToOne
    @MapsId("rentalOrdNo")
    @JoinColumn(name = "rentalordno", referencedColumnName = "rentalordno")
    private RentalOrder rentalOrder;

    @Column(name = "rentalsetname", length = 20)
    @NotBlank(message="*方案名稱: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,20}$",message = "*只能是中、英文字母、數字和_ , 且長度必需在2到20之間")
    private String rentalSetName;

    @Column(name = "rentalsetdays", columnDefinition = "TINYINT")
    @NotNull(message="*租借天數: 請勿空白")
    @Min(value = 0, message = "*租借天數: 不能小於{value}")
    @Max(value = 1, message = "*租借天數: 不能小於{value}")
    private Byte rentalSetDays;


    @Embeddable
    public static class CompositeDetail implements java.io.Serializable {

        @Column(name = "rentalordno")
        private Integer rentalOrdNo;

        public CompositeDetail() {
        }

        public CompositeDetail(Integer rentalOrdNo) {
            this.rentalOrdNo = rentalOrdNo;
        }

        public Integer getRentalOrdNo() {
            return rentalOrdNo;
        }

        public void setRentalOrdNo(Integer rentalOrdNo) {
            this.rentalOrdNo = rentalOrdNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeDetail)) return false;
            CompositeDetail that = (CompositeDetail) o;
            return Objects.equals(getRentalOrdNo(), that.getRentalOrdNo());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRentalOrdNo());
        }

    }

    public RentSet(){}

    public RentSet(CompositeDetail compositeDetail, RentalOrder rentalOrder, String rentalSetName, Byte rentalSetDays) {
        this.compositeDetail = compositeDetail;
        this.rentalOrder = rentalOrder;
        this.rentalSetName = rentalSetName;
        this.rentalSetDays = rentalSetDays;
    }

    public RentSet(CompositeDetail compositeDetail, RentalOrder rentalOrder, String rentalSetName) {
        this.compositeDetail = compositeDetail;
        this.rentalOrder = rentalOrder;
        this.rentalSetName = rentalSetName;
    }

    public RentSet(CompositeDetail compositeDetail, String rentalSetName, Byte rentalSetDays) {
        this.compositeDetail = compositeDetail;
        this.rentalSetName = rentalSetName;
        this.rentalSetDays = rentalSetDays;
    }

    public RentSet(CompositeDetail compositeDetail, RentalOrder rentalOrder, Byte rentalSetDays) {
        this.compositeDetail = compositeDetail;
        this.rentalOrder = rentalOrder;
        this.rentalSetDays = rentalSetDays;
    }

    public RentSet(CompositeDetail compositeDetail, String rentalSetName) {
        this.compositeDetail = compositeDetail;
        this.rentalSetName = rentalSetName;
    }

    public RentSet(CompositeDetail compositeDetail, RentalOrder rentalOrder) {
        this.compositeDetail = compositeDetail;
        this.rentalOrder = rentalOrder;
    }

    public RentSet(CompositeDetail compositeDetail, Byte rentalSetDays) {
        this.compositeDetail = compositeDetail;
        this.rentalSetDays = rentalSetDays;
    }

    public CompositeDetail getCompositeDetail() {
        return compositeDetail;
    }

    public void setCompositeDetail(CompositeDetail compositeDetail) {
        this.compositeDetail = compositeDetail;
    }

    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public String getRentalSetName() {
        return rentalSetName;
    }

    public void setRentalSetName(String rentalSetName) {
        this.rentalSetName = rentalSetName;
    }

    public Byte getRentalSetDays() {
        return rentalSetDays;
    }

    public void setRentalSetDays(Byte rentalSetDays) {
        this.rentalSetDays = rentalSetDays;
    }


}
