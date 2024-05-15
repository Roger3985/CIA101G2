package com.howard.rentalorder.controller;

import com.howard.rentalorder.dto.SetToCart;
import com.yu.rental.dao.RentalRepository;
import com.howard.rentalorder.dto.RentalOrderRequest;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.service.impl.RentalCartServiceImpl;
import com.howard.rentalorder.service.impl.RentalOrderServiceImpl;
import com.howard.rentalorderdetails.service.impl.RentalOrderDetailsServiceImpl;
import com.yu.rental.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rentalorder")
@CrossOrigin(origins = "http://localhost:8080")
public class RentalOrderController {

    /*--------------------------所有方法共用-------------------------------*/

    @Autowired
    private RentalOrderServiceImpl service;

    @Autowired
    private RentalCartServiceImpl cartService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalOrderDetailsServiceImpl detailsService; // 加入購物車用的

    @ModelAttribute("orderList")
    protected List<RentalOrder> getAllData() {
        return service.getAll();
    }

    @ModelAttribute("rentalList")
    protected  List<Rental> getAllRental() {
        return rentalRepository.findAll();
    }

    /*--------------------------所有方法共用-------------------------------*/

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    // 去 商城 首頁
    @GetMapping("/toRentalShop")
    public String toRentalShop() {
        return "/frontend/rental/rentalShop";
    }

    // 去 購物車 畫面
    @GetMapping("/rentalCart")
    public String toRentalCart() {
        return "/frontend/rental/rentalCart";
    }

    // 去 結帳 頁面
    @GetMapping("/toRentalPayment")
    public String toRentalPayment() {
        return "/frontend/rental/rentalPayment";
    }

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
    public ResponseEntity<?> createOrder(@RequestBody @Valid RentalOrderRequest order) {
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
        // 付款狀態 = 0(未付款)(此為初步實作，之後由創建訂單時付款與否決定狀態)
        order.setrentalPayStat((byte) 0);
        // 訂單狀態 = 40(訂單成立)
        order.setrentalOrdStat((byte) 40);
        // 歸還狀態 = 0(未歸還)
        order.setRtnStat((byte) 0);
        // 歸還註記(因為資料庫設定NotNull，所以先設定為"尚未歸還")
        order.setRtnRemark("尚未歸還");
        /*-------------------------執行創建訂單流程-------------------------*/
        // 創建訂單
        String form = service.createOrder(order);
        // 先把字串陣列轉成整數陣列(因為service層方法需要List<Integer>)
        List<Integer> rentalNoList = order.getBuyItems().stream()
                .map(Integer::parseInt)
                        .toList();
        // 把購物車清空
        cartService.deleteFromCart(order.getMemNo(), rentalNoList);
        // 回傳帶有付款畫面 html 的 form 字串
        return ResponseEntity.status(HttpStatus.CREATED).body(form);

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
    public String getOnAny(@RequestParam(required = false) Integer rentalOrdNo,
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

        Map<String, String> map = new HashMap<>();
        map.put("rentalNo", String.valueOf(setToCart.getRentalNo()));
        map.put("rentalCatNo", String.valueOf(rental.getRentalCategory().getRentalCatNo()));
        map.put("rentalName", rental.getRentalName());
        map.put("rentalPrice", String.valueOf(rental.getRentalPrice()));
        map.put("rentalDesPrice", String.valueOf(rental.getRentalCategory().getRentalDesPrice()));
        map.put("rentalSize", String.valueOf(rental.getRentalSize()));
        map.put("rentalColor", rental.getRentalColor());
        map.put("rentalInfo", rental.getRentalInfo());
        map.put("rentalStat", String.valueOf(rental.getRentalStat()));
        cartService.setToCart(setToCart.getMemNo(), map);
        System.out.println(rental.getRentalName());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental.getRentalName());

    }

    // 取出購物車商品資訊
    @GetMapping("/getFromCart")         // 因為 scan 要用 string 所以乾脆不轉型了
    public ResponseEntity<?> getFromCart(@RequestParam String memNo) {

        Map<String, Map<String, String>> map = cartService.getFromCart(memNo);
        return ResponseEntity.status(HttpStatus.OK).body(map);

    }

    // 從購物車刪除商品
    @DeleteMapping("/deleteFromCart")
    public ResponseEntity<?> deleteFromCart(@RequestParam Integer memNo,
                                            @RequestParam Integer rentalNo) {

        List<Integer> rentalNos = new ArrayList<>();
        rentalNos.add(rentalNo);
        cartService.deleteFromCart(memNo, rentalNos);
        return ResponseEntity.status(HttpStatus.OK).body("ok");

    }


    /*----------------------------有關購物車的方法----------------------------------*/


    /*----------------------------練習串接綠界api的方法----------------------------------*/

//    @PostMapping("/ecpayCheckout")
//    @ResponseBody
//    public String ecpayCheckout() {
//
//        String aioCheckOutALLForm = service.ecpayCheckout();
//        return aioCheckOutALLForm;
//
//    }

}
