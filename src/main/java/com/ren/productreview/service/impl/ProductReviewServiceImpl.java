package com.ren.productreview.service.impl;

import com.ren.productreview.dao.ProductReviewRepository;
import com.ren.productreview.entity.ProductReview;
import com.ren.productreview.service.ProductReivewService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReivewService_interface {

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Override
    public ProductReview addProductReview(ProductReview productReview) {
        return productReviewRepository.save(productReview);
    }

    @Override
    public ProductReview getOneProductReview(Integer memNo, Integer productNo) {
        return productReviewRepository.findProductReviewByCompositeProductReview_MemNoAndCompositeProductReview_ProductNo(memNo, productNo);
    }

    @Override
    public List<ProductReview> getByMemNo(Integer memNo) {
        return productReviewRepository.findProductReviewsByCompositeProductReview_MemNo(memNo);
    }

    @Override
    public List<ProductReview> getByProductNo(Integer productNo) {
        return productReviewRepository.findProductReviewsByCompositeProductReview_ProductNo(productNo);
    }

    @Override
    public List<ProductReview> getAll() {
        return productReviewRepository.findAll();
    }

    @Override
    public ProductReview updateProductReview(ProductReview productReview) {
        return productReviewRepository.save(productReview);
    }

    @Override
    public void deleteProductReview(Integer memNo, Integer productNo) {
        productReviewRepository.deleteByCompositeProductReview_MemNoAndAndCompositeProductReview_ProductNo(memNo, productNo);
    }

    @Override
    public void deleteByProductNo(Integer memNo) {
        productReviewRepository.deleteProductReviewsByCompositeProductReview_MemNo(memNo);
    }

    @Override
    public void deleteByMemNo(Integer productNo) {
        productReviewRepository.deleteProductReviewsByCompositeProductReview_ProductNo(productNo);
    }
}
