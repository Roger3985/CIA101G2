package com.howard.rentalorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howard.rentalorder.dto.*;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.service.impl.LogisticsStateService;
import com.howard.rentalorder.service.impl.RentalCartServiceImpl;
import com.howard.rentalorder.service.impl.RentalOrderServiceImpl;
import com.howard.rentalorder.service.impl.RentalOrderShippingService;
import com.howard.rentalorderdetails.entity.RentalOrderDetails;
import com.howard.rentalorderdetails.service.impl.RentalOrderDetailsServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.utils.JsonUtil.toJson;


@Controller
@RequestMapping("/frontend/rentalorder")
@CrossOrigin(origins = "http://localhost:8080")
public class FrontendRentalOrderController {

    /*--------------------------所有方法共用-------------------------------*/

    @Autowired
    private LogisticsStateService logisticsStateService;

    @Autowired
    private RentalOrderShippingService shippingService;

    @Autowired
    private RentalOrderServiceImpl service;

    @Autowired
    private RentalCartServiceImpl cartService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RentalOrderDetailsServiceImpl detailsService; // 加入購物車用的

    @ModelAttribute("orderList")
    protected List<RentalOrder> getAllData() {
        List<RentalOrder> list = service.getAll();
        list.sort(Comparator.comparing(RentalOrder::getrentalOrdNo).reversed());
        return list;
    }

    @ModelAttribute("rentalList")
    protected  List<Rental> getAllRental() {
        return rentalRepository.findAll();
    }

    @ModelAttribute("memberList")
    protected  List<Member> getAllMember() {
        return memberRepository.findAll();
    }

    String toLogin = "/frontend/member/loginMember";

    private void setInitArgForCreateOrder(RentalOrderRequest order) {
        // 下單時間 = 現在
        order.setrentalOrdTime(new Timestamp(System.currentTimeMillis()));
        // 預計租借日期 = 現在 (此為初步實作，之後由到貨狀態決定)
        order.setrentalDate(new Timestamp(System.currentTimeMillis()));
        /*
         * 預計歸還日期(rentalBackDate)，邏輯如下 :
         * 方案(rentStat) = 1 ? 歸還日期 = 現在 + 7天 : 歸還日期 = 現在 + 14天
         */
        int rentSet1 = 86400;
        if (Integer.valueOf(order.getRentSet()) == 1) {
            order.setrentalBackDate(new Timestamp(System.currentTimeMillis() + rentSet1 * 7));
        } else {
            order.setrentalBackDate(new Timestamp(System.currentTimeMillis() + rentSet1 * 14));
        }
        // 實際歸還日期(因為資料庫設定NotNull，所以先設定為現在)
        order.setrentalRealBackDate(new Timestamp(System.currentTimeMillis()));
        // 付款狀態先設定 = 0(未付款)
        order.setrentalPayStat((byte) 0);
        // 訂單狀態 = 10(撿貨中)
        order.setrentalOrdStat((byte) 10);
        // 歸還狀態 = 0(未歸還)
        order.setRtnStat((byte) 0);
        // 歸還註記(因為資料庫設定NotNull，所以先設定為"尚未歸還")
        order.setRtnRemark("尚未歸還");
    }

    /*--------------------------所有方法共用-------------------------------*/

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    // 去 前台 租借品追蹤 頁面
    @GetMapping("/toRentalMyTrack")
    public String toRentalMyTrack(HttpSession session) {
        if (session.getAttribute("loginsuccess") == null) {
            return "redirect:" + toLogin;
        }
        return "/frontend/rental/rentalMyTrack";
    }

    // 去 感謝付款 頁面
    @GetMapping("/thankForBuying")
    public String thankForBuying(@RequestParam(required = false) Integer rentalOrdNo,
                                 @RequestParam(required = false) Integer orderId, ModelMap model) {
        model.addAttribute("rentalOrdNo", rentalOrdNo);
        model.addAttribute("orderId", orderId);
        return "/frontend/rental/thankForBuying";
    }

    // 去 付款已取消 頁面
    @GetMapping("/linePayCancel")
    public String linePayCancel(@RequestParam(required = false) Integer rentalOrdNo, ModelMap model) {
        // 重新加回購物車
        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        RentalOrder order = service.getByAttributes(map).get(0);
        List<RentalOrderDetails> details = detailsService.getByAttributes(map);
        SetToCart setToCart = new SetToCart();
        setToCart.setMemNo(order.getMember().getMemNo());
        for (RentalOrderDetails detail : details) {
            assembleAndSet(setToCart, detail.getRental());
        }
        service.deleteOrder(rentalOrdNo);
        return "frontend/rental/linePayCancel";
    }

