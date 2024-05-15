package com.yu.rentalmyfavorite.controller;

import com.roger.member.entity.Member;
import com.roger.member.service.impl.MemberServiceImpl;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/backend/rentalmyfavorite") //對應資料夾路徑
public class RentalMyFavoriteBackEnd {


    @Autowired  // 自動裝配
    private RentalMyFavoriteServiceImpl rentalMyFAVService;
    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired  // 自動裝配
    private MemberServiceImpl memberService;


    //顯示後台首頁
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }


    //顯示全部租借品照片頁面 (後台)
    @GetMapping("/listAllRentalMyFAV")
    public String listAllRentalMyFAV() {
        return "/backend/rentalmyfavorite/listAllRentalMyFAV";
    }


    //顯示後台 select_page
    @GetMapping("/selectRentalMyFAV")
    public String selectPage(ModelMap model) {
        RentalMyFavorite rentalMyFavorite = new RentalMyFavorite();
//        rentalMyFavorite.setRentalNo(0); // 初始化rentalMyFavoriteNo
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/selectRentalMyFAV";
    }


    //顯示後台 addRentalMyFAV
    @GetMapping("/addRentalMyFAV")
    public String addRentalMyFAV(ModelMap model) {
        RentalMyFavorite rentalMyFavorite = new RentalMyFavorite();
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/addRentalMyFAV";
    }


    //顯示後台 updateRentalMyFavorite
    @GetMapping("/updateRentalMyFavorite")
    public String updateRentalMyFavorite(ModelMap model) {
        List<RentalMyFavorite> rentalMyFavoriteList =rentalMyFAVService.findAll();
        model.addAttribute("rentalMyFavoriteList", rentalMyFavoriteList);
        model.addAttribute("rentalMyFavorite", rentalMyFavoriteList.get(0));
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);
        return "/backend/rentalmyfavorite/updateRentalMyFAV";
    }

    //處理查詢
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam("rentalNo") String rentalNo, @RequestParam("memNo") String memNo, ModelMap model) {

        RentalMyFavorite rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
        List<RentalMyFavorite> rentalCatList = rentalMyFAVService.findAll();
        model.addAttribute("rentalCatList", rentalCatList);

        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rental", new Rental());
        model.addAttribute("rentalList", rentalList);

        List<Member> memberList = memberService.findAll();
        model.addAttribute("member", new Member());
        model.addAttribute("memberList", memberList);


        if (rentalMyFavorite == null) {
            model.addAttribute("errors", "errors");
            return "/backend/rentalmyfavorite/selectRentalMyFAV";
        }
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/listOneRentalMyFAV"; // 查詢完成後轉交
    }

    
    // 處理修改資料
    @PostMapping("updateRentalMyFAV")
    public String updateRentalMyFAV(@Valid RentalMyFavorite rentalMyFavorite, BindingResult result, ModelMap model) {

        if (rentalMyFavorite != null) {
            if (result.hasErrors()) { //若有錯誤
                //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
                List<FieldError> fieldErrors = result.getFieldErrors();
                for (int i = 0, len = fieldErrors.size(); i < len; i++) {
                    FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
                    model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入map。(Message是entity輸入的內容)
                    model.addAttribute("rentalMyFavorite", rentalMyFavorite);
                }
                return"/backend/rentalmyfavorite/updateRentalMyFAV";
            }
        }else{
            return "/backend/rentalmyfavorite/updateRentalMyFAV";
        }
        // 將資料添加到 ModelMap 中
        rentalMyFAVService.updateRentalFav(rentalMyFavorite);
        rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/listOneRentalMyFAV";
    }


    //處理單筆修改
    @PostMapping("getOneUpdate")
    public String getOneUpdate(@RequestParam("rentalNo") String rentalNo, @RequestParam("memNo") String memNo, ModelMap model) {
//        RentalMyFavorite rentalMyFavorite = rentalService.findByNo(Integer.valueOf(rentalNo));
//        model.addAttribute("rental", rental);
//        RentalMyFavorite rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/updateRentalMyFAV"; // 查詢完成後轉交
    }

    //處理新增
    @PostMapping("addRentalMyFAV")
    public String addRentalMyFAV(@Valid RentalMyFavorite rentalMyFavorite, BindingResult result, ModelMap model) {
//
//        if (rentalMyFavorite != null) {
//            if (result.hasErrors()) { //若有錯誤
//                //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
//                List<FieldError> fieldErrors = result.getFieldErrors();
//                for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                    FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                    model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                    model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//                }
//                return "/backend/rentalmyfavorite/addRentalMyFAV";
//            }
//        }else{
//            return "/backend/rentalmyfavorite/addRentalMyFAV";
//        }
//        // 將資料添加到 ModelMap 中
//        rentalMyFAVService.addRentalFav(rentalMyFavorite);
//        rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//        model.addAttribute("rentalMyFavorite", rentalMyFavorite);

        return "/backend/rentalmyfavorite/listAllRentalMyFAV";
    }
    
}


