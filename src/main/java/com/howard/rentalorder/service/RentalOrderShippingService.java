package com.howard.rentalorder.service;

public interface RentalOrderShippingService {

    /**
     * 租借訂單出貨
     * @param rentalOrdNo 要出貨的訂單編號
     * @return 可發送出貨請求的 formHTML
     */
    public String shipping(Integer rentalOrdNo);

}
