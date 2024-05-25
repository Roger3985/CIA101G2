package com.chihyun.schedule.service;

import com.chihyun.coupon.dao.CouponRepository;
import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import com.roger.member.service.impl.MemberServiceImpl;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import com.roger.notice.service.impl.NoticeServiceImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class Test_mycoupon {
    public static void main(String[] args) {

//        CouponRepository repository = new CouponRepository();
        CouponService couponService = new CouponService();
        MyCouponService myCouponService = new MyCouponService();
        MemberService memberService = new MemberServiceImpl();
        NoticeService noticeService = new NoticeServiceImpl();


        List<Coupon> list = couponService.getRel();
        for (Coupon coupon : list) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Timestamp now_tsp = new Timestamp(System.currentTimeMillis());
            String today = dateFormat.format(now_tsp);

            Timestamp coupReltime_tsp = coupon.getCoupRelDate();
            String coupReltime = dateFormat.format(coupReltime_tsp);

            System.out.println(today + "排程器執行了");

            if (today.equals(coupReltime)) {

                System.out.println(coupon.getCoupNo() + "發卷嚕!");

                List<Member> memberList = memberService.findAll();
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
            }
        }

    }
}
