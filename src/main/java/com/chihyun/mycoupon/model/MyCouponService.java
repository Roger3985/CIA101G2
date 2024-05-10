package com.chihyun.mycoupon.model;

import com.chihyun.coupon.entity.Coupon;
import com.chihyun.mycoupon.dao.MyCouponRepository;
import com.chihyun.mycoupon.entity.MyCoupon;
import com.roger.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MyCouponService {

    @Autowired
    Member member;
    @Autowired
    Coupon coupon;

    @Autowired
    MyCouponRepository repository;

    public void addMyCoupon(MyCoupon myCoupon) {
        repository.save(myCoupon);
    }

    public void addMyCoupon(Set<MyCoupon> myCoupons) {
        repository.saveAll(myCoupons);
    }

    public void updateMyCoupon(MyCoupon myCoupon) {
        repository.save(myCoupon);
    }

    public List<MyCoupon> getAll() {
        List<MyCoupon> listall = repository.findAll();
        return listall;
    }

    public List<MyCoupon> getExpired() {
        List<MyCoupon> ExpMyCouponsList = repository.findAll();
        for (MyCoupon expMyCoupons : ExpMyCouponsList) {
            if (expMyCoupons.getCoupUsedStat().equals(0)) {
                ExpMyCouponsList.add(expMyCoupons);
            }
        }
        return ExpMyCouponsList;
    }

    //前端會員登入後用
    public Optional<MyCoupon> getOneMyCoupon(Integer coupNo, Integer memNo) {
        Optional<MyCoupon> optional = repository.findMyCouponsByCompositeCouponMember_CoupNoAndCompositeCouponMember_MemNo(coupNo, memNo);
        return optional;
    }

    public List<MyCoupon> getAllMyCouponMem(Integer memNo) {
        List<MyCoupon> list = repository.findMyCouponsByCompositeCouponMember_MemNo(memNo);
        return list;
    }


}