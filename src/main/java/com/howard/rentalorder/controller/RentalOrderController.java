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
import com.howard.util.HmacSignature;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.howard.util.HmacSignature.generateSignature;

@Controller
@RequestMapping("/backend/rentalorder")
@CrossOrigin(origins = "http://localhost:8080")
public class RentalOrderController {

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

    /*--------------------------所有方法共用-------------------------------*/

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    // 去 測試 頁面
    @GetMapping("/toTestEcpay")
    public String toTestEcpay() {
        return "/backend/rentalorder/testEcpay";
    }

    // 去 加入購物車 頁面
    @GetMapping("/addToCart")
    public String toAddToCart() {
        return "/backend/rentalorder/addToCart";
    }

    // 去 購物車商品 頁面
    @GetMapping("/toRentalCart")
    public String toCart() {
        return "/backend/rentalorder/RentalCart";
    }

    // 去 結帳 頁面
    @GetMapping("/toCheckOut")
    public String toCheckOut() {
        return "/backend/rentalorder/checkOut";
    }

    @GetMapping("/createOrderSuccess")
    public String toSuccess() {
        return "/backend/rentalorder/createOrderSuccess";
    }

    // 去 首頁
    @GetMapping("/selectRentalOrder")
    public String toSelect() {
        return "/backend/rentalorder/selectRentalOrder";
    }

    // 去 所有訂單頁面
    @GetMapping("/listAllRentalOrder")
    public String toAllOrders() {
        return "/backend/rentalorder/listAllRentalOrder";
    }

    // 去 修改訂單 頁面
    @GetMapping("/updateRentalOrder")
    public String toUpdate() {
        return "/backend/rentalorder/updateRentalOrder";
    }

