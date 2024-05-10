package com.chihyun.mycoupon.dao;

import com.chihyun.mycoupon.entity.MyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyCouponRepository extends JpaRepository<MyCoupon, Integer> {

    List<MyCoupon> findMyCouponsByCompositeCouponMember_MemNo(Integer memNo);

    Optional<MyCoupon> findMyCouponsByCompositeCouponMember_CoupNoAndCompositeCouponMember_MemNo(Integer coupNo, Integer memNo);
}
