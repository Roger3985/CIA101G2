package com.Cia101G2.howard.rentalmytrack.controller;

import com.Cia101G2.Entity.RentalMyTrack;
import com.Cia101G2.howard.rentalmytrack.service.impl.RentalMyTrackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Controller
@RequestMapping("/backend/rentalmytrack")
public class BackendRentalMyTrackController {

    /*--------------------------所有方法共用-------------------------------*/

    @Autowired
    private RentalMyTrackServiceImpl rentalMyTrackService;

    // 讓所有方法的 ModelMap 都帶上 key=trackList，value=rentalMyTrackService.getAll() 的 key-value pair
    @ModelAttribute("trackList")
    protected List<RentalMyTrack> getAllData() {
        return rentalMyTrackService.getAll();
    }

    /*--------------------------所有方法共用-------------------------------*/

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/
    // 去 首頁 頁面 (selectRentalMyTrack)
    @GetMapping("/selectRentalMyTrack")
    public String toSelect() {
        return "/backend/rentalmytrack/selectRentalMyTrack";
    }

    // 去 所有追蹤品 頁面 (listAllRentalMyTracks)
    @GetMapping("/listAllRentalMyTracks")
    public String getAllProducts() {
        return "/backend/rentalmytrack/listAllRentalMyTracks";
    }

    // 從 所有追蹤品頁面 去 修改追蹤品資料 頁面 (updateRentalMyTrack)
    @GetMapping("/listAllToUpdateRentalMyTrack")
    public String listAllToUpdate(@RequestParam Integer rentalNo,
                                  @RequestParam Integer memNo,
                                  ModelMap model) {

        RentalMyTrack rentalMyTrack = rentalMyTrackService.findById(rentalNo, memNo);
        Timestamp rentalTrackTime = rentalMyTrack.getrentalTrackTime();

        model.addAttribute("rentalNo", rentalNo);
        model.addAttribute("memNo", memNo);
        model.addAttribute("rentalTrackTime", rentalTrackTime);
        return "/backend/rentalmytrack/updateRentalMyTrack";

    }

    // 去 修改追蹤品資料 頁面 (updateRentalMyTrack)
    @GetMapping("/updateRentalMyTrack")
    public String toUpdate() {
        return "/backend/rentalmytrack/updateRentalMyTrack";
    }

    // 去 新增追蹤品 頁面 (addRentalMyTrack)
    @GetMapping("/addRentalMyTrack")
    public String toAdd() {
        return "/backend/rentalmytrack/addRentalMyTrack";
    }

    /*--------------------------處理跳轉頁面請求的方法-------------------------------*/

    /*---------------------------處理CRUD請求的方法---------------------------------*/
    @PostMapping("/insert") // 新增
    public String insert(@RequestParam @NotNull Integer rentalNo,
                         @RequestParam @NotNull Integer memNo,
                         RedirectAttributes attributes) {

        // 資料是否已存在 ? 新增過了 : 繼續執行新增
        if (rentalMyTrackService.findById(rentalNo, memNo) != null) {
            attributes.addFlashAttribute("rentalNo", rentalNo);
            attributes.addFlashAttribute("memNo", memNo);
            attributes.addFlashAttribute("already", "該追蹤已經存在!");
            return "redirect:/backend/rentalmytrack/addRentalMyTrack";
        }
        // 沒有，就裝進內部類別
        RentalMyTrack.CompositeTrack compositeTrack = new RentalMyTrack.CompositeTrack(rentalNo, memNo);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // 裝進類別
        RentalMyTrack rentalMyTrack = new RentalMyTrack(compositeTrack, timestamp);
        rentalMyTrackService.insert(rentalMyTrack);
        // 新增完查查看有沒有新增成功
        if (rentalMyTrackService.findById(rentalNo, memNo) == null) {
            attributes.addFlashAttribute("unknownError", "不明錯誤導致無法新增");
            return "redirect:/backend/rentalmytrack/addRentalMyTrack";
        }
        // 新增成功，重導向
        return "redirect:/backend/rentalmytrack/listAllRentalMyTracks";

    }

