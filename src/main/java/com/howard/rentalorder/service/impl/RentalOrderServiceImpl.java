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
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

        if (map.containsKey("rentalPayStat")) {
            rentalOrder.setrentalPayStat((Byte) map.get("rentalPayStat"));
        }
        if (map.containsKey("rentalOrdStat")) {
            rentalOrder.setrentalOrdStat((Byte) map.get("rentalOrdStat"));
        }
        if (map.containsKey("rtnStat")) {

            Byte rtnStat = (Byte) map.get("rtnStat");

            if (rtnStat == 1) {

                Set<RentalOrderDetails> details = rentalOrder.getRentalOrderDetailses();
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
    public void createOrder(RentalOrderRequest req) {
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

        List<String> buyItems = req.getBuyItems();
        Set<RentalOrderDetails> details = new HashSet<>();
        for (String buyItem : buyItems) {

            RentalOrderDetails detail = new RentalOrderDetails();
            Rental rental = rentalRepository.findByRentalNo(Integer.valueOf(buyItem));
            detail.setRental(rental);
            detail.setCompositeDetail(new RentalOrderDetails.CompositeDetail(order.getrentalOrdNo(), rental.getRentalNo()));
            detail.setrentalPrice(rental.getRentalPrice());
            detail.setrentalDesPrice(rental.getRentalCategory().getRentalDesPrice());

            // 單一明細加入明細集合
            details.add(detail);
            // 把個別商品改變狀態為1(已預約)
            rental.setRentalStat((byte) 1);

        }
        // 明細放進訂單主體
        order.setRentalOrderDetailses(details);


    } // createOrder 方法結束

    /*----------------------------練習串接綠界api的方法----------------------------------*/

    public String ecpayCheckout() {

        AllInOne all = new AllInOne("");

        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo("testCompany0004");
        obj.setMerchantTradeDate("2024/05/10 08:05:23");
        obj.setTotalAmount("50");
        obj.setTradeDesc("test Description");
        obj.setItemName("TestItem");
        // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
        obj.setReturnURL("<http://211.23.128.214:5000>");
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("<http://192.168.1.37:8080/>");
        String form = all.aioCheckOut(obj, null);

        return form;

    }

}
