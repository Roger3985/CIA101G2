package com.howard.rentalorder.service.impl;

import com.roger.member.repository.MemberRepository;
import com.howard.rentalorder.dao.RentalOrderRepository;
import com.yu.rental.dao.RentalRepository;
import com.howard.rentalorder.dto.RentalOrderRequest;
import com.howard.rentalorder.service.RentalOrderService;
import com.howard.rentalorderdetails.dao.RentalOrderDetailsRepository;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorderdetails.entity.RentalOrderDetails;
import com.roger.member.entity.Member;
import com.yu.rental.entity.Rental;
import ecpay.logistics.integration.domain.CreateCVSObj;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Transactional
    public void update(Map<String, Object> map) {

        Integer rentalOrdNo = (Integer) map.get("rentalOrdNo");
        RentalOrder rentalOrder = repository.findById(rentalOrdNo).orElse(null);
        Set<RentalOrderDetails> details = rentalOrder.getRentalOrderDetailses();

        if (map.containsKey("rentalPayStat")) {
            rentalOrder.setrentalPayStat((Byte) map.get("rentalPayStat"));
        }
        if (map.containsKey("rentalOrdStat")) {

            if ((byte)map.get("rentalOrdStat") == (byte) 50) {
                rentalOrder.setRtnStat((byte) 1);
            }

            rentalOrder.setrentalOrdStat((Byte) map.get("rentalOrdStat"));
        }
        if (map.containsKey("rtnStat")) {

            Byte rtnStat = (Byte) map.get("rtnStat");

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
        // 拼接綠界成立訂單的商品明細(綠界商品明細規定各品名之間以#區隔)
        String itemNames = String.join("#", rentalNames);
        // 呼叫綠界成立訂單的方法並回傳
        return ecpayCheckout(order, itemNames);

    } // createOrder 方法結束

    /*----------------------------練習串接綠界api的方法----------------------------------*/

    public String ecpayCheckout(RentalOrder order, String itemNames) {

        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        // 訂單號碼(規定大小寫英文+數字)
        obj.setMerchantTradeNo( "Member" + order.getMember().getMemName() + order.getrentalOrdNo() + "test" );
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
        obj.setReturnURL("<http://211.23.128.214:5000>");
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("http://localhost:8080/backend/rentalorder/rentalCart"); // 問小吳上雲怕爆開(路徑問題)
        String form = all.aioCheckOut(obj, null);

        // 付款完後把付款狀態改為 1 (已付款)
        order.setrentalPayStat((byte) 1);

        return form;

    }

    public String shipping(Integer rentalOrdNo, Timestamp rentalOrdTime, BigDecimal rentalAllDepPrice
                            , String rentalRcvName) { // 出貨

        AllInOne all = new AllInOne("");
        CreateCVSObj obj = new CreateCVSObj();
        obj.setMerchantTradeNo( String.valueOf(rentalOrdNo) );
        obj.setMerchantTradeDate( String.valueOf(rentalOrdTime) );
        obj.setLogisticsType("HOME");
        obj.setLogisticsSubType("TCAT");
        obj.setGoodsAmount( String.valueOf(rentalAllDepPrice) );
        obj.setSenderName( "howard" );
        obj.setReceiverName( rentalRcvName );
        obj.setServerReplyURL( "http://localhost:8080/backend/index" );
        obj.setClientReplyURL( "http://localhost:8080/backend/index" );



        return "";
    }


}
