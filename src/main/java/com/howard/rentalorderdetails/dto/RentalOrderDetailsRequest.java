package com.howard.rentalorderdetails.dto;



import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RentalOrderDetailsRequest {

    @NotNull
    Integer rentalOrdNo; // -> 租借訂單編號
    @NotNull
    Integer rentalNo; // -> 租借品編號
    @NotNull
    BigDecimal rentalPrice; // -> 單價
    @NotNull
    BigDecimal rentalDesPrice; // -> 押金

    public Integer getrentalOrdNo() {
        return rentalOrdNo;
    }

    public void setrentalOrdNo(Integer rentalOrdNo) {
        this.rentalOrdNo = rentalOrdNo;
    }

    public Integer getRentalNo() {
        return rentalNo;
    }

    public void setrentalNo(Integer rentalNo) {
        this.rentalNo = rentalNo;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setrentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public BigDecimal getRentalDesPrice() {
        return rentalDesPrice;
    }

    public void setrentalDesPrice(BigDecimal rentalDesPrice) {
        this.rentalDesPrice = rentalDesPrice;
    }

}
