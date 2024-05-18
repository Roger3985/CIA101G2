package com.howard.rentalorder.service.impl;

import com.howard.rentalorder.entity.RentalOrder;
import ecpay.logistics.integration.AllInOne;
import ecpay.logistics.integration.domain.CreateHomeObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class RentalOrderShippingService {

    @Autowired
    private RentalOrderServiceImpl orderService;

    @Autowired
    private LogisticsStateService logisticsStateService;

    // 出貨
    public String shipping(Integer rentalOrdNo) {

        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        RentalOrder rentalOrder = orderService.getByAttributes(map).get(0);
        AllInOne all = new AllInOne("");
        CreateHomeObj obj = new CreateHomeObj();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        obj.setMerchantTradeDate( sdf.format(rentalOrder.getrentalOrdTime()) ); // 交易時間
        obj.setLogisticsSubType("TCAT"); // 物流子類型
        obj.setGoodsAmount( String.valueOf( rentalOrder.getrentalAllPrice().add(rentalOrder.getrentalAllDepPrice()) ) ); // 交易金額
        obj.setSenderName( "howard" ); // 寄件人姓名 未來代入管理員姓名
        obj.setSenderCellPhone("0923561207"); // 寄件人手機號碼
        obj.setSenderZipCode("32041"); // 寄件人郵遞區號
        obj.setSenderAddress("32041桃園市中壢區延平路498號7樓之10"); // 寄件人地址
        obj.setReceiverName( rentalOrder.getrentalRcvName() ); // 收件人姓名
        obj.setReceiverZipCode("32041"); // 收件人郵遞區號
        obj.setReceiverCellPhone("0923561207"); // 收件人手機號碼
        obj.setReceiverAddress("32041桃園市中壢區復興路46號8樓"); // 收件人地址
        obj.setScheduledPickupTime("4"); // 預定收件時段
        obj.setTemperature("0001"); // 配送溫度
        obj.setDistance("00"); // 距離
        obj.setSpecification("0001"); // 貨物規格
        obj.setServerReplyURL( "http://localhost:8080/backend/rentalorder/testToRentalCart" ); // 訂單狀態從這通知
//        obj.setClientReplyURL( "http://localhost:8080/backend/rentalorder/testToRentalCart" );
        String formHTML = all.create(obj);

        // 先把物流基本資料送進 Redis，用來放綠界回傳更新的物流狀態
        logisticsStateService.setLogisticsState(rentalOrder, formHTML);

        return formHTML;
    }

}
