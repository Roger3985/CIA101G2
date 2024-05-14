package com.iting.productorder.service.Impl;

import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.dao.ProductOrderRepository;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.iting.productorderdetail.dao.ProductOrderDetailRepository;
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
    ProductOrderDetailRepository productOrderDetailRepository;
    @Autowired
    MemberService memberSvc;
    @Autowired
    ProductOrderRepository repository;
    @Autowired
    CartService cartService;
    @Autowired
    ProductOrderDetailService productOrderDetailService;
    @Autowired
    CouponService couponService;
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

        ProductOrder productOrder = new ProductOrder();
        // 初始化訂單明細集合
        Set<ProductOrderDetail> orderDetails = new HashSet<>();
        productOrder.setProductOrderDetails(orderDetails);
        // 设置其他属性
        productOrder.setMemNo(cartRedis.getMemNo());
        BigDecimal productAllPrice = BigDecimal.ZERO; // Initialize productAllPrice
        // 將購物車商品轉換為訂單明細
        for (CartRedis cartItem : cartItems) {
            ProductOrderDetail orderDetail = new ProductOrderDetail();
            // 設定訂單明細相關屬性
            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
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
        // 返回訂單對象
        return productOrder;
    }



    @Override
    public void addOneProductOrderSuccess(ProductOrder productOrder) {
        int orderNoHash = Math.abs(UUID.randomUUID().hashCode());
        productOrder.setProductOrdNo(orderNoHash);
        productOrder.setMemNo(productOrder.getMember().getMemNo());
        productOrder.setProductOrdTime(Timestamp.valueOf(LocalDateTime.now()));
        productOrder.setProductByrEmail(productOrder.getMember().getMemMail());
        productOrder.setProductByrName(productOrder.getMember().getMemName());
        productOrder.setProductByrPhone(productOrder.getMember().getMemMob());
        productOrder.setProductAddr(productOrder.getMember().getMemAdd());

        Coupon coupon;
        if (productOrder.getCoupon() != null && productOrder.getCoupon().getCoupNo() != null) {
            coupon = couponService.getOneCoupon(productOrder.getCoupon().getCoupNo());
        } else {
            // 如果 coupon 或 coupNo 为空，则设置 coupon 为 id 为 1 的默认优惠券
            coupon = couponService.getOneCoupon(1);
        }
        productOrder.getCoupon().setCoupNo(coupon.getCoupNo());
        BigDecimal discount = coupon.getCoupDisc();
        BigDecimal totalPrice = productOrder.getProductAllPrice();
       productOrder.setProductRealPrice(discount.multiply(totalPrice));
        Set<ProductOrderDetail> orderDetails = new HashSet<>();
        productOrder.setProductOrderDetails(orderDetails);
        List<CartRedis> cartItems = cartService.findByCompositeKey(productOrder.getMemNo());
        BigDecimal productAllPrice = BigDecimal.ZERO; // Initialize productAllPrice

        // 将购物车商品转换为订单明细
        for (CartRedis cartItem : cartItems) {
            ProductOrderDetail orderDetail = new ProductOrderDetail();
            // 设置订单明细相关属性
            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
            compositeKey.setProductNo(cartItem.getProductNo());
            orderDetail.setCompositeKey(compositeKey);
            orderDetail.setProductPrice(cartItem.getProductPrice());
            orderDetail.setProductOrdQty(cartItem.getProductBuyQty());
            BigDecimal realPrice = BigDecimal.valueOf(cartItem.getProductBuyQty())
                    .multiply(cartItem.getProductPrice());
            orderDetail.setProductRealPrice(realPrice);
            // Increment productAllPrice by the real price of current order detail
            productAllPrice = productAllPrice.add(realPrice);
            // 设置订单编号
            orderDetail.getCompositeKey().setProductOrdNo(orderNoHash);
            // 加入订单明细 Set
            orderDetails.add(orderDetail);
        }
        // 保存订单
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
    @Override
    public ProductOrder getProductOrderByCoupon(Integer coupNo, Integer productOrdNo) {
        // 根據訂單編號查詢訂單詳情
        ProductOrder productOrder = getOneProductOrder(productOrdNo);

        // 根據優惠券編號查詢優惠券信息
        Coupon coupon = couponService.getOneCoupon(coupNo);

        // 更新訂單中的優惠券編號並計算相應的優惠金額
        if (productOrder != null && coupon != null) {
            // 更新優惠券編號
            productOrder.setCoupon(coupon);

            // 更新優惠金額和實付金額
            BigDecimal productDisc = productOrder.getProductDisc();
            BigDecimal productRealPrice = productOrder.getProductRealPrice();

            // 更新訂單的優惠金額
            productOrder.setProductDisc(productDisc);

            // 更新訂單的實付金額
            productOrder.setProductRealPrice(productRealPrice);

        }

        return productOrder;
    }



}
