package com.chihyun.schedule.service;


import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class Scheduler {

    @Autowired
    NoticeService noticeService;

    @Autowired
    MemberService memberService;

    @Autowired
    CouponService couponService;

    @Autowired
    MyCouponService myCouponService;

    //    設定排程器每天24:00執行發放優惠券至我的優惠券，發放完成後將優惠券狀態修改為"已發放"
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Taipei")
    public void mycouponsRelease() {

        List<Coupon> list = couponService.getRel();
        for (Coupon coupon : list) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Timestamp now_tsp = new Timestamp(System.currentTimeMillis());
            String today = dateFormat.format(now_tsp);

            Timestamp coupReltime_tsp = coupon.getCoupRelDate();
            String coupReltime = dateFormat.format(coupReltime_tsp);

            System.out.println(today + "排程器執行了");

            if (today.equals(coupReltime)) {

                System.out.println("優惠券編號" + coupon.getCoupNo() + "發卷給所有會員嚕!");
                List<Member> memberList = memberService.findAll();
                if (coupon.getCoupCond().equals("所有會員")) {
                    for (Member member : memberList) {

                        MyCoupon myCoupon = new MyCoupon();
                        MyCoupon.CompositeCouponMember compositeKey = new MyCoupon.CompositeCouponMember();
                        compositeKey.setMemNo(member.getMemNo());
                        compositeKey.setCoupNo(coupon.getCoupNo());

                        myCoupon.setCompositeCouponMember(compositeKey);
                        myCoupon.setCoupUsedStat(Byte.valueOf("0"));
                        myCoupon.setCoupExpDate(coupon.getCoupExpDate());
                        myCouponService.addMyCoupon(myCoupon);

                        coupon.setCoupRelStat(Byte.valueOf("1"));
                        couponService.updateCoupon(coupon);

                        Notice newNotice = new Notice();
                        newNotice.setMember(memberService.findByNo(member.getMemNo()));
                        String returnRemind = "您收到一張優惠券，請至我的優惠券查看詳情";
                        newNotice.setNotContent(returnRemind);
                        newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
                        newNotice.setNotStat((byte) 0);
                        noticeService.addNotice(newNotice);
                    }

                } else if (coupon.getCoupCond().equals("當月壽星")) {
                    List<Member> bdMemList = new ArrayList<>();
                    for (Member member : memberList) {

                        SimpleDateFormat dateFormatBD = new SimpleDateFormat("yyyy-MM-dd");
                        Date memBD_tsp = member.getMemBd();

                        String couponYMD = dateFormatBD.format(coupReltime_tsp);
                        String couponMM = couponYMD.substring(5, 7);
                        String memBdYMD = dateFormatBD.format(memBD_tsp);
                        String memBdMM = memBdYMD.substring(5, 7);

                        if (memBdMM.equals(couponMM)) {
                            bdMemList.add(member);
                        }
                    }
                    for (Member memberBd : bdMemList) {
                        System.out.println("優惠券編號" + coupon.getCoupNo() + "發卷給當月壽星嚕!");
                        MyCoupon myCoupon = new MyCoupon();
                        MyCoupon.CompositeCouponMember compositeKey = new MyCoupon.CompositeCouponMember();
                        compositeKey.setMemNo(memberBd.getMemNo());
                        compositeKey.setCoupNo(coupon.getCoupNo());

                        myCoupon.setCompositeCouponMember(compositeKey);
                        myCoupon.setCoupUsedStat(Byte.valueOf("0"));
                        myCoupon.setCoupExpDate(coupon.getCoupExpDate());
                        myCouponService.addMyCoupon(myCoupon);

                        coupon.setCoupRelStat(Byte.valueOf("1"));
                        couponService.updateCoupon(coupon);

                        Notice newNotice = new Notice();
                        newNotice.setMember(memberService.findByNo(memberBd.getMemNo()));
                        String returnRemind = "您收到一張優惠券，請至我的優惠券查看詳情";
                        newNotice.setNotContent(returnRemind);
                        newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
                        newNotice.setNotStat((byte) 0);
                        noticeService.addNotice(newNotice);
                    }
                }
            }
        }
    }


    //    利用排程器檢查已失效的優惠券，將狀態設定為「已失效」。
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Taipei")
    public void ExpMyCoupon() {
        List<MyCoupon> myCouponList = myCouponService.getExpired();
        for (MyCoupon myCoupon : myCouponList) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Timestamp now_tsp = new Timestamp(System.currentTimeMillis());
            String today = dateFormat.format(now_tsp);

            Timestamp coupExptime_tsp = myCoupon.getCoupon().getCoupExpDate();
            String coupExptime = dateFormat.format(coupExptime_tsp);

            if (today.equals(coupExptime)) {
                myCoupon.setCoupUsedStat(Byte.valueOf("2"));
                myCouponService.updateMyCoupon(myCoupon);
                System.out.println("所有過期優惠券狀態已修改為2");
            }
        }
    }
}
