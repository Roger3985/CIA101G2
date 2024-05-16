package com.iting.cart.service;

import com.iting.cart.entity.Cart;

import com.iting.cart.entity.CartRedis;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    public void addCart(List<CartRedis> cart);
    public void updateCart(CartRedis cartRedis);
   public void deleteBymemNo(Integer memNo);
  public List<CartRedis> findByCompositeKey(Integer memNo);
    public void deleteBymemNoAndProductNo(Integer memNo, Integer productNo);
///////////////////////////////////////////////////////////
//    public void createOrderFromCart(Integer memNo);
//public List<CartRedis> findByCompositeKey(CartRedis cartRedis);
public void addCart(CartRedis cart);
    public void updateCart(Integer productNo,Integer memNo,Integer productBuyQty);
}
