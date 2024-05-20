package com.iting.productorder.dao;

import com.iting.productorderdetail.entity.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.iting.productorder.entity.ProductOrder;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

        @Query(value = "SELECT * FROM productorder WHERE memno = ?1 ORDER BY CASE WHEN productOrdStat = 50 THEN 1 ELSE 0 END, productordtime DESC", nativeQuery = true)
        List<ProductOrder> findByMember(Integer memNo);



}