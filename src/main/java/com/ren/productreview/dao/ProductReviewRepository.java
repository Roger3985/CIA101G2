package com.ren.productreview.dao;

import com.ren.productreview.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

    @Transactional
    ProductReview findProductReviewByCompositeProductReview_MemNoAndCompositeProductReview_ProductNo(Integer memNo, Integer productNo);

    @Transactional
    List<ProductReview> findProductReviewsByCompositeProductReview_MemNo(Integer memNo);

    @Transactional
    List<ProductReview> findProductReviewsByCompositeProductReview_ProductNo(Integer productNo);

    @Transactional
    void deleteByCompositeProductReview_MemNoAndAndCompositeProductReview_ProductNo(Integer memNo, Integer productNo);

    @Transactional
    void deleteProductReviewsByCompositeProductReview_MemNo(Integer memNo);

    @Transactional
    void deleteProductReviewsByCompositeProductReview_ProductNo(Integer productNo);
}
