package com.iting.productmyfavorite.service.impl;

import com.iting.productmyfavorite.dao.ProductMyFavoriteRepository;
import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productmyfavorite.service.ProductMyFavoriteService;
import com.ren.product.dao.ProductRepository;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productMyFavoriteService")
public class ProductMyFavoriteImpl implements ProductMyFavoriteService {
    //單筆查詢(rentalNo)
    @Autowired //自動裝配
    private ProductMyFavoriteRepository repository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ProductMyFavorite findByProductNo(Integer  productNo) {
        return repository.findByProduct_ProductNo( productNo);
    }

    //單筆查詢
    @Override
    public List<ProductMyFavorite> findByMemNo(Integer memNo) {
        return repository.findByMember_MemNo(memNo);
    }

    //複合主鍵查詢
    @Override
    public ProductMyFavorite findByProductNoAndMemNo(Integer productNo, Integer memNo) {
        return repository.findByProduct_ProductNoAndMember_MemNo(productNo, memNo);
    }

    //複合主鍵查詢
    @Override
    public List<ProductMyFavorite> findByProductProductNoAndMemberMemNo(Integer productNo, Integer memNo) {
        return repository.findByProductProductNoAndMemberMemNo(productNo, memNo);
    }

    @Override
    public List<ProductMyFavorite> findByCompositeKey(Integer productNo) {
        return repository.findByCompositeKey(productNo);
    }

    //全部查詢
    @Override
    public List<ProductMyFavorite> findAll() {
        return repository.findAll();
    }




}
