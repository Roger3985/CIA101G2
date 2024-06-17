package com.yu.rentSet.entity;

import com.howard.rentalorder.entity.RentalOrder;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

@Entity  //標示類別為"永續類別"
@Table(name = "rentset")  //此"永續類別"對應到的表格
public class RentSet implements Serializable {

// 此表格為單向一對一映射，僅須在從表添加 @OneToOne & @MapsId & @JoinColumn
//    必須額外標註 @Id 於主鍵位置
    @Id
    @Column(name = "rentalordno", length = 20)
    private Integer rentalOrdNo;


//    CascadeType.PERSIST：當持久化當前實體時，相關聯的實體也會被持久化。
//    CascadeType.REMOVE：當刪除當前實體時，相關聯的實體也會被刪除。
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @MapsId // 代表關聯的Book ID也同時是自己的ID
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

    public Integer getRentalOrdNo() {
        return rentalOrdNo;
    }

    public void setRentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
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
