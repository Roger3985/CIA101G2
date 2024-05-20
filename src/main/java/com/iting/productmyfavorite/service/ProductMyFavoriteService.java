package com.iting.productmyfavorite.service;

import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productorderdetail.entity.ProductOrderDetail;

import java.util.List;

public interface ProductMyFavoriteService {
    public ProductMyFavorite findByProductNo(Integer  productNo);
    public List<ProductMyFavorite> findByMemNo(Integer memNo);
    public ProductMyFavorite findByProductNoAndMemNo(Integer productNo, Integer memNo);
    public List<ProductMyFavorite> findByProductProductNoAndMemberMemNo(Integer productNo, Integer memNo);
    public List<ProductMyFavorite> findAll();
    public List<ProductMyFavorite> findByCompositeKey(Integer productNo);

}
