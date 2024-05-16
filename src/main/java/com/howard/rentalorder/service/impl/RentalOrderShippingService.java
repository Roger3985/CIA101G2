package com.howard.rentalorder.service.impl;

import com.howard.rentalorder.entity.RentalOrder;
import ecpay.logistics.integration.domain.CreateCVSObj;
import ecpay.logistics.integration.AllInOne;
import ecpay.logistics.integration.domain.CreateHomeObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RentalOrderShippingService {

    @Autowired
    private RentalOrderServiceImpl orderService;

    public String shipping() { // 出貨


        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", 13);
        List<RentalOrder> rentalOrderList = orderService.getByAttributes(map);
        RentalOrder rentalOrder = rentalOrderList.get(0);
        AllInOne all = new AllInOne("");
        CreateHomeObj obj = new CreateHomeObj();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        obj.setMerchantTradeDate( sdf.format(rentalOrder.getrentalOrdTime()) );
        obj.setLogisticsSubType("TCAT");
        obj.setGoodsAmount( String.valueOf( rentalOrder.getrentalAllPrice().add(rentalOrder.getrentalAllDepPrice()) ) );
        obj.setSenderName( "howard" );
        obj.setSenderCellPhone("0923561207");
        obj.setSenderZipCode("32041"); // 寄件人郵遞區號
        obj.setSenderAddress("32041桃園市中壢區延平路498號7樓之10"); // 寄件人地址
        obj.setReceiverName( rentalOrder.getrentalRcvName() );
        obj.setReceiverZipCode("32041");
        obj.setReceiverCellPhone("0923561207");
        obj.setReceiverAddress("32041桃園市中壢區復興路46號8樓");
        obj.setScheduledPickupTime("4");
        obj.setTemperature("0001");
        obj.setDistance("00");
        obj.setSpecification("0001");
        obj.setServerReplyURL( "http://localhost:8080/backend/rentalorder/testToRentalCart" ); // 訂單狀態從這通知
//        obj.setClientReplyURL( "http://localhost:8080/backend/rentalorder/testToRentalCart" );
        String formHTML = all.create(obj);

        return formHTML;
    }

}
