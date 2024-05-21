package com.chihyun.mycoupon.controller;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.roger.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/frontend/mycoupon")
public class MyCouponFrontendController {

    @Autowired
    MyCouponService myCouponSvc;

    //    會員中心-我的優惠券清單
    @GetMapping("/mycouponlist")
    public String showList(Model model, HttpSession session) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        if (myData == null) {
            return "redirect:/frontend/member/loginMember";
        } else {
            List<MyCoupon> list = myCouponSvc.getAllMyCouponMem(myData.getMemNo());
            System.out.println(list);
            List<MyCoupon> showMyCoupon = new ArrayList<>();
            for (MyCoupon mycoupons : list) {
                if (mycoupons.getCoupUsedStat() == 0) {
                    showMyCoupon.add(mycoupons);
                }
            }
            int myCouponQTY = showMyCoupon.size();
            model.addAttribute("myCouponList", showMyCoupon);
            model.addAttribute("myCouponQTY", myCouponQTY);
            return "/frontend/mycoupon/myCoupon";
        }
    }

//    @PostMapping("/useMyCoupon")
//    public String userMycoupon(){
//        if(){
//
//        }else {
//
//        }
//    }

    //    未登入session測試用
//    @GetMapping("/selectTest")
//    public String select_page(Model model) {
//        return "frontend/mycoupon/selectTest";
//    }
//
//    //    會員中心-查看我的優惠券清單
//    @PostMapping("/getOne_For_Display")
//    public String getOne_For_Display(Model model, @RequestParam String memNo) {
//        List<MyCoupon> list = myCouponSvc.getAllMyCouponMem(Integer.valueOf(memNo));
//        model.addAttribute("myCouponList", list);
//        return "/frontend/mycoupon/listAllMyCoupon";
//    }

    //    購物車結帳按鈕-改變會員優惠券使用狀態
    @PostMapping("/updateMyCoupon")
    public void alterUsedStatus(MyCoupon myCoupon, @RequestParam String coupNo, @RequestParam String memNo, HttpSession session) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        MyCoupon.CompositeCouponMember compositeKey = new MyCoupon.CompositeCouponMember();
        compositeKey.setCoupNo(Integer.valueOf(coupNo));
        compositeKey.setMemNo(Integer.valueOf(memNo));
        myCoupon.setCompositeCouponMember(compositeKey);
        Optional<MyCoupon> optional = myCouponSvc.getOneMyCoupon(Integer.valueOf(coupNo), Integer.valueOf(memNo));
        if (optional.isPresent()) {
            myCoupon.setCoupon(optional.get().getCoupon());
            myCoupon.setCoupUsedStat(Byte.valueOf("1"));
            myCoupon.setCoupInfo(optional.get().getCoupInfo());
            myCoupon.setCoupExpDate(myCoupon.getCoupon().getCoupExpDate());
            myCouponSvc.updateMyCoupon(myCoupon);
        }
    }

//    @PostMapping("/updateMyCoupon")
//    public String alterUsedStatus(MyCoupon myCoupon, @RequestParam String coupNo, @RequestParam String memNo, Model model) {
//        MyCoupon.CompositeCouponMember compositeKey = new MyCoupon.CompositeCouponMember();
//        compositeKey.setCoupNo(Integer.valueOf(coupNo));
//        compositeKey.setMemNo(Integer.valueOf(memNo));
//        myCoupon.setCompositeCouponMember(compositeKey);
//        Optional<MyCoupon> optional = myCouponSvc.getOneMyCoupon(Integer.valueOf(coupNo), Integer.valueOf(memNo));
//        if(optional.isPresent()){
//            myCoupon.setCoupon(optional.get().getCoupon());
//            myCoupon.setCoupUsedStat(Byte.valueOf("1"));
//            myCoupon.setCoupInfo(optional.get().getCoupInfo());
//            myCoupon.setCoupExpDate(myCoupon.getCoupon().getCoupExpDate());
//            myCouponSvc.updateMyCoupon(myCoupon);
//        }
//        return "frontend/mycoupon/listAllMyCoupon";
//    }


    //    購物車結帳-選擇優惠券
    @PostMapping("/updateState")
    public void updateStat(MyCoupon myCoupon, Model model) {
        myCouponSvc.updateMyCoupon(myCoupon);
    }
}




