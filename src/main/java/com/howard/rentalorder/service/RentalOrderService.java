package com.howard.rentalorder.service;

import com.howard.rentalorder.dto.RentalOrderRequest;
import com.howard.rentalorder.entity.RentalOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RentalOrderService {

    /*------------------------------ 租借訂單的 CRUD -----------------------------------*/

    /**
     * 產生一筆租借訂單
     * @param req 裝有從前端傳來的成立訂單資訊的 dto
     * @param sReq 用來動態取得主機位置的 HttpServletRequest
     * @return
     */
    public String createOrder(RentalOrderRequest req, HttpServletRequest sReq);

    /**
     * 刪除所有明細後，刪除訂單
     * @param rentalOrdNo 訂單號碼
     */
    public void deleteOrder(Integer rentalOrdNo);

    /**
     * 更新訂單狀態
     * @param map 裝者要更新的資料 鍵值對 的 map
     *            (可更新的有 rentalPayStat、rentalOrdStat、rtnStat、rtnRemark、rtnCompensation )
     */
    public void updateOrder(Map<String, Object> map);

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

    /*------------------------------ 租借訂單的 CRUD -----------------------------------*/

    /*--------------------------------- 綠界金流 ---------------------------------------*/

    /**
     * 產生 綠界科技 金流訂單
     * @param order 要結帳的訂單
     * @param itemNames 該筆 訂單明細 中串接起來的品名 (ex:"衣服#褲子#鞋子...")
     * @return 帶有可送出付款請求的 formHTML
     */
    public String ecpayCheckout(RentalOrder order, String itemNames);

    /**
     * 把綠界回傳的交易成功資訊存入 Redis
     * @param infosMap 裝有綠界回傳 交易成功 資訊的 map
     */
    public void setTradeSuccessInfos(Map<String, String> infosMap);

    /**
     * 從 Redis 取得綠界金流訂單的 tradeNo (目前只從 map 取出 tradeNo，未來可擴充去取其他所需參數)
     * @param rentalOrdNo 租借訂單編號
     * @return 綠界對該筆金流訂單的授權碼(tradeNo)
     */
    public String postQueryTradeInfo(Integer rentalOrdNo);

    /**
     * 產生綠界刷退 "押金" 訂單
     * @param order 要刷退的那筆訂單
     * @return 裝有 綠界 對於刷退的回應資訊的 map
     */
    public Map<String, String> refundForEcPay(RentalOrder order);

    /**
     * 產生綠界刷退 "全款" 訂單
     * @param order 要刷退的那筆訂單
     * @return 裝有 綠界 對於刷退的回應資訊的 map
     */
    public Map<String, String> cancelForEcPay(RentalOrder order);

    /*--------------------------------- 綠界金流 ---------------------------------------*/

    /*------------------------------- LinePay金流 -------------------------------------*/

    /**
     * 對 LinePay 發出付款請求
     * @param order 要使用 LinePay 的訂單
     * @param sReq 拿來動態取得主機位置的 HttpServletRequest
     * @return
     */
    public String linePayCheckOut(RentalOrder order, HttpServletRequest sReq);

    /**
     * 確認 LinePay 對 主機 發送的確認付款請求
     * @param nonceForNotRepeat 綠界發送確認付款請求時會帶上的請求參數 -> 產生訂單時傳送給 LinePay 的訂單號碼
     * @return
     */
    public String linePayConfirm(String nonceForNotRepeat);

    /**
     * 產生 LinePay 刷退 "押金" 訂單
     * @param order 要刷退的那筆訂單
     * @return 裝有 LinePay 對於刷退的回應資訊的 map
     */
    public Map<String, String> refundForLinePay(RentalOrder order);

    /**
     * 產生 LinePay 刷退 "全款" 訂單
     * @param order 要刷退的那筆訂單
     * @return 裝有 LinePay 對於刷退的回應資訊的 map
     */
    public Map<String, String> cancelForLinePay(RentalOrder order);

    /**
     * 從 Redis 找到該訂單真正在資料庫的號碼。因為 LinePay 的 orderId 不能重複，
     * 所以在創建訂單時用 UUID 生成隨機字串作為 key，以資料庫真正訂單號碼做為 value，把鍵值對存入 Redis。並在需要真正訂單號碼，用當初生成的隨機字串來找出真正訂單號碼。
     * @param nonce 用 UUID 生成，代表 LinePay 訂單的隨機字串
     * @return 該筆訂單在資料庫裡真正的編號
     */
    public String findTrueRentalOrdNo(String nonce);

    /*------------------------------- LinePay金流 -------------------------------------*/

}
