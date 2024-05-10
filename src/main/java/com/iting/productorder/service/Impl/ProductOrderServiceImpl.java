package com.iting.productorder.service.Impl;

import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.dao.ProductOrderRepository;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service("productOrderService")
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    MemberService memberSvc;
    @Autowired
    ProductOrderRepository repository;
    @Autowired
    CartService cartService;
    @Autowired
    ProductOrderDetailService productOrderDetailService;
    @Override
    public List<ProductOrder> findByMember(Integer memNo){
        return repository.findByMember(memNo);
    }
    @Override
    public void addProductOrder(ProductOrder productOrder) {

        repository.save(productOrder);
    }

    @Override
    public ProductOrder addOneProductOrder(CartRedis cartRedis) {
        // 根據會員編號查詢購物車中的商品
        List<CartRedis> cartItems = cartService.findByCompositeKey(cartRedis.getMemNo());
        // 創建一個 Set 來存儲訂單明細
        Set<ProductOrderDetail> orderDetails = new HashSet<>();
        ProductOrder productOrder = new ProductOrder();
//        int orderNoHash = Math.abs(UUID.randomUUID().hashCode());
//        productOrder.setProductOrdNo(orderNoHash);
        productOrder.setMemNo(cartRedis.getMemNo());
        BigDecimal productAllPrice = BigDecimal.ZERO; // Initialize productAllPrice
        // 將購物車商品轉換為訂單明細
        for (CartRedis cartItem : cartItems) {
            ProductOrderDetail orderDetail = new ProductOrderDetail();
            // 設定訂單明細相關屬性
            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
//            compositeKey.setProductOrdNo(orderNoHash);
            compositeKey.setProductNo(cartItem.getProductNo());
            orderDetail.setCompositeKey(compositeKey);
            orderDetail.setProductPrice(cartItem.getProductPrice());
            orderDetail.setProductOrdQty(cartItem.getProductBuyQty());
            BigDecimal realPrice = BigDecimal.valueOf(cartItem.getProductBuyQty())
                    .multiply(cartItem.getProductPrice());
            orderDetail.setProductRealPrice(realPrice);
            // Increment productAllPrice by the real price of current order detail
            productAllPrice = productAllPrice.add(realPrice);
            // 加入訂單明細 Set
            orderDetails.add(orderDetail);
        }
        // Set productAllPrice to productOrder's ProductAllPrice
        productOrder.setProductAllPrice(productAllPrice);
        // 將訂單明細 Set 設定到訂單對象中
        productOrder.setProductOrderDetails(orderDetails);
        // 保存訂單到資料庫中
//         repository.save(productOrder);
       return productOrder;
    }

    @Override
    public void addOneProductOrderSuccess(ProductOrder productOrder) {
productOrder.setProductOrdTime(Timestamp.valueOf(LocalDateTime.now()));

        repository.save(productOrder);
    }
    @Override
    public void updateProductOrder(ProductOrder productOrder) {
        Optional<ProductOrder> existingOrder = repository.findById(productOrder.getProductOrdNo());
        if (existingOrder.isPresent()) {
            repository.save(productOrder);
        }
    }

    @Override
    public List<ProductOrder> getAll() {
        return repository.findAll();
    }

    @Override
    public ProductOrder getOneProductOrder(Integer productOrdNo) {
        Optional<ProductOrder> optional = repository.findById(productOrdNo);
        return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
    }


}
