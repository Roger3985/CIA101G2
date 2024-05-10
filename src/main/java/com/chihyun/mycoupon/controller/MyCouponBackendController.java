package com.chihyun.mycoupon.controller;

import com.chihyun.mycoupon.model.MyCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/backend/mycoupon")
public class MyCouponBackendController {

    @Autowired
    MyCouponService myCouponSvc;

    @GetMapping("/selectMyCoupon")
    public String select_page(Model model){
        return "backend/mycoupon/selectMyCoupon";
    }

    @PostMapping("/addMyCoupon")
    public String addMyCoupon(Model model){
        return "backend/mycoupon/addMyCoupon";
    }

//    @PostMapping("/insert")
//    public String insertMyCoupon(Model model, List<MyCoupon> myCoupons){
//
//
//        myCouponSvc.addMyCoupon(myCoupons);
//        List<MyCoupon> list = myCouponSvc.getAll();
//        model.addAttribute("myCoupon", list );
//        return "backend/mycoupon/selectMyCoupon";
//    }

}
