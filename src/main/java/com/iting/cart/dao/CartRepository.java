package com.iting.cart.dao;

import com.iting.cart.entity.Cart;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Cart.CompositeDetail> {
//    @Transactional
//    @Modifying
//    @Query(value = "SELECT * FROM cart WHERE memno = ?1", nativeQuery = true)
//    List<Cart> findByCompositeKey(Integer memNo);
//    @Transactional
//    @Modifying
//    @Query(value = "select * from Cart p where p.productNo = :productNo and p.memNo = :memNo", nativeQuery = true)
//    List<Cart> findByCompositeKeyProductNoAndCompositeKeyMemNo(Integer productNo, Integer memNo);
//    @Transactional
//    @Modifying
//    @Query(value = "delete from Cart where memNo =?1", nativeQuery = true)
//    void deleteByCompositeKey_MemNo(Integer memNo);
//    @Transactional
//    @Modifying
//    @Query(value = "delete from Cart p where p.productNo = :productNo and p.memNo = :memNo", nativeQuery = true)
//    void deleteByCompositeKey_MemNoAndproductNo(Integer productNo, Integer memNo);
}
