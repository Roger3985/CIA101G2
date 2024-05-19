package com.howard.rentalorder.service.impl;

import com.howard.rentalorder.entity.RentalOrder;
import com.howard.util.JedisUtil;
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
            // 創建物流訂單時，先把會有的 key 值放進去，以免綠界回傳時沒有資訊能辨識是哪筆訂單的資訊
//            Map<String, String> statInfos = new HashMap<>();
            jedis.set(itemKey, formHTML);

        }

    }

    public void updateLogisticsState(String statInfo) {



    }


}
