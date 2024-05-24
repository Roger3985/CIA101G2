package com.howard.rentalorder.service.impl;

import com.howard.rentalorder.dao.RentalOrderRepository;
import com.howard.rentalorder.dto.RentalOrderRequest;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.service.RentalOrderService;
import com.howard.rentalorderdetails.dao.RentalOrderDetailsRepository;
import com.howard.rentalorderdetails.entity.RentalOrderDetails;
import com.howard.util.JedisUtil;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.DoActionObj;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class RentalOrderServiceImpl implements RentalOrderService {

    @Autowired
    private RentalOrderRepository repository;

    @Autowired
    private RentalOrderDetailsRepository detailsRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    LogisticsStateService logisticsStateService;

    JedisPool jedisPool = null;

    public RentalOrderServiceImpl() {
        jedisPool = JedisUtil.getJedisPool();
    }

    @Transactional
    public void update(Map<String, Object> map) {

        Integer rentalOrdNo = (Integer) map.get("rentalOrdNo");
        RentalOrder rentalOrder = repository.findById(rentalOrdNo).orElse(null);
        Set<RentalOrderDetails> details = rentalOrder.getRentalOrderDetailses();

        if (map.containsKey("rentalPayStat")) {
            rentalOrder.setrentalPayStat((Byte) map.get("rentalPayStat"));
        }
        if (map.containsKey("rentalOrdStat")) {
            // 若訂單狀態 = 50(訂單完成)，則改變歸還狀態為 1(已歸還)、改變明細中所有商品狀態為 0(上架)
            if ((byte)map.get("rentalOrdStat") == (byte) 50) {
                rentalOrder.setRtnStat((byte) 1);
                for (RentalOrderDetails detail : details) {
                    detail.getRental().setRentalStat((byte) 0);
                }
            }

            rentalOrder.setrentalOrdStat((Byte) map.get("rentalOrdStat"));
        }
        if (map.containsKey("rtnStat")) {

            Byte rtnStat = (Byte) map.get("rtnStat");
            // 如果歸還狀態 = 1(已歸還)
            if (rtnStat == 1) {

                for (RentalOrderDetails detail : details) {
                    detail.getRental().setRentalStat((byte) 3);

                }
                rentalOrder.setRentalOrderDetailses(details);

            }

            rentalOrder.setRtnStat(rtnStat);
        }
        if (map.containsKey("rtnRemark")) {
            rentalOrder.setRtnRemark((String) map.get("rtnRemark"));
        }
        if (map.containsKey("rtnCompensation")) {
            rentalOrder.setRtnCompensation((BigDecimal) map.get("rtnCompensation"));
        }
        repository.save(rentalOrder);

    }

    @Override
    public List<RentalOrder> getAll() {
        return repository.findAll();
    }

    public List<RentalOrder> getByAttributes(Map<String, Object> map) {

        if (map.isEmpty()) {
            return repository.findAll();
        }

        Integer rentalOrdNo = null;
        Integer memNo = null;
        String rentalByrName = null;
        String rentalByrPhone = null;
        String rentalByrEmail = null;
        String rentalRcvName = null;
        String rentalRcvPhone = null;
        Byte rentalTakeMethod = null;
        String rentalAddr = null;
        Byte rentalPayMethod = null;
        BigDecimal rentalAllPrice = null;
        BigDecimal rentalAllDepPrice = null;
        Timestamp rentalOrdTime = null;
        Timestamp rentalDate = null;
        Timestamp rentalBackDate = null;
        Timestamp rentalRealBackDate = null;
        Byte rentalPayStat = null;
        Byte rentalOrdStat = null;
        Byte rtnStat = null;
        String rtnRemark = null;
        BigDecimal rtnCompensation = null;
        if (map.containsKey("rentalOrdNo")) {
            rentalOrdNo = (Integer) map.get("rentalOrdNo");
        }
        if (map.containsKey("memNo")) {
            memNo = (Integer) map.get("memNo");
        }
        if (map.containsKey("rentalByrName")) {
            rentalByrName = (String) map.get("rentalByrName");
        }
        if (map.containsKey("rentalByrPhone")) {
            rentalByrPhone = (String) map.get("rentalByrPhone");
        }
        if (map.containsKey("rentalByrEmail")) {
            rentalByrEmail = (String) map.get("rentalByrEmail");
        }
        if (map.containsKey("rentalRcvName")) {
            rentalRcvName = (String) map.get("rentalRcvName");
        }
        if (map.containsKey("rentalRcvPhone")) {
            rentalRcvPhone = (String) map.get("rentalRcvPhone");
        }
        if (map.containsKey("rentalTakeMethod")) {
            rentalTakeMethod = (Byte) map.get("rentalTakeMethod");
        }
        if (map.containsKey("rentalAddr")) {
            rentalAddr = (String) map.get("rentalAddr");
        }
        if (map.containsKey("rentalPayMethod")) {
            rentalPayMethod = (Byte) map.get("rentalPayMethod");
        }
        if (map.containsKey("rentalAllPrice")) {
            rentalAllPrice = (BigDecimal) map.get("rentalAllPrice");
        }
        if (map.containsKey("rentalAllDepPrice")) {
            rentalAllDepPrice = (BigDecimal) map.get("rentalAllDepPrice");
        }
        if (map.containsKey("rentalOrdTime")) {
            rentalOrdTime = (Timestamp) map.get("rentalOrdTime");
        }
        if (map.containsKey("rentalDate")) {
            rentalDate = (Timestamp) map.get("rentalDate");
        }
        if (map.containsKey("rentalBackDate")) {
            rentalBackDate = (Timestamp) map.get("rentalBackDate");
        }
        if (map.containsKey("rentalRealBackDate")) {
            rentalRealBackDate = (Timestamp) map.get("rentalRealBackDate");
        }
        if (map.containsKey("rentalPayStat")) {
            rentalPayStat = (Byte) map.get("rentalPayStat");
        }
        if (map.containsKey("rentalOrdStat")) {
            rentalOrdStat = (Byte) map.get("rentalOrdStat");
        }
        if (map.containsKey("rtnStat")) {
            rtnStat = (Byte) map.get("rtnStat");
        }
        if (map.containsKey("rtnRemark")) {
            rtnRemark = (String) map.get("rtnRemark");
        }
        if (map.containsKey("rtnCompensation")) {
            rtnCompensation = (BigDecimal) map.get("rtnCompensation");
        }

        return repository.findByAttributes(rentalOrdNo, memNo, rentalByrName, rentalByrPhone, rentalByrEmail,
                rentalRcvName, rentalRcvPhone, rentalTakeMethod, rentalAddr, rentalPayMethod, rentalAllPrice,
                rentalAllDepPrice, rentalOrdTime, rentalDate, rentalBackDate, rentalRealBackDate, rentalPayStat,
                rentalOrdStat, rtnStat, rtnRemark, rtnCompensation);

    }

    @Transactional
    public String createOrder(RentalOrderRequest req) {
        // 新增訂單
        RentalOrder order = new RentalOrder();
        Optional<Member> members = memberRepository.findById(req.getMemNo());
        Member member = members.orElse(null);
        order.setMember(member);
        order.setrentalByrName(req.getrentalByrName());
        order.setrentalByrPhone(req.getrentalByrPhone());
        order.setrentalByrEmail(req.getrentalByrEmail());
        order.setrentalRcvName(req.getrentalRcvName());
        order.setrentalRcvPhone(req.getrentalRcvPhone());
        order.setrentalTakeMethod(req.getrentalTakeMethod());
        if (req.getrentalTakeMethod() == (byte) 1) {
            req.setrentalAddr(null);
        }
        order.setrentalAddr(req.getrentalAddr());
        order.setrentalPayMethod(req.getrentalPayMethod());
        order.setrentalAllPrice(req.getrentalAllPrice());
        order.setrentalAllDepPrice(req.getrentalAllDepPrice());
        order.setrentalOrdTime(req.getrentalOrdTime());
        order.setrentalDate(req.getrentalDate());
        order.setrentalBackDate(req.getrentalBackDate());
        order.setrentalRealBackDate(req.getrentalRealBackDate());
        order.setrentalPayStat(req.getrentalPayStat());
        order.setrentalOrdStat(req.getrentalOrdStat());
        order.setRtnStat(req.getRtnStat());
        order.setRtnRemark(req.getRtnRemark());

        repository.save(order);
        // 取出所有租借的商品的 rentalNo
        List<String> buyItems = req.getBuyItems();
        // 拿來裝準備 set 進 order 裡的明細的 HashSet
        Set<RentalOrderDetails> details = new HashSet<>();
        // 拿來裝明細裡所有品名的 ArrayList
        List<String> rentalNames = new ArrayList<>();

        for (String buyItem : buyItems) {

            RentalOrderDetails detail = new RentalOrderDetails();
            Rental rental = rentalRepository.findByRentalNo(Integer.valueOf(buyItem));
            detail.setRental(rental);
            detail.setCompositeDetail(new RentalOrderDetails.CompositeDetail(order.getrentalOrdNo(), rental.getRentalNo()));
            detail.setrentalPrice(rental.getRentalPrice());
            detail.setrentalDesPrice(rental.getRentalCategory().getRentalDesPrice());

            // 單一明細加入明細集合
            details.add(detail);
            // 把個別商品名稱加入集合
            rentalNames.add(rental.getRentalName());
            // 把個別商品改變狀態為1(已預約)
            rental.setRentalStat((byte) 1);

        }
        // 明細放進訂單主體
        order.setRentalOrderDetailses(details);


        // 若沒有呼叫綠界金流方法(rentalPayMethod = 2，也就是現場付款)，則付款狀態(rentalPayStat)維持 0(未付款)
        if (order.getrentalPayMethod() == (byte) 2) {
            order.setrentalOrdStat((byte) 30);
            return "thankForBuying";
        }

        // 拼接綠界成立訂單的商品明細(綠界商品明細規定各品名之間以#區隔)
        String itemNames = String.join("#", rentalNames);
        // 呼叫綠界成立訂單的方法並回傳
        return ecpayCheckout(order, itemNames);

    } // createOrder 方法結束

    /*----------------------------串接綠界金流 api 的方法----------------------------------*/

    /**
     * 結帳產生金流訂單
     * @param order 要結帳的訂單
     * @param itemNames 該筆 訂單明細 中串接起來的品名(ex:"衣服#褲子#鞋子...")
     * @return 帶有可送出付款請求的 formHTML
     */
    public String ecpayCheckout(RentalOrder order, String itemNames) {

        if (order.getrentalTakeMethod() == (byte) 1) {
            order.setrentalOrdStat((byte) 30);
        }

        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        // 訂單號碼(規定大小寫英文+數字)
        String merchantTradeNo = "Member" + order.getMember().getMemName() + order.getrentalOrdNo() + "TTT";
        obj.setMerchantTradeNo( merchantTradeNo );
        // 交易時間(先把毫秒部分切掉)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        obj.setMerchantTradeDate( sdf.format(order.getrentalOrdTime()) );
        // 總金額(總金額 + 總押金)
        obj.setTotalAmount( String.valueOf( order.getrentalAllPrice().add(order.getrentalAllDepPrice()) ) );
        // 交易描述(沒改)
        obj.setTradeDesc("test Description");
        // 交易商品明細
        obj.setItemName(itemNames);
        // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
        obj.setReturnURL("https://adf4-2001-b400-e389-697b-a1af-a7e7-7925-d3ce.ngrok-free.app/backend/rentalorder/receiveTradeInfos");
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("http://localhost:8080/frontend/rentalorder/thankForBuying?rentalOrdNo=" + order.getrentalOrdNo()); // 問小吳上雲怕爆開(路徑問題)
        String form = all.aioCheckOut(obj, null);

        // 付款完後把付款狀態改為 1 (已付款)
        order.setrentalPayStat((byte) 1);
        // 把 綠界訂單號 跟 自家訂單號 存進 redis，因為命名不同，刷退時要參考
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(10);
            String orderMappingKey = "rentalOrdNo : " + order.getrentalOrdNo();
            jedis.set(orderMappingKey, merchantTradeNo);
        }
        return form;

    } // 結帳產生金流訂單 結束

    /**
     * 存入綠界回傳的交易成功資訊
     * @param infosMap 裝有綠界回傳 交易成功 資訊的 map
     */
    public void setTradeSuccessInfos(Map<String, String> infosMap) {
//        System.out.println("有進來service方法喔喔喔喔喔");
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(10);
            String cursor = "0";
            // 拿來拼接 redis 裡 key 值的字串
            String rentalOrdNo = "";
            // 從 redis 找出該筆訂單的 merchantTradeNo
            ScanParams scanParams = new ScanParams().match("rentalOrdNo : *").count(100);
            // 取得所有符合的 key
            List<String> keys = jedis.scan(cursor, scanParams).getResult();
            // 所有 key 去跑迴圈，試圖找到該筆訂單的 rentalOrdNo
            for (String key : keys) {
                // 如果 key 值跟綠界回傳的 merchantTradeNo 一樣，就找到所要的訂單號碼了
                if ( jedis.get(key).matches(infosMap.get("MerchantTradeNo")) ) {
                    // 把找到的 key 值(ex: "rentalOrdNo : 15")以 ": " 為分水嶺切成兩辦，然後取後半段，得到該訂單的 rentalOrdNo
                    rentalOrdNo = key.split(": ", 2)[1];
                }
            }
            String key = "tradeSuccessInfos for rentalOrdNo : " + rentalOrdNo;
            jedis.hmset(key, infosMap);
        }

    } // 存入綠界回傳的交易成功資訊 結束

    /**
     * 查詢綠界金流訂單的 tradeNo(目前只從 map 取出 tradeNo，未來可擴充去取其他所需參數)
     * @param rentalOrdNo 租借訂單編號
     * @return 綠界對該筆金流訂單的授權碼(tradeNo)
     */
    public String postQueryTradeInfo(Integer rentalOrdNo) {

        try (Jedis jedis = jedisPool.getResource()) {

            jedis.select(10);
            // 找出該訂單的 merchantTradeNo
            String merchantTradeNo = jedis.get("rentalOrdNo : " + rentalOrdNo);
            // 查詢
            AllInOne all = new AllInOne("");
            QueryTradeInfoObj obj = new QueryTradeInfoObj();
            obj.setMerchantTradeNo(merchantTradeNo);
            // 把從綠界回傳的 訂單資訊字串 重構成 map，然後回傳從 map 找到的該訂單的 TradeNo
            return logisticsStateService.parseLogisticsInfo(all.queryTradeInfo(obj)).get("TradeNo");

        }

    }

    /**
     * 產生刷退押金訂單
     * @param order 要刷退的那筆訂單
     * @return 裝有 綠界對於刷退的回應資訊的 map
     */
    public Map<String, String> refund(RentalOrder order) {

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(10);
            // 實例化 刷退 所需物件
            AllInOne all = new AllInOne("");
            DoActionObj obj = new DoActionObj();
            String refundPercent = null;

            // 從 redis 取出對應的 綠界訂單號(因為綠界對訂單號有自己的規定，所以跟資料庫訂單號不同命名)
            String orderMappingKey = "rentalOrdNo : " + order.getrentalOrdNo();
            String merchantTradeNo = jedis.get(orderMappingKey);
            // 設定在綠界，該筆訂單之訂單編號
            obj.setMerchantTradeNo(merchantTradeNo);

            // 判斷此筆訂單有無超時，並依照結果設定可刷退多少押金
            LocalDateTime backTime = order.getrentalBackDate().toLocalDateTime();
            LocalDateTime realBackTime = order.getrentalRealBackDate().toLocalDateTime();
            int daysLate = (int) Duration.between(backTime, realBackTime).toDays();
            switch (daysLate) {

                case 0 : // 晚一天以內，退 100%
                    obj.setTotalAmount( String.valueOf(order.getrentalAllDepPrice()) );
                    refundPercent = "100";
                    break;
                case 1 : // 晚一天到兩天，退 50%
                    obj.setTotalAmount( String.valueOf( order.getrentalAllDepPrice()
                                                             .multiply(new BigDecimal("0.5"))
                                                             .setScale(0, RoundingMode.HALF_UP) ) );
                    refundPercent = "50";
                    break;
                case 2 : // 晚兩天，退 0%
                    obj.setTotalAmount( String.valueOf(order.getrentalAllDepPrice()
                                                            .divide(order.getrentalAllDepPrice(), 0, RoundingMode.HALF_UP) ) );

                    refundPercent = "0";
                    break;
                default : // 晚兩天以上，退 0%
                    if (daysLate > 2) {
                        obj.setTotalAmount( String.valueOf(order.getrentalAllDepPrice()
                                                                .divide(order.getrentalAllDepPrice(), 0, RoundingMode.HALF_UP) ) );

                        refundPercent = "0";
                    }
                    break;

            }
            // 找出 tradeNo
            String tradeNo = postQueryTradeInfo(order.getrentalOrdNo());
            // 設定綠界的交易編號(tradeNo)
            obj.setTradeNo(tradeNo);
            // 設定欲執行動作為 刷退
            obj.setAction("R");
            // 把回傳的 刷退 資訊重構成好用格式
            Map<String, String> refundInfos = logisticsStateService.parseLogisticsInfo( all.doAction(obj) );
            // 刷退幾 %
            refundInfos.put("refundPercent", refundPercent);
            return refundInfos;

        }

    } // 產生刷退押金訂單 結束

}
