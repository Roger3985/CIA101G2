package com.iting.productorder.controller.frontend;

import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.google.gson.JsonObject;
import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.entity.uniqueAnnotation.Create;
import com.roger.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("productOrder")
@RequestMapping("/frontend/productorder")

public class ProductOrderController {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    ProductOrderService productOrderSvc;
    @Autowired
    CartService cartSvc;
    @Autowired
    MemberService memberService;
    @Autowired
    ProductOrderDetailService productOrderDetailService;
    @Autowired
    CouponService couponService;
    @Autowired
    MyCouponService myCouponService;
    @Autowired
    ProductPictureServiceImpl productPictureService;


    @PostMapping("/submitOrder")
    @ResponseBody
    public ResponseEntity<String> submitOrder(@RequestParam("productByrName") String productByrName,
                                              @RequestParam("productByrPhone") String productByrPhone,
                                              @RequestParam("productByrEmail") String productByrEmail,
                                              @RequestParam("productRcvName") String productRcvName,
                                              @RequestParam("productRcvPhone") String productRcvPhone,
                                              @RequestParam("productTakeMethod") Byte productTakeMethod,
                                              @RequestParam("productAddr") String productAddr,
                                              @RequestParam("productPayMethod") Byte productPayMethod,
                                              @RequestParam("productAllPrice") BigDecimal productAllPrice,
                                              @RequestParam(value = "coupNo", required = false) Integer coupNo,
                                              HttpSession session) {
        // 获取 session 中的 member 对象
        Member myData = (Member) session.getAttribute("loginsuccess"); // 强制转换为 Member 类型

        // 未登录
        if (myData == null) {
            session.setAttribute("location", "/frontend/productorder/submitOrder");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("frontend/member/loginMember");
        }

        // 创建并设置 ProductOrder 对象
        ProductOrder productOrder = new ProductOrder();
        productOrder.setMember(myData); // 设置 Member 对象
        productOrder.setMemNo(myData.getMemNo());
        productOrder.setProductByrName(productByrName);
        productOrder.setProductByrPhone(productByrPhone);
        productOrder.setProductByrEmail(productByrEmail);
        productOrder.setProductRcvName(productRcvName);
        productOrder.setProductRcvPhone(productRcvPhone);
        productOrder.setProductAddr(productAddr);
        productOrder.setProductTakeMethod(productTakeMethod);
        productOrder.setProductPayMethod(productPayMethod);
        productOrder.setProductAllPrice(productAllPrice);

// 声明 coupon 变量并初始化为 null
        final Coupon coupon;

// 检查是否存在优惠券
        Optional<MyCoupon> myCouponOptional = myCouponService.getOneMyCoupon(coupNo, myData.getMemNo());

// 如果存在优惠券，设置优惠券的使用状态并获取对应的 Coupon 对象
        if (myCouponOptional.isPresent()) {
            MyCoupon myCoupon = myCouponOptional.get();
            myCoupon.setCoupUsedStat((byte) 1);
            coupon = myCoupon.getCoupon();
        } else {
            coupon = null; // 如果不存在优惠券，则将 coupon 设置为 null
        }

// 设置订单对象的优惠券信息
        productOrder.setCoupon(coupon);

        // 设置订单对象的优惠券信息
        String result = productOrderSvc.addOneProductOrderSuccess(productOrder);
        cartSvc.deleteBymemNo(myData.getMemNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }



//        /return "redirect:" + result;


    @PostMapping("insertProductOrderSuccess")
    public String insertProductOrderSuccess(@Validated(Create.class) ProductOrder productOrder,
                                            BindingResult result,
                                            ModelMap model,
                                            @RequestParam(value = "coupNo", required = false) Integer coupNo,
                                            HttpSession session) {
        Member myData;
        Object memNo = 0; // 声明并初始化memNo为Object类型

        if (session.getAttribute("loginsuccess") == null) {

        } else {
            myData = (Member) session.getAttribute("loginsuccess"); // 强制转换为Member类型
            memNo = myData.getMemNo();
        }

        if (coupNo == null) {
            coupNo = 1;

        } else {
            myCouponService.getOneMyCoupon(coupNo, (Integer) memNo)
                    .ifPresent(myCoupon -> myCoupon.setCoupUsedStat((byte) 1));
        }


        /*************************** 1.接收请求参数 - 输入格式的错误处理 ************************/
        productOrder.setCoupon(couponService.getOneCoupon(coupNo));
        productOrderSvc.addOneProductOrderSuccess(productOrder);

        cartSvc.deleteBymemNo((Integer) memNo);


        return "frontend/cart/ProductOrderSuccess";
    }

//    @ResponseBody
//    @PostMapping("/ecpay")
//    public String ecpay(@RequestParam Integer productOrderNo) {
//
//        String result = productOrderSvc.();
//        return result;
////        /return "redirect:" + result;
//
//    }

    @PostMapping("insertOrder")
    public String insertOrder(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpSession session) {
        // 获取 session 中的 member 对象
        Member myData;
        myData = (Member) session.getAttribute("loginsuccess"); // 强制转换为 Member 类型

        // 未登录
        if (myData == null) {
            return "redirect:frontend/member/loginMember";
        }

        // 获取 memNo
        Integer memNo = myData.getMemNo();

        // 创建产品订单
        ProductOrder productOrder = productOrderSvc.addOneProductOrder(cartRedis);
      productOrder.setMember(memberService.findByNo(memNo));
        productOrder.setProductOrdStat((byte) 40);
        productOrder.setProductStat((byte) 0);

        // 获取所有优惠券
        List<MyCoupon> myCoupons = myCouponService.getAllMyCouponMem(memNo);
        // 过滤出 CoupUsedStat 不等于 1 的优惠券
        List<MyCoupon> filteredCoupons = myCoupons.stream()
                .filter(coupon -> coupon.getCoupUsedStat() != 1)
                .collect(Collectors.toList());
        // 获取购物车数据
        List<CartRedis> cartListData = cartSvc.findByCompositeKey(memNo);
        for (CartRedis cartItem : cartListData) {
            Integer cartProductNo = cartItem.getProductNo();
            List<ProductPicture> productPictures = productPictureService.getByProductNo(cartProductNo);
            if (productPictures != null && !productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                Integer productNo = firstProductPicture.getProduct().getProductNo();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                if (session.getAttribute("productImage" + productNo) == null) {
                    session.setAttribute("productImage" + productNo, base64Image);
                }
                model.addAttribute("productImage" + productNo, base64Image);
            }
        }

        // 添加模型属性
        model.addAttribute("coupons", filteredCoupons);
        model.addAttribute("productOrder", productOrder);
        session.setAttribute("productOrder", productOrder); // 将订单存储在会话中

        return "frontend/cart/CartToProductOrderDetail";
    }




    public BindingResult removeFieldError(ProductOrder productOrder, BindingResult result, String removedFieldName) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldError -> !fieldError.getField().equals(removedFieldName))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(productOrder, "productOrder");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    @GetMapping("CartEnd")
    public String CartEnd(ModelMap model, HttpSession session) {
        Member myData;
        myData = (Member) session.getAttribute("loginsuccess");
        Integer memNo = myData.getMemNo();
        // 使用memNo执行您的逻辑
        List<ProductOrder> list = productOrderSvc.findByMember(memNo);
        model.addAttribute("productorderListData", list);
        return "frontend/cart/CartEnd";
    }

