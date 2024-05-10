package com.howard.rentalorderdetails.controller;

import com.changhoward.cia10108springboot.Entity.RentalOrderDetails;
import com.changhoward.cia10108springboot.howard.rentalorderdetails.dto.RentalOrderDetailsRequest;
import com.changhoward.cia10108springboot.howard.rentalorderdetails.service.impl.RentalOrderDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rentalorderdetails")
public class RentalOrderDetailsController {

    /*--------------------------所有方法共用-------------------------------*/
    @Autowired
    private RentalOrderDetailsServiceImpl service;

    @ModelAttribute("detailList")
    protected List<RentalOrderDetails> getAllData() {
        return service.getAll();
    }
    /*--------------------------所有方法共用-------------------------------*/

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/
    // 去首頁
    @GetMapping("/selectRentalOrderDetails")
    public String toSelect() {
        return "/backend/rentalorderdetails/selectRentalOrderDetails";
    }

    // 去所有頁面
    @GetMapping("/listAllRentalOrderDetails")
    public String getAllProducts() {
        return "/backend/rentalorderdetails/listAllRentalOrderDetails";
    }

    // 從所有去修改頁面
    @GetMapping("/listAllToUpdateRentalOrderDetails")
    public String listAllToUpdate(@RequestParam Integer rentalOrdNo,
                                  @RequestParam Integer rentalNo,
                                  @RequestParam BigDecimal rentalPrice,
                                  @RequestParam BigDecimal rentalDesPrice,
                                  ModelMap model) {

        model.addAttribute("rentalOrdNo", rentalOrdNo);
        model.addAttribute("rentalNo", rentalNo);
        model.addAttribute("rentalPrice", rentalPrice);
        model.addAttribute("rentalDesPrice", rentalDesPrice);
        return "/backend/rentalorderdetails/updateRentalOrderDetails";

    }

    // 去修改頁面
    @GetMapping("/updateRentalOrderDetails")
    public String toUpdate() {
        return "/backend/rentalorderdetails/updateRentalOrderDetails";
    }

    // 去新增頁面
    @GetMapping("/addRentalOrderDetails")
    public String toAdd() {
        return "/backend/rentalorderdetails/addRentalOrderDetails";
    }

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    /*---------------------------處理CRUD請求的方法---------------------------------*/
    @PostMapping("/insert") // 新增
    public ResponseEntity<?> insert(@RequestBody @Valid RentalOrderDetailsRequest details_request,
                                 RedirectAttributes attributes,
                                 ModelMap model) {

        // 先把 PK 、單價、押金 取出
        Integer rentalOrdNo = details_request.getrentalOrdNo();
        Integer rentalNo = details_request.getrentalNo();
        BigDecimal rentalPrice = details_request.getrentalPrice();
        BigDecimal rentalDesPrice = details_request.getrentalDesPrice();
        // 資料是否已存在 ? 新增過了 : 繼續執行新增
        if (service.findById(rentalOrdNo, rentalNo) != null) {
            attributes.addFlashAttribute("already", "已經新增過了");
            return ResponseEntity.status(HttpStatus.OK).body("/backend/rentalorderdetails/addRentalOrderDetails");
//            return "redirect:/backend/rentalorderdetails/addRentalOrderDetails";
        }
        // 沒有，就裝進內部類別
        RentalOrderDetails.CompositeDetail compositeDetail = new RentalOrderDetails.CompositeDetail(rentalOrdNo, rentalNo);
        // 裝進類別
        RentalOrderDetails details_checked = new RentalOrderDetails(compositeDetail, rentalPrice, rentalDesPrice);
        service.insert(details_checked);
        // 新增完查查看有沒有新增成功
        RentalOrderDetails details_return = service.findById(rentalOrdNo, rentalNo);
        model.addAttribute("details", details_return);
        return ResponseEntity.status(HttpStatus.OK).body("/backend/rentalorderdetails/listAllRentalOrderDetails");
//        return "redirect:/backend/rentalorderdetails/listAllRentalOrderDetails";

    }

    @PostMapping("/update")
    public String update(@RequestParam @NotBlank Integer rentalOrdNo,
                         @RequestParam @NotBlank Integer rentalNo,
                         @RequestParam @NotBlank BigDecimal rentalPrice,
                         @RequestParam @NotBlank BigDecimal rentalDesPrice,
                         RedirectAttributes attributes,
                         ModelMap model) {

        // 先查查看該明細是否存在
        if (service.findById(rentalOrdNo, rentalNo) == null) {
//            model.addAttribute("noData", "查無資料，無法更新");
            attributes.addFlashAttribute("noData", "查無資料，無法更新");
            return "redirect:/backend/rentalorderdetails/updateRentalOrderDetails";
        }
        // 存在，開始修改
        RentalOrderDetails.CompositeDetail compositeDetail = new RentalOrderDetails.CompositeDetail(rentalOrdNo, rentalNo);
        RentalOrderDetails rentalOrderDetails = new RentalOrderDetails(compositeDetail, rentalPrice, rentalDesPrice);
        service.update(rentalOrderDetails);
        // 改完查詢出來回傳
        RentalOrderDetails details_return = service.findById(rentalOrdNo, rentalNo); // 待刪
        model.addAttribute("details", details_return); // 待刪
        return "redirect:/backend/rentalorderdetails/listAllRentalOrderDetails";

    }

    @GetMapping("/getOnAny")
    public String getOnAny(@RequestParam(required = false) Integer rentalOrdNo,
                           @RequestParam(required = false) Integer rentalNo,
                           @RequestParam(required = false) BigDecimal rentalPrice,
                           @RequestParam(required = false) BigDecimal rentalDesPrice,
                           ModelMap model) {

        Map<String, Object> map = new HashMap<>();

        if (rentalOrdNo != null) {
            map.put("rentalOrdNo", rentalOrdNo);
        }
        if (rentalNo != null) {
            map.put("rentalNo", rentalNo);
        }
        if (rentalPrice != null) {
            map.put("rentalPrice", rentalPrice);
        }
        if (rentalDesPrice != null) {
            map.put("rentalDesPrice", rentalDesPrice);
        }

        List<RentalOrderDetails> rentalOrderDetailsList = service.getByAttributes(map);
        model.addAttribute("rentalOrderDetailsList", rentalOrderDetailsList);
        model.addAttribute("getOnAny", "true");
        return "/backend/rentalorderdetails/selectRentalOrderDetails";

    }

    /*---------------------------處理CRUD請求的方法---------------------------------*/

}
