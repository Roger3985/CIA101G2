package com.iting.productorderdetail.dao;

import com.iting.productorderdetail.entity.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderDetailRepository extends JpaRepository<ProductOrderDetail, ProductOrderDetail.CompositeDetail> {

    @Query(value = "SELECT * FROM productorderdetail WHERE productOrdNo = ?1", nativeQuery = true)
    List<ProductOrderDetail> findByCompositeKey(Integer productOrdNo);



@Query(value = "select * from productorderdetail p where p.productOrdNo = :productOrdNo and p.productNo = :productNo", nativeQuery = true)
    ProductOrderDetail findByproductOrdNoAndproductNo(@Param("productOrdNo") Integer productOrdNo, @Param("productNo") Integer productNo);


    }



