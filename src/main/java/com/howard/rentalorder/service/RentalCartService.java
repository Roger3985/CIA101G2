package com.howard.rentalorder.service;

import java.util.List;
import java.util.Map;

public interface RentalCartService {

    /**
     * 把商品加入購物車 (存進 Redis)
     * @param memNo 會員編號
     * @param map 裝著商品資訊的 map
     */
    public void setToCart(Integer memNo, Map<String, String> map);

    /**
     * 從購物車刪除商品 (刪除 Redis 裡商品資訊)
     * @param memNo 會員編號
     * @param rentalNos 裝著所有要刪除的商品的 rentalNo(租借品編號) 的 List
     */
    public void deleteFromCart(Integer memNo, List<Integer> rentalNos);

    /**
     * 更新 Redis 裡購物車商品的資訊
     * @param rentalNos 裝著所有要更新的商品的 rentalNo(租借品編號) 的 List
     * @param field 要更新 value 的 key 值 (ex:更新"商品狀態"為 0，則 field = 商品狀態)
     * @param value 要更新的 value (ex:更新"商品狀態"為 0，則 value = 0)
     */
    public void updateCart(List<Integer> rentalNos, String field, String value);

    /**
     * 從 Redis 取得會員購物車裡所有商品資訊
     * @param memNo 會員編號
     * @return 裝著所有商品資訊的 map
     */
    public Map<String, Map<String, String>> getFromCart(String memNo);

}
