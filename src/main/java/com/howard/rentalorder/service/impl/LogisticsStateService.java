package com.howard.rentalorder.service.impl;

import com.howard.rentalorder.entity.RentalOrder;
import com.howard.util.JedisUtil;
import ecpay.logistics.integration.AllInOne;
import ecpay.logistics.integration.domain.QueryLogisticsTradeInfoObj;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Component
public class LogisticsStateService {

    JedisPool jedisPool = null;

    public LogisticsStateService() {
        jedisPool = JedisUtil.getJedisPool();
    }

    // 創立物流單時先放空字串進
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

    /**
     * 把綠界物流回傳的字串重新組合成比較好用的結構
     * @param infoString : 綠界物流回傳的物流狀態資訊字串
     * @return : Map<物流狀態名, 物流狀態值>
     */
    public Map<String, String> parseLogisticsInfo(String infoString) {

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

    /**
     * 查詢宅配訂單最新狀態
     *
     * @param memNo 產生該筆物流訂單的會員編號
     * @param rentalOrdNo 產生該筆物流訂單的租借訂單編號
     * @return 最新物流狀態碼
     */
    public String postQueryLogisticsTradeInfo(Integer memNo, Integer rentalOrdNo){

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            // 取出該筆物流訂單的 AllPayLogisticsID
            String itemKey = "logisticsStat : member : " + memNo + " : rentalOrdNo : " + rentalOrdNo;
            Map<String, String> statInfos = jedis.hgetAll(itemKey);
            String allPayLogisticsID = statInfos.get("AllPayLogisticsID");
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


}