    // 去 會員租借訂單 頁面
    @GetMapping("/toMemberRentalOrders")
    public String toMemberRentalOrders(HttpSession session, ModelMap model) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return "redirect:" + toLogin;
        }
        model.addAttribute("member", member);
        return "/frontend/rental/memberrentalorders";
    }

    // 去 單一租借訂單 頁面
    @GetMapping("/toMemberRentalOrder")
    public String toMemberRentalOrder(@RequestParam Integer rentalOrdNo, ModelMap model, HttpSession session) {
        if (session.getAttribute("loginsuccess") == null) {
            return "redirect:" + toLogin;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        RentalOrder rentalOrder = service.getByAttributes(map).get(0);
        model.addAttribute("rentalOrder", rentalOrder);
        return "/frontend/rental/memberRentalOrder";
    }

    // 去 租借商城 首頁
    @GetMapping("/toRentalShop")
    public String toRentalShop() {
        return "/frontend/rental/rentalShop";
    }

    // 去 租借商城的購物車 畫面
    @GetMapping("/rentalCart")
    public String toRentalCart(HttpSession session) {
        if (session.getAttribute("loginsuccess") == null) {
            return "redirect:" + toLogin;
        }
        return "/frontend/rental/rentalCart";
    }

    // 去 租借商城的結帳 頁面
    @GetMapping("/toRentalPayment")
    public String toRentalPayment(HttpSession session) {
        if (session.getAttribute("loginsuccess") == null) {
            return "redirect:" + toLogin;
        }
        return "/frontend/rental/rentalPayment";
    }

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    /*---------------------------處理CRUD請求的方法---------------------------------*/

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody @Valid RentalOrderRequest order,
                                         HttpSession session, HttpServletRequest sReq) {

        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        /*-------------------------創建訂單時，設定初始參數-------------------------*/
        setInitArgForCreateOrder(order);
        /*-------------------------執行創建訂單流程-------------------------*/
        /**
         * 創建訂單
         * @return 若有線上付款，回傳包含請求綠界付款畫面之字串，否則回傳感謝購買頁面路徑
         */
        order.setMemNo(member.getMemNo());
        String form = service.createOrder(order, sReq);
        // 先把字串陣列轉成整數陣列(因為service層方法需要List<Integer>)
        List<Integer> rentalNoList = order.getBuyItems().stream()
                .map(Integer::parseInt)
                .toList();
        // 把購物車清空
        cartService.deleteFromCart(member.getMemNo(), rentalNoList);
        // 更新 Redis 裡其他購物車資料，有加入明細裡的商品的，租借品狀態改為 1 (已預約)
        cartService.updateCart(rentalNoList, "rentalStat", "1");
        // 回傳帶有付款畫面 html 的 form 字串
        return ResponseEntity.status(HttpStatus.CREATED).body(form);

    }

    // 接收綠界金流交易回傳參數
    @PostMapping("/receiveTradeInfos")
    public ResponseEntity<?> receiveTradeInfos(@RequestBody String infos) {

        Map<String, String> infosMap = logisticsStateService.parseLogisticsInfo(infos);
        service.setTradeSuccessInfos(infosMap);
        return ResponseEntity.status(HttpStatus.OK).body("1|OK");

    }

    // 取消訂單
    @PostMapping("/tryCancelOrder")
    public ResponseEntity<?> tryCancelOrder(@RequestBody Integer rentalNo, HttpSession session) {

        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }



        return ResponseEntity.status(HttpStatus.OK).body("ok");

    }

    // 給 會員所有訂單頁面 用的 getOnAny
    @GetMapping("/getOnAnyForOrdersPage")
    public String getOnAnyForOrdersPage(@RequestParam(required = false) Byte rentalOrdStat, HttpSession session,
                                        ModelMap model) {

        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return "redirect:" + toLogin;
        }
        Map<String, Object> map = new HashMap<>();

        if (rentalOrdStat != null) {
            map.put("rentalOrdStat", rentalOrdStat);
        }
        List<RentalOrder> orderList = service.getByAttributes(map);
        orderList.sort(Comparator.comparing(RentalOrder::getrentalOrdNo).reversed());
        model.addAttribute("orderList", orderList);
        model.addAttribute("member", member);
        return "frontend/rental/memberrentalorders";

    }

    /*---------------------------處理CRUD請求的方法---------------------------------*/

    /*----------------------------有關購物車的方法----------------------------------*/

    // 加入購物車
    @PostMapping("/setToCart")
    public ResponseEntity<?> setToCart(@RequestBody SetToCart setToCart, HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        Rental rental = rentalRepository.findByRentalNo(setToCart.getRentalNo());
        setToCart.setMemNo(member.getMemNo());
        assembleAndSet(setToCart, rental);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental.getRentalName());

    }

    // 再買一次
    @PostMapping("/buyAgain")
    public ResponseEntity<?> buyAgain(@RequestBody SetToCart setToCart, HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        setToCart.setMemNo(member.getMemNo());
        for (Integer rentalNo : setToCart.getRentalNos()) {
            Rental rental = rentalRepository.findByRentalNo(rentalNo);
            assembleAndSet(setToCart, rental);
        }
        return ResponseEntity.status(HttpStatus.OK).body("rentalCart");

    }

    /**
     * 組裝購物車資料參數然後把租借品資訊存入購物車
     * @param setToCart : 含有成員變數 rentalNo、memNo、List<Integer> rentalNos 的 dto
     * @param rental : 用 rentalNo 找出的租借品
     */
    public void assembleAndSet(SetToCart setToCart, Rental rental) {

        Map<String, String> map = new HashMap<>();
        map.put("rentalNo", String.valueOf(rental.getRentalNo()));
        map.put("rentalCatNo", String.valueOf(rental.getRentalCategory().getRentalCatNo()));
        map.put("rentalName", rental.getRentalName());
        map.put("rentalPrice", String.valueOf(rental.getRentalPrice()));
        map.put("rentalDesPrice", String.valueOf(rental.getRentalCategory().getRentalDesPrice()));
        map.put("rentalSize", String.valueOf(rental.getRentalSize()));
        map.put("rentalColor", rental.getRentalColor());
        map.put("rentalInfo", rental.getRentalInfo());
        map.put("rentalStat", String.valueOf(rental.getRentalStat()));

        cartService.setToCart(setToCart.getMemNo(), map);

    }

    // 取出購物車商品資訊
    @GetMapping("/getFromCart")
    public ResponseEntity<?> getFromCart(HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        Map<String, Map<String, String>> map = cartService.getFromCart( String.valueOf(member.getMemNo()) );
        return ResponseEntity.status(HttpStatus.OK).body(map);

    }

    // 從購物車刪除商品
    @PostMapping("/deleteFromCart")
    public ResponseEntity<?> deleteFromCart(@RequestBody DeleteCantRent deleteCantRent,
                                            HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        cartService.deleteFromCart(member.getMemNo(), deleteCantRent.getRentalNos());
        return ResponseEntity.status(HttpStatus.OK).body("ok");

    }

    // 從購物車刪除商品
