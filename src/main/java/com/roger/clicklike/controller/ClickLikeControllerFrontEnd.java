package com.roger.clicklike.controller;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.service.ClickLikeService;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/frontend/clicklike")
public class ClickLikeControllerFrontEnd {

    @Autowired
    private ClickLikeService clickLikeService;

    @Autowired
    private MyCouponService myCouponSvc;

    /**
     * 前往查看單個會員的全部點讚的專欄文章
     * @return
     */
    @GetMapping("/memberClikeLikeData")
    public String memberClikeLikeData(ModelMap modelMap,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        // 從 HTTP 會話中獲取當前已登入的會員跟通知資料
        Member myData = (Member) session.getAttribute("loginsuccess");

        // 如果會員未登錄，重定向到登錄頁面
        if (myData == null) {
            redirectAttributes.addAttribute("error", "登入失敗");
            return "redirect:/frontend/member/loginMember";
        } else {
            List<ClickLike> clickLikeList = clickLikeService.getLikedArticlesByMember(myData.getMemNo());

            List<MyCoupon> list = myCouponSvc.getAllMyCouponMem(myData.getMemNo());
            System.out.println(list);
            List<MyCoupon> showMyCoupon = new ArrayList<>();
            for (MyCoupon mycoupons : list) {
                if (mycoupons.getCoupUsedStat() == 0) {
                    showMyCoupon.add(mycoupons);
                }
            }
            int myCouponQTY = showMyCoupon.size();
            // 將會員資料添加到模型中
            modelMap.addAttribute("myData", myData);
            modelMap.addAttribute("clickLikeList", clickLikeList);
            modelMap.addAttribute("myCouponQTY", myCouponQTY);

            return "/frontend/clicklike/oneMemberClickLike";
        }

    }


}
