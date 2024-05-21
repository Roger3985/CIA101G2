package com.iting.productmyfavorite.dao;

import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

    @Repository
    public interface ProductMyFavoriteRepository extends JpaRepository<ProductMyFavorite, ProductMyFavorite.CompositeProductMyFavorite> {

        /**
         * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
         * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <MemberDTO, Long>。
         */
        @Transactional
        ProductMyFavorite findByProduct_ProductNo(Integer productNo);

        @Transactional
       List <ProductMyFavorite> findByMember_MemNo(Integer memNo); //memNo查詢

        @Transactional
        ProductMyFavorite findByProduct_ProductNoAndMember_MemNo(Integer productNo,Integer memNo); //複合主鍵查詢

        @Transactional
        List<ProductMyFavorite> findByProductProductNoAndMemberMemNo(Integer productNo, Integer memNo); //複合主鍵查詢


        @Query(value = "SELECT * FROM ProductMyFavorite WHERE productNo = ?1", nativeQuery = true)
        List<ProductMyFavorite> findByCompositeKey(Integer productNo);

    }


