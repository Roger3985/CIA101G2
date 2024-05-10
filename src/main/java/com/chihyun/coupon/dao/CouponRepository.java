package com.chihyun.coupon.dao;

import com.chihyun.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    @Transactional
    @Modifying
    @Query(value = "delete from coupon where coupNo = ?", nativeQuery = true)
    void deleteById(int coupNo);

}
