package com.ren.product.dao;

import com.ren.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 根據關鍵字搜尋產品
    @Transactional
    List<Product> findByProductNameContaining(String keyword);
    @Transactional
    List<Product> findByProductNameContainingAndProductCategoryProductCatNo(String keyword, Integer productCatNo);
    @Transactional
    List<Product> findByProductCategoryProductCatNo(Integer productCatNo);
    // 新增方法以獲取所有上架的商品
    @Transactional
    List<Product> findByProductStat(Integer productStat);
    // 新增方法以根據商品編號查找商品
    @Transactional
    Optional<Product> findByProductNo(Integer productNo);

    @Transactional
    @Modifying
    @Query("SELECT p FROM Product p WHERE " +
//            "(:productNo IS NULL OR p.productNo = :productNo) AND " +
//            "(:productCatNo IS NULL OR p.productCatNo = :productCatNo) AND " +
            "(:productName IS NULL OR p.productName = :productName) AND " +
            "(:productInfo IS NULL OR p.productInfo = :productInfo) AND " +
            "(:productSize IS NULL OR p.productSize = :productSize) AND " +
            "(:productColor IS NULL OR p.productColor = :productColor) AND " +
            "(:productPrice IS NULL OR p.productPrice = :productPrice) AND " +
            "(:productStat IS NULL OR p.productStat = :productStat) AND " +
            "(:productSalQty IS NULL OR p.productSalQty = :productSalQty) AND " +
            "(:productComPeople IS NULL OR p.productComPeople = :productComPeople) AND " +
            "(:productComScore IS NULL OR p.productComScore = :productComScore)")
    List<Product> findByAttributes(
//            @Param("productNo") Integer productNo,
//            @Param("productCatNo") Integer productCatNo,
            @Param("productName") String productName,
            @Param("productInfo") String productInfo,
            @Param("productSize") Integer productSize,
            @Param("productColor") String productColor,
            @Param("productPrice") BigDecimal productPrice,
            @Param("productStat") Byte productStat,
            @Param("productSalQty") Integer productSalQty,
            @Param("productComPeople") Integer productComPeople,
            @Param("productComScore") Integer productComScore
    );

}