    @PostMapping("MemberGetAll")
    public String getAll(@RequestParam("productOrdNo") Integer productOrdNo, @RequestParam("productNo") Integer productNo, ModelMap model) {
        ProductOrderDetail productOrderDetail = productOrderDetailService.findByproductOrdNoAndproductNo(productOrdNo, productNo);
        model.addAttribute("productOrderDetail", productOrderDetail);
        Product product = productService.getOneProduct(productNo);
        model.addAttribute("product", product);
        return "frontend/cart/ProductScorce";
    }

    //    @PostMapping("getAll")
//    public String getAll(@RequestParam("memNo") Integer memNo,@RequestParam("productNo") Integer productNo, ModelMap model) {
//        ProductOrderDetail productOrderDetail= productOrderDetailService.findByproductOrdNoAndproductNo(productOrdNo,productNo);
//        model.addAttribute("productOrderDetail", productOrderDetail);
//        Product product= productService.getOneProduct(productNo);
//        model.addAttribute("product", product);
//        return "frontend/cart/ProductScorce";
//    }
    @PostMapping("/coupNoInstantly")
    @ResponseBody
    public ResponseEntity<String> updatePriceInstantly(@RequestParam("coupno.coupNo") String coupNo,
                                                       @RequestParam("productAllPrice") BigDecimal productAllPrice) {
        // 如果coupNo为null或者空字符串，则设为默认值1
        int couponNumber = 1; // 默认值
        if (coupNo != null && !coupNo.isEmpty()) {
            couponNumber = Integer.parseInt(coupNo);
        }


        Coupon coupon = couponService.getOneCoupon(couponNumber);

        // 如果获取的coupon为null，可以考虑返回错误或者默认值
        if (coupon == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon not found");
        }

        BigDecimal productRealPrice = productAllPrice.multiply(coupon.getCoupDisc());
        BigDecimal productDisc = productAllPrice.subtract(productRealPrice);

        // 创建一个 JSON 对象，包含所需的字段
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("productDisc", productDisc.toString());
        jsonObject.addProperty("productRealPrice", productRealPrice.toString());

        // 返回 JSON 格式的响应
        return ResponseEntity.ok().body(jsonObject.toString());


    }
}