    @PostMapping("/update") // 修改
    public String update(@RequestParam("rentalNo") Integer rentalNo,
                         @RequestParam("memNo") Integer memNo,
                         @RequestParam("expRentalDate") @NotNull Date expRentalDate, // 把@拿掉就可以
                         RedirectAttributes attributes) {

        // 租借品、會員編號有錯誤時
//        if (result.hasErrors()) {
//            result.getAllErrors().forEach( error -> attributes.addFlashAttribute( "error_ "+ ( (FieldError) error ).getField(), error.getDefaultMessage() ) );
//            attributes.addFlashAttribute("rentalNo", rentalNo);
//            attributes.addFlashAttribute("memNo", memNo);
//            return "redirect:/backend/rentalmytrack/updateRentalMyTrack";
//        }

        // 先查查看該商品是否存在
        if (rentalMyTrackService.findById(rentalNo, memNo) == null) {
            attributes.addFlashAttribute("rentalNo", rentalNo);
            attributes.addFlashAttribute("memNo", memNo);
            attributes.addFlashAttribute("expRentalDate", expRentalDate);
            attributes.addFlashAttribute("noData", "查無資料");
            return "redirect:/backend/rentalmytrack/updateRentalMyTrack";
        }
        // 檢查期望租借日期是否早於或等於今天
        Date toDay = new Date(System.currentTimeMillis());
        if (expRentalDate.compareTo(toDay) <= 0) {
            attributes.addFlashAttribute("rentalNo", rentalNo);
            attributes.addFlashAttribute("memNo", memNo);
            attributes.addFlashAttribute("expRentalDate", expRentalDate);
            attributes.addFlashAttribute("wrongDate", "日期不能早於或等於今天!");
            return "redirect:/backend/rentalmytrack/updateRentalMyTrack";
        }
        RentalMyTrack.CompositeTrack compositeTrack = new RentalMyTrack.CompositeTrack(rentalNo, memNo);
        RentalMyTrack rentalMyTrack = new RentalMyTrack(compositeTrack, expRentalDate);
        // 有查到且日期正確，開始修改
        rentalMyTrackService.update(rentalMyTrack);
        // 改完，重導向
        attributes.addFlashAttribute("success", "修改成功!");
        return "redirect:/backend/rentalmytrack/listAllRentalMyTracks";

    }

    @GetMapping("/getOnAny") // 查詢
    public String getOnAny(@RequestParam(required = false) Integer rentalNo,
                           @RequestParam(required = false) Integer memNo,
                           @RequestParam(required = false) Timestamp rentalTrackTime,
                           @RequestParam(required = false) Date expRentalDate,
                           ModelMap model) {

        Map<String, Object> map = new HashMap<>();

        if (rentalNo != null) {
            map.put("rentalNo", rentalNo);
        }
        if (memNo != null) {
            map.put("memNo", memNo);
        }
        if (rentalTrackTime != null) {
            map.put("rentalTrackTime", rentalTrackTime);
        }
        if (expRentalDate != null) {
            map.put("expRentalDate", expRentalDate);
        }

        List<RentalMyTrack> rentalMyTrackList = rentalMyTrackService.getByAttributes(map);
        model.addAttribute("rentalMyTrackList", rentalMyTrackList);
        model.addAttribute("getOnAny", "true");
        return "/backend/rentalmytrack/selectRentalMyTrack";

    }
    /*---------------------------處理CRUD請求的方法---------------------------------*/

    /*--------------------------將重複動作提煉成方法---------------------------------*/
//    private RedirectAttributes addAttibutes(Integer rentalNo, Integer memNo,
//                                            Date expRentalDate, RedirectAttributes attributes) {
//        if (rentalNo != null) {
//            attributes.addFlashAttribute("rentalNo", rentalNo)
//        }
//
//    }
    /*--------------------------將重複動作提煉成方法---------------------------------*/


}
