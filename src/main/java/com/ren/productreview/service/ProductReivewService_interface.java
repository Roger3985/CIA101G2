package com.ren.productreview.service;

import com.ren.productreview.entity.ProductReview;

import java.util.List;

public interface ProductReivewService_interface {

    ProductReview addProductReview(ProductReview productReview);

    ProductReview getOneProductReview(Integer memNo, Integer productNo);

    List<ProductReview> getByMemNo(Integer memNo);

    List<ProductReview> getByProductNo(Integer productNo);

    List<ProductReview> getAll();

    ProductReview updateProductReview(ProductReview productReview);

    void deleteProductReview(Integer memNo, Integer productNo);

    void deleteByProductNo(Integer memNo);

    void deleteByMemNo(Integer memNo);
}
