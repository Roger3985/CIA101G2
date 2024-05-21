package com.chihyun.mycoupon.controller;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/backend/mycoupon")
public class MyCouponBackendController {

    @Autowired
    MyCouponService myCouponSvc;

    @GetMapping("/selectMyCoupon")
    public String select_page(Model model) {
        return "backend/mycoupon/selectMyCoupon";
    }

    @PostMapping("/addMyCoupon")
    public String addMyCoupon(Model model) {
        return "backend/mycoupon/addMyCoupon";
    }

    @GetMapping("/listAllMyCoupon")
    public String listAllMyCoupon(Model model) {
        List<MyCoupon> MyCouponList = myCouponSvc.getAll();
        model.addAttribute("MyCouponList", MyCouponList);
        return "backend/mycoupon/listAllMyCoupon";
    }
}
