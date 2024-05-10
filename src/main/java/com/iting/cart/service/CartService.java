package com.iting.cart.service;

import com.iting.cart.entity.Cart;

import com.iting.cart.entity.CartRedis;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

public interface CartService {

//    public List<Cart> findByCompositeKey(Integer memNo);
//   public void updateCart(Cart cart);
//    public void addCart(Cart cart);
//    public List<Cart> findByCompositeKeyProductNoAndCompositeKeyMemNo(Integer productNo,Integer memNo);
//    public void deleteBymemNo(Integer memNo) ;
//    public void deleteBymemNoAndProductNo(Integer productNo,Integer memNo) ;

    public void addCart(CartRedis cart);
    public void updateCart(CartRedis cartRedis);
//   public void deleteBymemNo(Integer memNo);
  public List<CartRedis> findByCompositeKey(Integer memNo);
    public void deleteBymemNoAndProductNo(Integer memNo, Integer productNo);
///////////////////////////////////////////////////////////
//    public void createOrderFromCart(Integer memNo);
//public List<CartRedis> findByCompositeKey(CartRedis cartRedis);
}
