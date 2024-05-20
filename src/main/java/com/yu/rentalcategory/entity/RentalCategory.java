package com.yu.rentalcategory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yu.rental.entity.Rental;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity  //標示類別為"永續類別"
@Table(name = "rentalcategory")  //此"永續類別"對應到的表格
public class RentalCategory implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    // 分組驗證 (ex. Add時是以自動分配的數字，但Update也許對應不到。固可使用此方法)
    public static interface AddRentalCatGroup{};
    public static interface UpdateRentalCatGroup{};

    @Id //標示此為Pk
    @Column(name="rentalcatno")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //有設立AUTO_INCREMENT
    private Integer rentalCatNo;


    @Column(name="rentalcatname", length=40)
    @NotBlank(message="*租借品類別名稱: 請勿空白", groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,40}$",message = "*只能是中、英文字母、數字和_ , 且長度必需在2到40之間",
            groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    private String rentalCatName;


    @Column(name="rentalstockqty")
    @NotNull(message="*租借品庫存數量: 請勿空白", groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    @Min(value = 0, message = "*不能小於{value}", groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    private Integer rentalStockQty;


    @Column(name="rentalrentedqty")
    @NotNull(message="*租借品已租借數量: 請勿空白", groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    @Min(value = 0, message = "*不能小於{value}", groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    private Integer rentalRentedQty;


    @Column(name="rentaldesprice",columnDefinition="BigDecimal")
    @NotNull(message="*押金: 請勿空白", groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    @DecimalMin(value = "00000", message = "*押金: 不能小於0",
            groups = {RentalCategory.AddRentalCatGroup.class, RentalCategory.UpdateRentalCatGroup.class})
    private BigDecimal rentalDesPrice;


    @JsonBackReference
    @OneToMany(mappedBy = "rentalCategory", cascade = CascadeType.ALL) //CascadeType.ALL把對應到的相關資料刪除
    private Set<Rental> rentals;

    public RentalCategory() {
    }

    public RentalCategory(Integer rentalCatNo, String rentalCatName, Integer rentalStockQty, Integer rentalRentedQty, BigDecimal rentalDesPrice, Set<Rental> rentals) {
        this.rentalCatNo = rentalCatNo;
        this.rentalCatName = rentalCatName;
        this.rentalStockQty = rentalStockQty;
        this.rentalRentedQty = rentalRentedQty;
        this.rentalDesPrice = rentalDesPrice;
        this.rentals = rentals;
    }

    public Integer getRentalCatNo() {
        return rentalCatNo;
    }

    public void setRentalCatNo(Integer rentalCatNo) {
        this.rentalCatNo = rentalCatNo;
    }

    public String getRentalCatName() {
        return rentalCatName;
    }

    public void setRentalCatName(String rentalCatName) {
        this.rentalCatName = rentalCatName;
    }

    public Integer getRentalStockQty() {
        return rentalStockQty;
    }

    public void setRentalStockQty(Integer rentalStockQty) {
        this.rentalStockQty = rentalStockQty;
    }

    public Integer getRentalRentedQty() {
        return rentalRentedQty;
    }

    public void setRentalRentedQty(Integer rentalRentedQty) {
        this.rentalRentedQty = rentalRentedQty;
    }

    public BigDecimal getRentalDesPrice() {
        return rentalDesPrice;
    }

    public void setRentalDesPrice(BigDecimal rentalDesPrice) {
        this.rentalDesPrice = rentalDesPrice;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }
}