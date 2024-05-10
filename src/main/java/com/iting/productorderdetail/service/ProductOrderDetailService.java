package com.iting.productorderdetail.service;

import com.iting.productorderdetail.entity.ProductOrderDetail;

import java.util.List;
import java.util.Set;

public interface ProductOrderDetailService {
    public List<ProductOrderDetail> findByCompositeKey(Integer productOrdNo);
    public List<ProductOrderDetail> getAll();
   public void updateProductOrderDetail(ProductOrderDetail productOrderDetail);
    public void addProductOrderDetail(ProductOrderDetail productOrderDetail);
  public  ProductOrderDetail findByproductOrdNoAndproductNo(Integer productOrdNo,Integer productNo);

 /////////////////////////////////////////////////////////////////////








}
