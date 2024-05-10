package com.iting.productorder.service;

import com.iting.cart.entity.CartRedis;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorderdetail.entity.ProductOrderDetail;

import java.util.List;

public interface ProductOrderService {
//後端
public void addProductOrder(ProductOrder productOrder);

    public void updateProductOrder(ProductOrder productOrder) ;
    public ProductOrder getOneProductOrder(Integer productOrdNo);
    public List<ProductOrder> getAll();
    public List<ProductOrder> findByMember(Integer memNo);

//////////////////////////////////////////////////////////////////////////////////

    public ProductOrder addOneProductOrder(CartRedis cartRedis);
    public void addOneProductOrderSuccess(ProductOrder productOrder);
}