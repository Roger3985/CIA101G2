package com.howard.rentalorder.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howard.rentalorder.dao.RentalOrderRepository;
import com.howard.rentalorder.dto.*;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;

import javax.servlet.http.HttpServletRequest;
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
    RentalCartServiceImpl cartService;
    @Autowired
    LogisticsStateServiceImpl logisticsStateServiceImpl;

    JedisPool jedisPool = null;
    public RentalOrderServiceImpl() {
        jedisPool = JedisUtil.getJedisPool();
    }

    /*------------------------------ 租借訂單的 CRUD -----------------------------------*/

    @Override
    @Transactional
    public String createOrder(RentalOrderRequest req, HttpServletRequest sReq) {
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

        // 若沒有呼叫綠界金流或Line Pay金流方法(rentalPayMethod = 2，也就是現場付款)，則付款狀態(rentalPayStat)維持 0(未付款)
        if (order.getrentalPayMethod() == (byte) 2) {
            order.setrentalOrdStat((byte) 40);
            return "thankForBuying";
        }
        // 如果選綠界
        if (order.getrentalPayMethod() == (byte) 1) {
            // 拼接綠界成立訂單的商品明細(綠界商品明細規定各品名之間以#區隔)
            String itemNames = String.join("#", rentalNames);
            // 呼叫綠界成立訂單的方法並回傳
            return ecpayCheckout(order, itemNames);
        }
        // 如果不選現場也不選綠界 = 選 line pay
        return linePayCheckOut(order, sReq);

    } // createOrder 方法結束

    @Override
    public void deleteOrder(Integer rentalOrdNo) {

        RentalOrder order = findOrderByOrdNo(rentalOrdNo);
        // 找出明細並刪除
        Set<RentalOrderDetails> details = order.getRentalOrderDetailses();
        detailsRepository.deleteAll(details);
        // 然後刪訂單
        repository.delete(order);

    } // deleteOrder 方法結束

    @Override
    @Transactional
    public void updateOrder(Map<String, Object> map) {

        Integer rentalOrdNo = (Integer) map.get("rentalOrdNo");
        RentalOrder rentalOrder = repository.findById(rentalOrdNo).orElse(null);
        Set<RentalOrderDetails> details = rentalOrder.getRentalOrderDetailses();

        if (map.containsKey("rentalPayStat")) {
            rentalOrder.setrentalPayStat((Byte) map.get("rentalPayStat"));
        }
        if (map.containsKey("rentalOrdStat")) {
            rentalOrder.setrentalOrdStat((Byte) map.get("rentalOrdStat"));
        }
        if (map.containsKey("rentalRealBackDate")) {
            rentalOrder.setrentalRealBackDate((Timestamp) map.get("rentalRealBackDate"));
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
        // 若訂單狀態 = 50(訂單完成)，則改變歸還狀態為 1(已歸還)、改變明細中所有商品狀態為 0(上架)
        if ((byte)map.get("rentalOrdStat") == (byte) 50) {
            rentalOrder.setRtnStat((byte) 1);
            for (RentalOrderDetails detail : details) {
                detail.getRental().setRentalStat((byte) 0);
            }
        }
        repository.save(rentalOrder);

    } // updateOrder 方法結束

    @Override
    public List<RentalOrder> getAll() {
        return repository.findAll();
    } // getAll 方法結束

    @Override
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

    } // getByAttributes 方法結束

    /*------------------------------ 租借訂單的 CRUD -----------------------------------*/

    /*--------------------------------- 綠界金流 ---------------------------------------*/

    @Override
    public String ecpayCheckout(RentalOrder order, String itemNames) {

        if (order.getrentalTakeMethod() == (byte) 1) {
            order.setrentalOrdStat((byte) 30);
        }

        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        // 訂單號碼(規定大小寫英文+數字)
        String merchantTradeNo = uuId;
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

    } // ecpayCheckout 結束

    @Override
    public void setTradeSuccessInfos(Map<String, String> infosMap) {

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

    } // setTradeSuccessInfos 結束

    @Override
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
            return logisticsStateServiceImpl.parseLogisticsInfo(all.queryTradeInfo(obj)).get("TradeNo");

        }

    } // postQueryTradeInfo 結束

    @Override
    public Map<String, String> refundForEcPay(RentalOrder order) {

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
            Map<String, String> refundInfos = logisticsStateServiceImpl.parseLogisticsInfo( all.doAction(obj) );
            // 刷退幾 %
            refundInfos.put("refundPercent", refundPercent);
            return refundInfos;

        }

    } // refund 結束

    @Override
    public Map<String, String> cancelForEcPay(RentalOrder order) {

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
            obj.setTotalAmount( String.valueOf( order.getrentalAllPrice().add(order.getrentalAllDepPrice()) ) );
            // 找出 tradeNo
            String tradeNo = postQueryTradeInfo(order.getrentalOrdNo());
            // 設定綠界的交易編號(tradeNo)
            obj.setTradeNo(tradeNo);
            // 設定欲執行動作為 刷退
            obj.setAction("R");
            // 把回傳的 刷退 資訊重構成好用格式
            Map<String, String> refundInfos = logisticsStateServiceImpl.parseLogisticsInfo( all.doAction(obj) );
            // 刷退幾 %
            refundInfos.put("refundPercent", "100");
            for (RentalOrderDetails detail : order.getRentalOrderDetailses()) {
                rentalRepository.findByRentalNo(detail.getRental().getRentalNo()).setRentalStat((byte) 0);
            }
            return refundInfos;

        }

    } // cancel 結束

    /*--------------------------------- 綠界金流 ---------------------------------------*/

    /*------------------------------- LinePay金流 -------------------------------------*/

    private static final String CHANNEL_ID = "2005342190";
    private static final String CHANNEL_SECRET = "44c865afc4d0e1d4575ea90a87616108";
    private static final String REQUEST_URL = "https://sandbox-api-pay.line.me";
    private static final String REQUEST_URI = "/v3/payments/request";

    @Override
    public String linePayCheckOut(RentalOrder order, HttpServletRequest sReq) {

        if (order.getrentalTakeMethod() == (byte) 1) {
            order.setrentalOrdStat((byte) 30);
        }

        try {
            /*----------------------- 實例化、初始化所需物件和參數 -----------------------*/
            // 設定訂單主體
            ObjectMapper objectMapper = new ObjectMapper();
            CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
            // 付款總金額 (所有 package 的 總租金 + 總押金 加總)
            form.setAmount( order.getrentalAllPrice().add(order.getrentalAllDepPrice()) );
            // 貨幣種類 (還支援 USD、JPY、THB)
            form.setCurrency("TWD");
            // 訂單編號
            String nonce = UUID.randomUUID().toString();
            form.setOrderId(nonce);
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.select(10);
                String key = "nonceForNotRepeat : " + nonce;
                jedis.set( key, String.valueOf(order.getrentalOrdNo()) );
            }
            // 設定 packages
            ProductPackageForm productPackageForm = new ProductPackageForm();
            // package 的 id (packages 為陣列，裡面可裝 1 個以上的 package)(設為 1，因為不曉得分 package 的用途，所以先全部在一個 package)
            productPackageForm.setId("1");
            // package 的名稱 (不知道設甚麼，所以放專案名稱)
            productPackageForm.setName("fallELove");
            // 一個 package 裡所有商品總價 (總租金 + 總押金)
            productPackageForm.setAmount( order.getrentalAllPrice().add(order.getrentalAllDepPrice()) );
            // 設定 products (products 為陣列，裡面可裝 1 個以上的 product)
            List<ProductForm> products = new ArrayList<>();
            for (RentalOrderDetails detail : order.getRentalOrderDetailses()) {
                ProductForm productForm = new ProductForm();
                // 商品 id
                productForm.setId( String.valueOf(detail.getRental().getRentalNo()) );
                // 商品名稱
                productForm.setName( String.valueOf(detail.getRental().getRentalName()) );
                // 商品 img (資料庫空的所以隨便設)
                productForm.setImageUrl("https://static.wikia.nocookie.net/worldpedias/images/0/0f/Patrick_Star.PNG/revision/latest?cb=20140620143247&path-prefix=zh");
                // 商品數量
                productForm.setQuantity(new BigDecimal(1));
                // 商品價格 (個別商品的 租金 + 押金)
                productForm.setPrice( detail.getRentalPrice().add(detail.getRentalDesPrice()) );
                // 商品放進 List 裡
                products.add(productForm);
            }
            // 把 products 放進 packages 裡
            productPackageForm.setProducts(products);
            // 把 packages 放進 訂單主體 裡
            form.setPackages(Arrays.asList(productPackageForm));
            // 設定 url
            RedirectUrls redirectUrls = new RedirectUrls();
            String baseUrl = sReq.getScheme() + "://" + sReq.getServerName() + ":" + sReq.getServerPort() + sReq.getContextPath();
            // 付款完成後跳轉的 url
            redirectUrls.setConfirmUrl(baseUrl + "/frontend/rentalorder/thankForBuying");
            // 取消付款時跳轉的 url
            redirectUrls.setCancelUrl(baseUrl + "/frontend/rentalorder/linePayCancel?rentalOrdNo=" + order.getrentalOrdNo());
            form.setRedirectUrls(redirectUrls);
            /*------------------------------ 產生驗證簽名 ------------------------------*/
            String requestBody = objectMapper.writeValueAsString(form);
            String signature = encrypt(CHANNEL_SECRET, CHANNEL_SECRET + REQUEST_URI + objectMapper.writeValueAsString(form) + nonce);
            /*------------------------------ 產生請求標頭 ------------------------------*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-LINE-ChannelId", CHANNEL_ID);
            headers.set("X-LINE-Authorization-Nonce", nonce);
            headers.set("X-LINE-Authorization", signature);
            /*--------------------- 產生請求主體、送出請求並取得回應 ----------------------*/
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            // 送出請求
            ResponseEntity<String> response = restTemplate.exchange(REQUEST_URL + REQUEST_URI, HttpMethod.POST, entity, String.class);
            /*-------------------------- 基於回應做後續處理 ---------------------------*/
            if (response.getStatusCode() == HttpStatus.OK) {
                // 付款完後把付款狀態改為 1 (已付款)
                order.setrentalPayStat((byte) 1);
                // 從回應物件中獲取支付 URL
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                // 把交易編號(transactionId)存入 Redis
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.select(10);
                    String orderMappingKey = "rentalOrdNoForLinePay : " + order.getrentalOrdNo();
                    jedis.set(orderMappingKey, rootNode.path("info").path("transactionId").asText());
                }
                String paymentUrl = rootNode.path("info").path("paymentUrl").path("web").asText();
                return paymentUrl;
            } else {
                return "/frontend/rentalorder/linePayCancel?rentalOrdNo=" + order.getrentalOrdNo();
            }

        } catch (Exception e) {
            System.out.println("喔是喔真的假的5555555");
        }

        return "/frontend/rentalorder/linePayCancel?rentalOrdNo=" + order.getrentalOrdNo();

    } // linePayCheckOut 結束

    @Override
    public String linePayConfirm(String nonceForNotRepeat) {

        /*-----------------------找出transactionId-----------------------*/
        String transactionId = "";
        Integer rentalOrdNo = null;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(10);
            // 找出真正的 rentalOrdNo
            rentalOrdNo = Integer.valueOf( findTrueRentalOrdNo(nonceForNotRepeat) );
            String key = "rentalOrdNoForLinePay : " + rentalOrdNo;
            transactionId = jedis.get(key);
        }
        /*-----------------------組裝 Request Body-----------------------*/
        RentalOrder order = repository.findById(rentalOrdNo).orElse(null);
        String requestBody = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if (order != null) {
            LinePayConfirm linePayConfirm = new LinePayConfirm();
            linePayConfirm.setAmount(order.getrentalAllPrice().add(order.getrentalAllDepPrice()));
            linePayConfirm.setCurrency("TWD");
            try {
                requestBody = objectMapper.writeValueAsString(linePayConfirm);
            } catch (JsonProcessingException e) {
                System.out.println("轉換linePayConfirm物件出問題喔");
            }
        }
        /*--------------------------組裝驗證簽名--------------------------*/
        String signature = null;
        String nonce = UUID.randomUUID().toString();
        final String REFUND_URI = "/v3/payments/" + transactionId + "/confirm";
        signature = encrypt(CHANNEL_SECRET, CHANNEL_SECRET + REFUND_URI + requestBody + nonce);
        /*----------------------組裝 Request Header----------------------*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", CHANNEL_ID);
        headers.set("X-LINE-Authorization-Nonce", nonce);
        headers.set("X-LINE-Authorization", signature);
        /*---------------------產生請求主體、送出請求並取得回應----------------------*/
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(REQUEST_URL + REFUND_URI, HttpMethod.POST, entity, String.class);
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            System.out.println("轉換回應物件過程中有錯喔喔喔喔");
        }
        return rootNode.path("returnCode").asText();

    } // LinePayConfirm 結束

    @Override
    public Map<String, String> refundForLinePay(RentalOrder order) {
        /*----------------------- 實例化、初始化所需物件和參數 -----------------------*/
        // 用來轉換成 RequestBody 要的格式的自訂物件
        LinePayRefund linePayRefund = new LinePayRefund();
        // 用來放刷退回應訊息的 map
        Map<String, String> refundInfos = new HashMap<>();
        // 用來紀錄刷退幾%的字串
        String refundPercent = null;
        /*--------------- 判斷此筆訂單有無超時，並依照結果設定可刷退多少押金 ---------------*/
        LocalDateTime backTime = order.getrentalBackDate().toLocalDateTime();
        LocalDateTime realBackTime = order.getrentalRealBackDate().toLocalDateTime();
        int daysLate = (int) Duration.between(backTime, realBackTime).toDays();
        switch (daysLate) {
            case 0 : // 晚一天以內，退 100%
                linePayRefund.setRefundAmount(order.getrentalAllDepPrice());
                refundPercent = "100";
                break;
            case 1 : // 晚一天到兩天，退 50%
                linePayRefund.setRefundAmount( order.getrentalAllDepPrice()
                        .multiply(new BigDecimal("0.5"))
                        .setScale(0, RoundingMode.HALF_UP) );
                refundPercent = "50";
                break;
            case 2 : // 晚兩天，退 0%
                linePayRefund.setRefundAmount( order.getrentalAllDepPrice()
                        .divide(order.getrentalAllDepPrice(), 0, RoundingMode.HALF_UP) );
                refundPercent = "0";
                break;
            default : // 晚兩天以上，退 0%
                if (daysLate > 2) {
                    linePayRefund.setRefundAmount( order.getrentalAllDepPrice()
                            .divide(order.getrentalAllDepPrice(), 0, RoundingMode.HALF_UP) );
                    refundPercent = "0";
                }
                break;
        }
        /*----------------------- 送出請求，取得回應後進行後續處理 -----------------------*/
        JsonNode rootNode = sdLinePayRefundReq(linePayRefund, order);
        // 結果代碼 (ex:0000、1198...)
        refundInfos.put("returnCode", rootNode.path("returnCode").asText());
        // 成功訊息或失敗原因
        refundInfos.put("returnMessage", rootNode.path("returnMessage").asText());
        // 刷退幾 %
        refundInfos.put("refundPercent", refundPercent);
        return refundInfos;

    } // refundForLinePay 結束

    @Override
    public Map<String, String> cancelForLinePay(RentalOrder order) {

        /*----------------------- 實例化、初始化所需物件和參數 -----------------------*/
        // 用來轉換成 RequestBody 要的格式的自訂物件
        LinePayRefund linePayRefund = new LinePayRefund();
        // 用來放刷退回應訊息的 map
        Map<String, String> refundInfos = new HashMap<>();
        linePayRefund.setRefundAmount(null);
        /*----------------------- 送出請求，取得回應後進行後續處理 -----------------------*/
        JsonNode rootNode = sdLinePayRefundReq(linePayRefund, order);
        // 結果代碼 (ex:0000、1198...)
        refundInfos.put("returnCode", rootNode.path("returnCode").asText());
        // 成功訊息或失敗原因
        refundInfos.put("returnMessage", rootNode.path("returnMessage").asText());
        // 刷退幾 %
        refundInfos.put("refundPercent", "100");
        for (RentalOrderDetails detail : order.getRentalOrderDetailses()) {
            rentalRepository.findByRentalNo(detail.getRental().getRentalNo()).setRentalStat((byte) 0);
        }
        return refundInfos;

    } // cancelForLinePay 結束

    @Override
    public String findTrueRentalOrdNo(String nonce) {

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(10);
            String key = "nonceForNotRepeat : " + nonce;
            return jedis.get(key);
        }

    } // findTrueRentalOrdNo 結束

    /*------------------------------- LinePay金流 -------------------------------------*/

    /*--------------------------------所有方法共用-------------------------------------*/

    // 用真實訂單號碼找出訂單
    public RentalOrder findOrderByOrdNo(Integer rentalOrdNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        return getByAttributes(map).get(0);
    }

    // 把資料轉換成指定格式字串
    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }

    // 轉換成 Base64 字串
    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }

    /**
     * 對 LinePay 送出刷退請求，並解析回應物件後回傳
     * @param linePayRefund 裝有刷退所需資訊的物件
     * @param order 要執行刷退的訂單
     * @return 解析完的 LinePay 回傳的回應
     */
    public JsonNode sdLinePayRefundReq(LinePayRefund linePayRefund, RentalOrder order) {

        String transactionId = null;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(10);
            // 從 redis 取出對應的 LinePay 訂單號
            String orderMappingKey = "rentalOrdNoForLinePay : " + order.getrentalOrdNo();
            transactionId = jedis.get(orderMappingKey);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String signature = null;
        String requestBody = null;
        final String REFUND_URI = "/v3/payments/" + transactionId + "/refund";
        String nonce = UUID.randomUUID().toString();

        try {
            requestBody = objectMapper.writeValueAsString(linePayRefund);
        } catch (JsonProcessingException e) {
            System.out.println("轉換linePayRefund物件出問題喔");
        }
        signature = encrypt(CHANNEL_SECRET, CHANNEL_SECRET + REFUND_URI + requestBody + nonce);
        /*------------------------------ 產生請求標頭 ------------------------------*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", CHANNEL_ID);
        headers.set("X-LINE-Authorization-Nonce", nonce);
        headers.set("X-LINE-Authorization", signature);
        /*--------------------- 產生請求主體、送出請求並取得回應 ----------------------*/
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(REQUEST_URL + REFUND_URI, HttpMethod.POST, entity, String.class);
        /*---------------------------- 解析回應物件 -----------------------------*/
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            System.out.println("轉換回應物件過程中有錯喔喔喔喔");
        }
        return rootNode;

    }

    /*--------------------------------所有方法共用-------------------------------------*/

}
