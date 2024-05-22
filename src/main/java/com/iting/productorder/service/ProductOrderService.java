package com.iting.productorder.service;

import com.iting.cart.entity.CartRedis;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorderdetail.entity.ProductOrderDetail;

import java.util.List;

public interface ProductOrderService {
//後端
public void addOneOrderSuccess(ProductOrder productOrder);
    public void updateProductOrder(ProductOrder productOrder);
public void addProductOrder(ProductOrder productOrder);
    public ProductOrder getOneProductOrder(Integer productOrdNo);
    public List<ProductOrder> getAll();
    public List<ProductOrder> findByMember(Integer memNo);
    public ProductOrder getProductOrderByCoupon(Integer coupNo,Integer productOrdNo);

//////////////////////////////////////////////////////////////////////////////////
public String ecpay(ProductOrder productOrder, String Names);
    public ProductOrder addOneProductOrder(CartRedis cartRedis);
    public String addOneProductOrderSuccess(ProductOrder productOrder);
}