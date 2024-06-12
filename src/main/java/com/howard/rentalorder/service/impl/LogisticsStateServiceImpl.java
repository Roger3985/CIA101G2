package com.howard.rentalorder.service.impl;

import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.service.LogisticsStateService;
import com.howard.util.JedisUtil;
import ecpay.logistics.integration.AllInOne;
import ecpay.logistics.integration.domain.QueryLogisticsTradeInfoObj;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Component
public class LogisticsStateServiceImpl implements LogisticsStateService {

    JedisPool jedisPool = null;

    public LogisticsStateServiceImpl() {
        jedisPool = JedisUtil.getJedisPool();
    }

    @Override
    public String postQueryLogisticsTradeInfo(Integer memNo, Integer rentalOrdNo){

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            // 取出該筆物流訂單的 AllPayLogisticsID
            String itemKey = "logisticsStat : member : " + memNo + " : rentalOrdNo : " + rentalOrdNo;
            Map<String, String> statInfos = jedis.hgetAll(itemKey);
            String allPayLogisticsID = statInfos.get("AllPayLogisticsID");
            System.out.println("用內網查到了，這筆的AllPayLogisticsID = " + allPayLogisticsID);
            if (allPayLogisticsID == null) {
                return "noData";
            }
            // 用那筆 AllPayLogisticsID 去查詢物流狀態
            AllInOne all = new AllInOne();
            QueryLogisticsTradeInfoObj obj = new QueryLogisticsTradeInfoObj();
            obj.setAllPayLogisticsID(allPayLogisticsID);
            // 查到一長串物流最新狀態的字串
            String infosUpdated = all.queryLogisticsTradeInfo(obj);
            // 重構成 key-value 形式，方便取值
            Map<String, String> statInfosUpdated = parseLogisticsInfo(infosUpdated);
            // 回傳物流狀態碼
            return statInfosUpdated.get("LogisticsStatus");

        }

    } // postQueryLogisticsTradeInfo 結束


    @Override
    public void setLogisticsState(RentalOrder order, String formHTML) {

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            String itemKey = "logisticsStat : member : " + order.getMember().getMemNo() + " : rentalOrdNo : " + order.getrentalOrdNo();
            // 重構 formHTML 成可用格式 (以 | 為分水嶺，將 formHTML 切割成兩部分，取第二部分，也就從 AllPayLogisticsID 開始)
            String infoString = formHTML.split("\\|", 2)[1];
            Map<String, String> statInfos = parseLogisticsInfo(infoString);
            jedis.hmset(itemKey, statInfos);

        }

    }

    /*----------------------------------------提煉成方法----------------------------------------*/
    @Override
    public Map<String, String> parseLogisticsInfo(String infoString) {
        System.out.println("有進來組合參數方法喔");
        Map<String, String> statInfos = new HashMap<>();
        // 把 formHTML 裡的各個 key-value 以 & 為分水嶺切割開來，放進字串陣列
        String[] infoPairs = infoString.split("&");
        for (String pair : infoPairs) {
            // 把每一個 key-value pair 以 = 為分水嶺切割開來
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0];
            // 如果 value 有值就放，沒有就放空字串
            String value = keyValue.length > 1 ? keyValue[1] : "";
            statInfos.put(key, value);

        }
        return  statInfos;

    }

}