    // 從 所有訂單 去 修改訂單 頁面
    @GetMapping("/listAllToUpdateRentalOrder")
    public String listAllToUpdate(@RequestParam Integer rentalOrdNo, ModelMap model) {
        // 把資料一起帶到修改頁面
        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        List<RentalOrder> rentalOrderList = service.getByAttributes(map);
        if (rentalOrderList == null || rentalOrderList.isEmpty()) {
            return "/backend/rentalorder/listAllRentalOrder";
        }
        RentalOrder rentalOrder = rentalOrderList.get(0);
        model.addAttribute("rentalOrdNo", rentalOrder.getrentalOrdNo());
        model.addAttribute("memNo", rentalOrder.getMember().getMemNo());
        model.addAttribute("rentalByrName", rentalOrder.getrentalByrName());
        model.addAttribute("rentalByrPhone", rentalOrder.getrentalByrPhone());
        model.addAttribute("rentalByrEmail", rentalOrder.getrentalByrEmail());
        model.addAttribute("rentalRcvName", rentalOrder.getrentalRcvName());
        model.addAttribute("rentalRcvPhone", rentalOrder.getrentalRcvPhone());
        model.addAttribute("rentalTakeMethod", rentalOrder.getrentalTakeMethod());
        model.addAttribute("rentalAddr", rentalOrder.getrentalAddr());
        model.addAttribute("rentalPayMethod", rentalOrder.getrentalPayMethod());
        model.addAttribute("rentalAllPrice", rentalOrder.getrentalAllPrice());
        model.addAttribute("rentalAllDepPrice", rentalOrder.getrentalAllDepPrice());
        model.addAttribute("rentalOrdTime", rentalOrder.getrentalOrdTime());
        model.addAttribute("rentalDate", rentalOrder.getrentalDate());
        model.addAttribute("rentalBackDate", rentalOrder.getrentalBackDate());
        model.addAttribute("rentalRealBackDate", rentalOrder.getrentalRealBackDate());
        model.addAttribute("rentalPayStat", rentalOrder.getrentalPayStat());
        model.addAttribute("rentalOrdStat", rentalOrder.getrentalOrdStat());
        model.addAttribute("rtnStat", rentalOrder.getRtnStat());
        model.addAttribute("rtnRemark", rentalOrder.getRtnRemark());
        model.addAttribute("rtnCompensation", rentalOrder.getRtnCompensation());
        return "/backend/rentalorder/updateRentalOrder";

    }

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    /*---------------------------處理CRUD請求的方法---------------------------------*/

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody @Valid RentalOrderRequest order, HttpServletRequest sReq) {
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
        String form = service.createOrder(order, sReq);
        // 先把字串陣列轉成整數陣列(因為service層方法需要List<Integer>)
        List<Integer> rentalNoList = order.getBuyItems().stream()
                .map(Integer::parseInt)
                        .toList();
        // 把購物車清空
        cartService.deleteFromCart(order.getMemNo(), rentalNoList);
        // 更新 Redis 裡其他購物車資料，有加入明細裡的商品的，租借品狀態改為 1 (已預約)
        cartService.updateCart(rentalNoList, "rentalStat", "1");
        // 回傳帶有付款畫面 html 的 form 字串
        return ResponseEntity.status(HttpStatus.CREATED).body(form);

    }

    // 接收綠界金流交易回傳參數
    @PostMapping("/receiveTradeInfos")
    public ResponseEntity<?> receiveTradeInfos(@RequestBody String infos) {

        Map<String, String> infosMap = logisticsStateService.parseLogisticsInfo(infos);
        System.out.println("接收到交易成功訊息了喔喔喔喔喔 ===" + infosMap.get("MerchantTradeNo"));
        service.setTradeSuccessInfos(infosMap);
        return ResponseEntity.status(HttpStatus.OK).body("1|OK");

    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam Integer rentalOrdNo,
                              @RequestParam(required = false) Byte rentalPayStat,
                              @RequestParam(required = false) Byte rentalOrdStat,
                              @RequestParam(required = false) Byte rtnStat,
                              @RequestParam(required = false) String rtnRemark,
                              @RequestParam(required = false) BigDecimal rtnCompensation, ModelMap model) {
        // 付款時 => 更改付款狀態 -> 自動
        // 選宅配，商品寄到目的地時 => 變更訂單狀態 -> 自動
        // 歸還時 => 加上歸還註記、加上賠償金額、變更歸還狀態 -> 員工手動
        // 取消訂單時 => 更改訂單狀態 -> 員工手動
        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        if (rentalPayStat != null) {
            map.put("rentalPayStat", rentalPayStat);
        }
        if (rentalOrdStat != null) {
            map.put("rentalOrdStat", rentalOrdStat);
            if (rentalOrdStat == (byte) 50) {

            }
        }
        if (rtnStat != null) {
            map.put("rtnStat", rtnStat);
        }
        if (rtnRemark != null) {
            map.put("rtnRemark", rtnRemark);
        }
        if (rtnCompensation != null) {
            map.put("rtnCompensation", rtnCompensation);
        }
        service.update(map);

        return "redirect:/backend/rentalorder/listAllRentalOrder";

    }

    @GetMapping("/getOnAny")
    public String getOnAny(
                           @RequestParam(required = false) Integer rentalOrdNo,
                           @RequestParam(required = false) Integer memNo,
                           @RequestParam(required = false) String rentalByrName,
                           @RequestParam(required = false) String rentalByrPhone,
                           @RequestParam(required = false) String rentalByrEmail,
                           @RequestParam(required = false) String rentalRcvName,
                           @RequestParam(required = false) String rentalRcvPhone,
                           @RequestParam(required = false) Byte rentalTakeMethod,
                           @RequestParam(required = false) String rentalAddr,
                           @RequestParam(required = false) Byte rentalPayMethod,
                           @RequestParam(required = false) BigDecimal rentalAllPrice,
                           @RequestParam(required = false) BigDecimal rentalAllDepPrice,
                           @RequestParam(required = false) Timestamp rentalOrdTime,
                           @RequestParam(required = false) Timestamp rentalDate,
                           @RequestParam(required = false) Timestamp rentalBackDate,
                           @RequestParam(required = false) Timestamp rentalRealBackDate,
                           @RequestParam(required = false) Byte rentalPayStat,
                           @RequestParam(required = false) Byte rentalOrdStat,
                           @RequestParam(required = false) Byte rtnStat,
                           @RequestParam(required = false) String rtnRemark,
                           @RequestParam(required = false) BigDecimal rtnCompensation,
                           ModelMap model) {

        Map<String, Object> map = new HashMap<>();

        if (rentalOrdNo != null) {
            map.put("rentalOrdNo", rentalOrdNo);
        }
        if (memNo != null) {
            map.put("memNo", memNo);
        }
        if (rentalByrName != null) {
            map.put("rentalByrName", rentalByrName);
        }
        if (rentalByrPhone != null) {
            map.put("rentalByrPhone", rentalByrPhone);
        }
        if (rentalByrEmail != null) {
            map.put("rentalByrEmail", rentalByrEmail);
        }
        if (rentalRcvName != null) {
            map.put("rentalRcvName", rentalRcvName);
        }
        if (rentalRcvPhone != null) {
            map.put("rentalRcvPhone", rentalRcvPhone);
        }
        if (rentalTakeMethod != null) {
            map.put("rentalTakeMethod", rentalTakeMethod);
        }
        if (rentalAddr != null) {
            map.put("rentalAddr", rentalAddr);
        }
        if (rentalPayMethod != null) {
            map.put("rentalPayMethod", rentalPayMethod);
        }
        if (rentalAllPrice != null) {
            map.put("rentalAllPrice", rentalAllPrice);
        }
        if (rentalAllDepPrice != null) {
            map.put("rentalAllDepPrice", rentalAllDepPrice);
        }
        if (rentalOrdTime != null) {
            map.put("rentalOrdTime", rentalOrdTime);
        }
        if (rentalDate != null) {
            map.put("rentalDate", rentalDate);
        }
        if (rentalBackDate != null) {
            map.put("rentalBackDate", rentalBackDate);
        }
        if (rentalRealBackDate != null) {
            map.put("rentalRealBackDate", rentalRealBackDate);
        }
        if (rentalPayStat != null) {
            map.put("rentalPayStat", rentalPayStat);
        }
        if (rentalOrdStat != null) {
            map.put("rentalOrdStat", rentalOrdStat);
        }
        if (rtnStat != null) {
            map.put("rtnStat", rtnStat);
        }
        if (rtnRemark != null) {
            map.put("rtnRemark", rtnRemark);
        }
        if (rtnCompensation != null) {
            map.put("rtnCompensation", rtnCompensation);
        }

        List<RentalOrder> rentalOrderList = service.getByAttributes(map);
        model.addAttribute("rentalOrderList", rentalOrderList);
        model.addAttribute("getOnAny", "true");
        return "/backend/rentalorder/selectRentalOrder";

    }

    /*---------------------------處理CRUD請求的方法---------------------------------*/

    /*----------------------------有關購物車的方法----------------------------------*/

    // 加入購物車
    @PostMapping("/setToCart")
    public ResponseEntity<?> setToCart(@RequestBody SetToCart setToCart) {

        Rental rental = rentalRepository.findByRentalNo(setToCart.getRentalNo());
        assembleAndSet(setToCart, rental);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental.getRentalName());

    }

    // 再買一次
    @PostMapping("/buyAgain")
    public ResponseEntity<?> buyAgain(@RequestBody SetToCart setToCart) {

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
//        System.out.println("有進來這方法喔，取到的rentalNo = " + rental.getRentalNo());
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
    @GetMapping("/getFromCart")         // 因為 scan 要用 string 所以乾脆不轉型了
    public ResponseEntity<?> getFromCart(@RequestParam String memNo) {

        Map<String, Map<String, String>> map = cartService.getFromCart(memNo);
        return ResponseEntity.status(HttpStatus.OK).body(map);

    }

    // 從購物車刪除商品
    @PostMapping("/deleteFromCart")
    public ResponseEntity<?> deleteFromCart(@RequestBody DeleteCantRent deleteCantRent) {

        cartService.deleteFromCart(deleteCantRent.getMemNo(), deleteCantRent.getRentalNos());
        return ResponseEntity.status(HttpStatus.OK).body("ok");

    }


    /*----------------------------有關購物車的方法----------------------------------*/


    /*----------------------------有關物流的方法----------------------------------*/

    // 出貨
    @PostMapping("/createShippingOrder")
    public ResponseEntity<?> createShippingOrder(@RequestParam Integer rentalOrdNo) {
        System.out.println("有進來controller方法" + rentalOrdNo);

        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        RentalOrder order = service.getByAttributes(map).get(0);
        order.setrentalOrdStat((byte) 20);
        String formHTML = shippingService.shipping(rentalOrdNo);
        return ResponseEntity.status(HttpStatus.OK).body(formHTML);

    }

    // 查詢物流最新進度
    @GetMapping("/queryNewStat")
    public ResponseEntity<?> queryNewStat(@RequestParam Integer memNo,
                                          @RequestParam Integer rentalOrdNo) {
        // 目前只有回傳物流狀態碼，其他資訊都沒取出來
        String logisticsStatus = logisticsStateService.postQueryLogisticsTradeInfo(memNo, rentalOrdNo);
        return ResponseEntity.status(HttpStatus.OK).body(logisticsStatus);

    }

    @PostMapping("/testToRentalCart")
    public String rentalCart() {
        return "/frontend/rental/rentalCart";
    }

    @GetMapping("/shipping")
    public String shippingStatus(HttpServletRequest req, ModelMap model) {

        req.getParameterMap().forEach((key, value) ->model.addAttribute(key, value[0]));
        return "/backend/rentalorder/shipping";

    }

    /*----------------------------有關刷退押金的方法----------------------------------*/

    // 刷退
    @PostMapping("/depRefund")
    public ResponseEntity<?> depRefund(@RequestBody Integer rentalOrdNo) {

        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        Map<String, String> refundInfos = service.refund(service.getByAttributes(map).get(0));
        return ResponseEntity.status(HttpStatus.OK).body(refundInfos);

    }

    /*----------------------------line pay----------------------------------*/

    // LinePay 刷退
    @PostMapping("/depRefundForLinePay")
    public ResponseEntity<?> depRefundForLinePay(@RequestBody Integer rentalOrdNo) {

        Map<String, Object> map = new HashMap<>();
        map.put("rentalOrdNo", rentalOrdNo);
        Map<String, String> refundInfos = service.refundForLinePay(service.getByAttributes(map).get(0));
        return ResponseEntity.status(HttpStatus.OK).body(refundInfos);

    }

    /*----------------------------line pay----------------------------------*/


}
