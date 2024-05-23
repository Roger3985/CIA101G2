package com.howard.rentalorder.controller;

import com.howard.rentalorder.dto.DeleteCantRent;
import com.howard.rentalorder.dto.RentalOrderRequest;
import com.howard.rentalorder.dto.SetToCart;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.service.impl.LogisticsStateService;
import com.howard.rentalorder.service.impl.RentalCartServiceImpl;
import com.howard.rentalorder.service.impl.RentalOrderServiceImpl;
import com.howard.rentalorder.service.impl.RentalOrderShippingService;
import com.howard.rentalorderdetails.service.impl.RentalOrderDetailsServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

import static com.howard.util.HmacSignature.generateSignature;

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
    public String thankForBuying(@RequestParam(required = false) Integer rentalOrdNo, ModelMap model) {
        model.addAttribute("rentalOrdNo", rentalOrdNo);
        return "/frontend/rental/thankForBuying";
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
                                         HttpSession session) {

        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        /*-------------------------創建訂單時，設定初始參數-------------------------*/
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
        /*-------------------------執行創建訂單流程-------------------------*/
        /**
         * 創建訂單
         * @return 若有線上付款，回傳包含請求綠界付款畫面之字串，否則回傳感謝購買頁面路徑
         */
        String form = service.createOrder(order);
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

    // 給 會員所有訂單頁面 用的 getOnAny
    @GetMapping("/getOnAnyForOrdersPage")
    public String getOnAnyForOrdersPage(@RequestParam(required = false) Byte rentalOrdStat, ModelMap model) {

        Map<String, Object> map = new HashMap<>();

        if (rentalOrdStat != null) {
            map.put("rentalOrdStat", rentalOrdStat);
        }
        List<RentalOrder> orderList = service.getByAttributes(map);
        orderList.sort(Comparator.comparing(RentalOrder::getrentalOrdNo).reversed());
        model.addAttribute("orderList", orderList);
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

    private static final String CHANNEL_ID = "2005342190";
    private static final String CHANNEL_SECRET = "44c865afc4d0e1d4575ea90a87616108";
    private static final String REQUEST_URL = "https://sandbox-api-pay.line.me/v3/payments/request";

    @PostMapping("/testLinePay")
    public void testLinePay() {

        String orderId = "11111";
        int amount = 100; // 支付金額
        String currency = "TWD";
        String productName = "好看衣服";
        String confirmUrl = "http://localhost:8080/backend/rentalorder/thankForBuying";
        String cancelUrl = "http://localhost:8080/backend/rentalorder/rentalCart";

        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("orderId", orderId);
        requestBody.put("packages", new JSONObject().put("id", "package_id").put("amount", amount).put("name", productName));
        requestBody.put("redirectUrls", new JSONObject().put("confirmUrl", confirmUrl).put("cancelUrl", cancelUrl));

        String nonce = UUID.randomUUID().toString();
        String data = CHANNEL_SECRET + REQUEST_URL + requestBody.toString() + nonce;
        String signature = null;
        try {
            signature = generateSignature(CHANNEL_SECRET, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            signature = generateSignature(CHANNEL_SECRET, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpPost post = new HttpPost(REQUEST_URL);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("X-LINE-ChannelId", CHANNEL_ID);
        post.setHeader("X-LINE-Authorization-Nonce", nonce);
        post.setHeader("X-LINE-Authorization", signature);

        try {
            post.setEntity(new StringEntity(requestBody.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println("Response: " + responseString);

            JSONObject responseJson = new JSONObject(responseString);
            // 處理 responseJson 以取得交易ID等信息
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /*----------------------------line pay----------------------------------*/

}
