package com.chihyun.mycoupon.dao;

import com.chihyun.mycoupon.entity.MyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyConponCompositeRepository extends JpaRepository<MyCoupon, MyCoupon.CompositeCouponMember> {

}