//    @PostMapping("/deleteFromCart")
//    public ResponseEntity<?> deleteFromCart(@RequestBody DeleteCantRent deleteCantRent,
//                                            HttpSession session) {
//
//        Member member = (Member) session.getAttribute("loginsuccess");
//
//        cartService.deleteFromCart(member.getMemNo(), deleteCantRent.getRentalNos());
//
//        session.setAttribute("loginsuccess", member);
//
//        return ResponseEntity.status(HttpStatus.OK).body("ok");
//
//    }


    /*----------------------------有關購物車的方法----------------------------------*/


    /*----------------------------有關物流的方法----------------------------------*/

    // 出貨
    @PostMapping("/createShippingOrder")
    public ResponseEntity<?> createShippingOrder(@RequestParam Integer rentalOrdNo) {

        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        RentalOrder order = service.getByAttributes(map).get(0);
        order.setrentalOrdStat((byte) 20);
        String formHTML = shippingService.shipping(rentalOrdNo);
        return ResponseEntity.status(HttpStatus.OK).body(formHTML);

    }

    // 查詢物流最新進度
    @GetMapping("/queryNewStat")
    public ResponseEntity<?> queryNewStat(@RequestParam Integer rentalOrdNo, HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        // 目前只有回傳物流狀態碼，可依照需求增加鑰取的值
        String logisticsStatus = logisticsStateService.postQueryLogisticsTradeInfo(member.getMemNo(), rentalOrdNo);
        return ResponseEntity.status(HttpStatus.OK).body(logisticsStatus);

    }

    /*----------------------------line pay----------------------------------*/



    @PostMapping("/linePay")
    public RedirectView linePay(@RequestParam("jsonData") String jsonData,
                                    HttpSession session, HttpServletRequest sReq) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return new RedirectView(toLogin);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        RentalOrderRequest order = new RentalOrderRequest();
        try {
            order = objectMapper.readValue(jsonData, RentalOrderRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println("發生轉換錯誤了喔喔喔");
        }
        /*-------------------------創建訂單時，設定初始參數-------------------------*/
        setInitArgForCreateOrder(order);
        order.setMemNo(member.getMemNo());
        /*----------------------------執行創建訂單流程----------------------------*/
        String paymentUrl = service.createOrder(order, sReq);
        return new RedirectView(paymentUrl);

    }




    /*----------------------------line pay----------------------------------*/

}
