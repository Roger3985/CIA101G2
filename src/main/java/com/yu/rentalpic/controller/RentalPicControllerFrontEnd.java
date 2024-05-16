//package com.yu.rentalpic.controller;
//
//import com.yu.rental.service.RentalServiceImpl;
//import com.yu.rentalpic.entity.RentalPic;
//import com.yu.rentalpic.service.RentalPicServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//import java.io.IOException;
//
//@Controller
//@RequestMapping("/frontend") //對應資料夾路徑
//public class RentalPicControllerFrontEnd {
//
//    /**前端網頁需求：
//     *
//     * 照片上傳
//     * 點入商品欄後，顯示個別商品、全部商品
//     * 排序方法：最新上架、價格低~高、價格高~低、最熱門
//     * 點選"加入我的最愛"，即可新增查看
//     *個別品項篩選 (針對類別、金額、Size)
//     *
//     */
//
//    @Autowired  // 自動裝配
//    private RentalPicServiceImpl rentalPicService;
//    @Autowired  // 自動裝配
//    private RentalServiceImpl rentalService;
//
//////////////////////////////////////////////////////////////////////////////////////////////////
//// 測試區 //
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////
////    //顯示所有照片
////    @GetMapping("/showAllPic")
////    public String showAllPic(@Valid RentalPic rentalPic , BindingResult result, ModelMap model,
////                             @RequestParam("upFiles") MultipartFile[] parts) throws IOException {
////
////        // 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
////        result = removeFieldError(rentalPic, result, "upFiles");
////
////        if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
////            model.addAttribute("errorMessage", "照片: 請上傳照片");
////        } else {
////            for (MultipartFile multipartFile : parts) {
////                byte[] buf = multipartFile.getBytes();
////                rentalPic.setUpFiles(buf);
////            }
////        }
////        if (result.hasErrors() || parts[0].isEmpty()) {
////            return "back-end/emp/addEmp";
////        }
////
////    }
//
//
//    //顯示單筆訂單的照片
//
//}
