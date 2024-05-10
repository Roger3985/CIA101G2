package com.chihyun.schedule.service;


import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
@Component
public class Scheduler {

    @Autowired
    MemberService memberService;

    @Autowired
    CouponService couponService;

    @Autowired
    MyCouponService myCouponService;

    //    設定排程器每天24:00執行發放優惠券至我的優惠券，發放完成後將優惠券狀態修改為"已發放"
    @Scheduled(cron = "* 0 0 * * *", zone = "Asia/Taipei")
    public void mycouponsRelease() {

        List<Coupon> list = couponService.getRel();
        for (Coupon coupon1 : list) {

            Coupon coupon = couponService.getOneCoupon(coupon1.getCoupNo());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Timestamp now_tsp = new Timestamp(System.currentTimeMillis());
            String today = dateFormat.format(now_tsp);

            Timestamp coupReltime_tsp = coupon.getCoupRelDate();
            String coupReltime = dateFormat.format(coupReltime_tsp);

            System.out.println(today + "排程器執行了");

            if (today.equals(coupReltime)) {

                System.out.println(coupon1.getCoupNo() + "發卷嚕!");

                List<Member> memberList = memberService.findAll();
                for (Member member : memberList) {

                    MyCoupon myCoupon = new MyCoupon();
                    MyCoupon.CompositeCouponMember compositeKey = new MyCoupon.CompositeCouponMember();
                    compositeKey.setMemNo(member.getMemNo());
                    compositeKey.setCoupNo(coupon.getCoupNo());

                    myCoupon.setCompositeCouponMember(compositeKey);
                    myCoupon.setCoupInfo(coupon.getCoupCond());
                    myCoupon.setCoupUsedStat(Byte.valueOf("0"));
                    myCoupon.setCoupExpDate(coupon.getCoupExpDate());
                    myCouponService.addMyCoupon(myCoupon);

                    coupon.setCoupRelStat(Byte.valueOf("1"));
                    couponService.updateCoupon(coupon);
                }
            }
        }
    }

    //    利用排程器檢查已失效的優惠券，將狀態設定為「已失效」。
    @Scheduled(cron = "* 0 0 * * *", zone = "Asia/Taipei")
    public void ExpMyCoupon() {
        List<MyCoupon> myCouponList = myCouponService.getExpired();
        for (MyCoupon myCoupon : myCouponList) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Timestamp now_tsp = new Timestamp(System.currentTimeMillis());
            String today = dateFormat.format(now_tsp);

            Timestamp coupExptime_tsp = myCoupon.getCoupExpDate();
            String coupExptime = dateFormat.format(coupExptime_tsp);

            if (today.equals(coupExptime)) {
                myCoupon.setCoupUsedStat(Byte.valueOf("2"));
                System.out.println("所有過期優惠券狀態修改為2");
                myCouponService.updateMyCoupon(myCoupon);
            }
        }
    }
}
