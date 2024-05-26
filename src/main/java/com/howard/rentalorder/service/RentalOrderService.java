package com.howard.rentalorder.service;

import com.howard.rentalorder.entity.RentalOrder;

import java.util.List;
import java.util.Map;

public interface RentalOrderService {

    /**
     * 刪除所有明細後，刪除訂單
     * @param rentalOrdNo 訂單號碼
     */
    public void deleteOrder(Integer rentalOrdNo);

    /**
     * 取得所有租借訂單
     * @return 資料庫裡所有租借訂單
     */
    public List<RentalOrder> getAll();

    /**
     * 對租借訂單做複合查詢
     * @param map 裝著搜尋條件的 map (可用租借訂單所有屬性來查)
     * @return 裝著所有符合條件的 租借訂單 的 List
     */
    public List<RentalOrder> getByAttributes(Map<String, Object> map);

    /**
     * 更新訂單狀態
     * @param map 裝者要更新的資料 鍵值對 的 map
     *            (可更新的有 rentalPayStat、rentalOrdStat、rtnStat、rtnRemark、rtnCompensation )
     */
    public void update(Map<String, Object> map);

}
