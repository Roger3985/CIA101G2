package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity  //標示類別為"永續類別"
@Table(name = "rentalcategory")  //此"永續類別"對應到的表格
public class RentalCategory {

    @Id //標示此為Pk
    @Column(name="rentalcatno")
    private Integer rentalCatNo;

    @Column(name="rentalcatname", length=40)
    private String rentalCatName;

    @Column(name="rentalstockqty")
    private Integer rentalStockQty;

    @Column(name="rentalrentedqty")
    private Integer rentalRentedQty;

    @Column(name="rentaldesprice",columnDefinition="BigDecimal")
    private BigDecimal rentalDesPrice;
    @JsonBackReference
    @OneToMany(mappedBy = "rentalCategory", cascade = CascadeType.ALL) //CascadeType.ALL把對應到的相關資料刪除
    private Set<Rental> rentals;

    public Integer getrentalCatNo() {
        return rentalCatNo;
    }

    public void setrentalCatNo(Integer rentalCatNo) {
        this.rentalCatNo = rentalCatNo;
    }

    public String getrentalCatName() {
        return rentalCatName;
    }

    public void setrentalCatName(String rentalCatName) {
        this.rentalCatName = rentalCatName;
    }

    public Integer getrentalStockQty() {
        return rentalStockQty;
    }

    public void setrentalStockQty(Integer rentalStockQty) {
        this.rentalStockQty = rentalStockQty;
    }

    public Integer getrentalRentedQty() {
        return rentalRentedQty;
    }

    public void setrentalRentedQty(Integer rentalRentedQty) {
        this.rentalRentedQty = rentalRentedQty;
    }

    public BigDecimal getrentalDesPrice() {
        return rentalDesPrice;
    }

    public void setrentalDesPrice(BigDecimal rentalDesPrice) {
        this.rentalDesPrice = rentalDesPrice;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }

    public RentalCategory() {
    }

    public RentalCategory(Integer rentalCatNo) {
        this.rentalCatNo = rentalCatNo;
    }

}