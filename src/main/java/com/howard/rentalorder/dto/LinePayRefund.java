package com.howard.rentalorder.dto;

import java.math.BigDecimal;

public class LinePayRefund {

    private BigDecimal refundAmount;

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

}
