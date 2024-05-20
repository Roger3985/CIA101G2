package com.yu.rentSet.entity;

import com.howard.rentalorder.entity.RentalOrder;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity  //標示類別為"永續類別"
@Table(name = "rentset")  //此"永續類別"對應到的表格
public class RentSet implements java.io.Serializable {

    @Id //標示為PK
    @Column(name = "rentalordno")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //有設立AUTO_INCREMENT
    private Integer rentalOrdNo;

    @OneToOne
    @MapsId
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

    public RentSet(){}

    public RentSet(Integer rentalOrdNo, String rentalSetName, Byte rentalSetDays) {
        this.rentalOrdNo = rentalOrdNo;
        this.rentalSetName = rentalSetName;
        this.rentalSetDays = rentalSetDays;
    }

    public RentSet(Integer rentalOrdNo, RentalOrder rentalOrder, String rentalSetName, Byte rentalSetDays) {
        this.rentalOrdNo = rentalOrdNo;
        this.rentalOrder = rentalOrder;
        this.rentalSetName = rentalSetName;
        this.rentalSetDays = rentalSetDays;
    }

    public Integer getRentalOrdNo() {
        return rentalOrdNo;
    }

    public void setRentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
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

    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

}