package com.iting.productorder.dao;

import com.iting.productorderdetail.entity.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.iting.productorder.entity.ProductOrder;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    @Query(value = "SELECT * FROM productorder WHERE memno = ?1", nativeQuery = true)
    List<ProductOrder> findByMember(Integer memNo);
}