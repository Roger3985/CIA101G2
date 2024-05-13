package com.ren.productreview.dao;

import com.ren.productreview.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<Integer, ProductReview> {
}
