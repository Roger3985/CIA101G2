package com.howard.rentalorder.service;

import com.howard.rentalorder.entity.RentalOrder;

import java.util.Map;

public interface LogisticsStateService {

    /**
     * 查詢宅配訂單最新狀態
     *
     * @param memNo 產生該筆物流訂單的會員編號
     * @param rentalOrdNo 產生該筆物流訂單的租借訂單編號
     * @return 最新物流狀態碼
     */
    public String postQueryLogisticsTradeInfo(Integer memNo, Integer rentalOrdNo);

    /**
     *   創立物流單時先放空字串進 Redis
     * @param order 產生該筆物流訂單的訂單號碼
     * @param formHTML 綠界回傳的物流訂單回應資訊
     */
    public void setLogisticsState(RentalOrder order, String formHTML);

    /**
     * 把回傳的字串重新組合成比較好用的結構
     * @param infoString : 綠界物流回傳的物流狀態資訊字串
     * @return : Map<物流狀態名, 物流狀態值>
     */
    public Map<String, String> parseLogisticsInfo(String infoString);

}
