package com.howard.rentalorder.service.impl;

import com.yu.rental.dao.RentalRepository;
import com.howard.rentalorder.service.RentalCartService;
import com.howard.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RentalCartServiceImpl implements RentalCartService {

    JedisPool jedisPool = null;
    public RentalCartServiceImpl() {
        jedisPool = JedisUtil.getJedisPool();
    }

    @Autowired
    private RentalRepository rentalRepository;

    public void setToCart(Integer memNo, Map<String, String> map) {

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            String itemKey = "member : " + memNo + " : cartItem : " + map.get("rentalNo");
            jedis.hmset(itemKey, map);

            // 測試 redis 同步更新
//            Map<String, String> map1 = new HashMap<>();
//            map1.put("rentalNo", String.valueOf(5001));
//            map1.put("rentalCatNo", String.valueOf(1));
//            map1.put("rentalName", "格紋成套西裝");
//            map1.put("rentalPrice", String.valueOf(15500));
//            map1.put("rentalDesPrice", String.valueOf(4000));
//            map1.put("rentalSize", String.valueOf(2));
//            map1.put("rentalColor", "黑色");
//            map1.put("rentalInfo", "款式：劍領 單排釦。適用場合：婚攝 婚宴 宴客 求婚");
//            map1.put("rentalStat", String.valueOf(0));
//            jedis.hmset("member : 2 : cartItem : 5001", map1);
//
//            Map<String, String> map2 = new HashMap<>();
//            map2.put("rentalNo", String.valueOf(5011));
//            map2.put("rentalCatNo", String.valueOf(1));
//            map2.put("rentalName", "雙排扣格紋成套西裝");
//            map2.put("rentalPrice", String.valueOf(12500));
//            map2.put("rentalDesPrice", String.valueOf(4000));
//            map2.put("rentalSize", String.valueOf(3));
//            map2.put("rentalColor", "深卡其色");
//            map2.put("rentalInfo", "款式：標準領 單排釦 雙扣。適用場合：婚攝 婚宴 求婚 日常");
//            map2.put("rentalStat", String.valueOf(0));
//            jedis.hmset("member : 2 : cartItem : 5011", map2);


        }

    } // SetToCart 方法結束

    public void deleteFromCart(Integer memNo, List<Integer> rentalNos) {
            // 從連線池取得連線物件
        try(Jedis jedis = jedisPool.getResource()) {
            // 使用 1 號倉庫
            jedis.select(10);
           /*
            * 把要刪除的商品編號轉化成 Redis 裡儲存的 key 的格式，說明如下 :
            * 用 stream() 從 rentalNos 得到 stream 物件 -> 用 map() 遍歷 stream 物件中的各個元素，並執行以下操作 :
            * 取得 stream 物件中每個元素，命名為 rentalNo，接著透過 Lambda 表達式將之轉換為 String 型別，指定格式的 key ->
            * 將裝著已轉換完畢的 key 值的 stream 物件，透過 toArray()，轉換為字串陣列，轉換邏輯詳見 Medium
           */
            String[] keys = rentalNos.stream()
                    .map(rentalNo -> "member : " + memNo + " : cartItem : " + rentalNo)
                    .toArray(String[]::new);
            // 會返回刪除成功的筆數，還沒用到但先寫著備用
            long result = jedis.del(keys);

        } // try 結束

    } // deleteFromCart 方法結束

    public void updateCart(List<Integer> rentalNos, String field, String value) {

        try(Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            String cursor = "0";

            for (Integer rentalNo : rentalNos) {

                do {
                    // 設定掃描參數
                    ScanParams scanParams = new ScanParams().match("member : * : cartItem : " + rentalNo).count(100);
                    // 執行掃描，得到裝著所有符合條件的 key 和 新的 cursor 值的 List
                    ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                    // 得到裝著所有符合條件的 key 的 List
                    List<String> keys = scanResult.getResult();
                    // 對所有符合格式的 key，裡面的 map 裡面的 value 做更新
                    for (String key : keys) {
                        jedis.hset(key, field, value);
                    }
                    // 得到新的 cursor 值
                    cursor = scanResult.getCursor();

                } while (!cursor.equals("0"));

            } // forEach 結束

        } // try 結束

    } // updateCart 方法結束

    public Map<String, Map<String, String>> getFromCart(String memNo) {
        /*
         * new 一個叫做 Results 的 map，裡面每一個 key 值代表一個購物車商品存進 Redis 用的 key，這個 key 的值是一個 map，
         * 每一個 map 代表該商品的資訊，例如 : 價格、顏色...
         */
        Map<String, Map<String, String>> results = new HashMap<>();

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            String cursor = "0";

            do {
                // 對下方要進行的掃描做配置，去掃描任何 key 是以 "member : "+memNo+" : item : " 型式的資料，一次回傳100個左右
                ScanParams scanParams = new ScanParams().match("member : " + memNo + " : cartItem : *").count(100);
                // 使用當前 cursor 和掃描參數執行 scan 方法，返回一個 ScanResult 物件，裡面包含執行完 scan 的新的 cursor 值和找到的 key 值(可能不只一個)
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                // 從 ScanResult 取出查詢到的 key 值們，然後放進 List 裡
                List<String> keys = scanResult.getResult();
                // 每次掃描完 cursor 值會更動，取得更動完的 cursor 值，表示下一次要從哪個值開始
                cursor = scanResult.getCursor();

                // 用裝著所有 key 值的 List 去跑迴圈
                for (String key : keys) {
                    /*
                     * 用每一個 key 值取得所有 value (ex : rentalPrice、rentalInfo...)，然後裝進 Map 裡面，
                     * 所以現在這個叫做 item 的 Map，裡面就是"同一個商品"的很多筆下面這樣的資料 :
                     * rentalPrice : 20000、rentalColor : 白色...
                     */
                    Map<String, String> item = jedis.hgetAll(key);
                    // 把每一個代表一個購物車商品的 key 跟對應的 value -> 裝著商品資訊的 Map 放進 result 這個 Map
                    results.put(key, item);
                }
                // cursor 值回到 0，表示已掃描完所有
            } while (!cursor.equals("0"));

        }
        // 回傳裝有所有購物車商品，跟個別商品的所有資訊的 Map
        return results;

    } // getFromCart 方法結束

} // class 結束
