
package com.iting.productorder.service.Impl;
import ecpay.logistics.integration.exception.EcpayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.howard.rentalorder.dto.RentalOrderRequest;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorderdetails.entity.RentalOrderDetails;
import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.dao.ProductOrderRepository;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.iting.productorderdetail.dao.ProductOrderDetailRepository;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import com.yu.rental.entity.Rental;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    ProductServiceImpl productService;


    @Override
    public List<ProductOrder> findByMember(Integer memNo) {
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
            Product product = productService.getOneProduct(cartItem.getProductNo());
            orderDetail.setProduct(product);
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
    @Transactional
    public String addOneProductOrderSuccess(ProductOrder productOrder) {
        // 生成订单号
        int orderNoHash = Math.abs(UUID.randomUUID().hashCode());
        productOrder.setProductOrdNo(orderNoHash);

        // 获取订单中的会员信息
        Member member = productOrder.getMember();

        productOrder.setProductOrdTime(Timestamp.valueOf(LocalDateTime.now()));

        productOrder.setProductStat((byte) 0);
        productOrder.setProductOrdStat((byte) 40);

        // 处理优惠券信息
        Coupon coupon = productOrder.getCoupon();
        if (coupon == null || coupon.getCoupNo() == null) {
            // 设置默认的优惠券编号
            coupon = couponService.getOneCoupon(1);
        } else {
            coupon = couponService.getOneCoupon(coupon.getCoupNo());
        }
        BigDecimal discount = coupon.getCoupDisc();
        BigDecimal totalPrice = productOrder.getProductAllPrice();
        productOrder.setProductDisc(discount);
        productOrder.setProductRealPrice(discount.multiply(totalPrice));

        // 构建订单明细
        Set<ProductOrderDetail> orderDetails = new HashSet<>();
        List<CartRedis> cartItems = cartService.findByCompositeKey(member.getMemNo());
        for (CartRedis cartItem : cartItems) {
            ProductOrderDetail orderDetail = new ProductOrderDetail();
            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
            compositeKey.setProductNo(cartItem.getProductNo());
            orderDetail.setCompositeKey(compositeKey);
            orderDetail.setProductPrice(cartItem.getProductPrice());
            orderDetail.setProductOrdQty(cartItem.getProductBuyQty());
            BigDecimal realPrice = BigDecimal.valueOf(cartItem.getProductBuyQty())
                    .multiply(cartItem.getProductPrice());
            orderDetail.setProductRealPrice(realPrice);
            compositeKey.setProductOrdNo(orderNoHash);
            orderDetails.add(orderDetail);
        }
        productOrder.setProductOrderDetails(orderDetails);

        productOrder.setProductStat((byte) 1);
        repository.save(productOrder);

        // 调用支付接口
        String itemNames = cartItems.stream().map(CartRedis::getProductName).collect(Collectors.joining("#"));
        return ecpay(productOrder, itemNames);
    }



    private static final Logger logger = LoggerFactory.getLogger(ProductOrderServiceImpl.class);

    @Override
    public String ecpay(ProductOrder productOrder, String itemNames)
    {

        try {

            AllInOne all = new AllInOne("");
            AioCheckOutALL obj = new AioCheckOutALL();
            BigDecimal AllPrice = productOrder.getProductRealPrice();
// 将 BigDecimal 类型的金额转换为整数，这里采用向下取整的方式
            String totalAmount = AllPrice.setScale(0, RoundingMode.DOWN).toString();
            obj.setMerchantTradeNo("MKeQ"  + productOrder.getProductOrdNo());//订单编号
// 交易时间(先把毫秒部分切掉)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            obj.setMerchantTradeDate(sdf.format(productOrder.getProductOrdTime()));  //订单日期
            obj.setTotalAmount(totalAmount);  // 将 BigDecimal 转换为字符串并设置给 TotalAmount
            //訂單全部總金額
            obj.setTradeDesc("test Description");
            obj.setItemName(itemNames);
            obj.setReturnURL("http://211.23.128.214:5000");
            //		obj.setOrderResultURL("http://127.0.0.1:5502");
            //		obj.setOrderResultURL("https://www.google.com.tw/");
            obj.setOrderResultURL("http://localhost:8080/frontend/cart/ProductOrderSuccess");  //付款完跳轉的頁面
            obj.setNeedExtraPaidInfo("N");
            String form = all.aioCheckOut(obj, null);

            return form;

        } catch (EcpayException e) {
            // 返回空表单或者其他处理
            return null;
        }
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


    @Override
    @Transactional
    public void addOneOrderSuccess(ProductOrder productOrder) {
        // 生成订单号
        int orderNoHash = Math.abs(UUID.randomUUID().hashCode());
        productOrder.setProductOrdNo(orderNoHash);

        // 获取订单中的会员信息
        Member member = productOrder.getMember();

        productOrder.setProductOrdTime(Timestamp.valueOf(LocalDateTime.now()));

        productOrder.setProductStat((byte) 0);
        productOrder.setProductOrdStat((byte) 40);

        // 处理优惠券信息
        Coupon coupon = productOrder.getCoupon();
        if (coupon == null || coupon.getCoupNo() == null) {
            // 设置默认的优惠券编号
            coupon = couponService.getOneCoupon(1);
        } else {
            coupon = couponService.getOneCoupon(coupon.getCoupNo());
        }
        BigDecimal discount = coupon.getCoupDisc();
        BigDecimal totalPrice = productOrder.getProductAllPrice();
        productOrder.setProductDisc(discount);
        productOrder.setProductRealPrice(discount.multiply(totalPrice));

        // 构建订单明细
        Set<ProductOrderDetail> orderDetails = new HashSet<>();
        List<CartRedis> cartItems = cartService.findByCompositeKey(member.getMemNo());
        for (CartRedis cartItem : cartItems) {
            ProductOrderDetail orderDetail = new ProductOrderDetail();
            ProductOrderDetail.CompositeDetail compositeKey = new ProductOrderDetail.CompositeDetail();
            compositeKey.setProductNo(cartItem.getProductNo());
            orderDetail.setCompositeKey(compositeKey);
            orderDetail.setProductPrice(cartItem.getProductPrice());
            orderDetail.setProductOrdQty(cartItem.getProductBuyQty());
            BigDecimal realPrice = BigDecimal.valueOf(cartItem.getProductBuyQty())
                    .multiply(cartItem.getProductPrice());
            orderDetail.setProductRealPrice(realPrice);
            compositeKey.setProductOrdNo(orderNoHash);
            orderDetails.add(orderDetail);
        }
        productOrder.setProductOrderDetails(orderDetails);

        productOrder.setProductStat((byte) 0);
        repository.save(productOrder);

    }
}
