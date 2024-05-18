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


    @PostMapping("insertOrder")
    public String insertOrder(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpSession session) {
//        Member member= new Member();
//        member.setMemNo(1);
        Object memNo = 0; // 声明并初始化memNo为Object类型
//
//       session.setAttribute("member",member);
        Member member = (Member) session.getAttribute("member"); // 强制转换为Member类型
        memNo = member.getMemNo();

//未登入
        if (session.getAttribute("member") == null) {
            return "/backend/login";
        } else {
            ProductOrder productOrder = productOrderSvc.addOneProductOrder(cartRedis);
            member = memberService.findByNo((Integer) memNo);
            productOrder.setMember(member);
            productOrder.setProductOrdStat(Byte.valueOf((byte) 40));
            productOrder.setProductStat(Byte.valueOf((byte) 0));
// 获取所有优惠券
            List<MyCoupon> myCoupons = myCouponService.getAllMyCouponMem((Integer) memNo);
// 过滤出 CoupUsedStat 不等于 1 的优惠券
            List<MyCoupon> filteredCoupons = myCoupons.stream()
                    .filter(coupon -> coupon.getCoupUsedStat() != 1)
                    .collect(Collectors.toList());
            model.addAttribute("coupons", filteredCoupons);
            model.addAttribute("productOrder", productOrder);
            session.setAttribute("productOrder", productOrder); // 將訂單存儲在會話中
            return "frontend/cart/CartToProductOrderDetail";
        }
    }

    @PostMapping("insertProductOrderSuccess")
    public String insertProductOrderSuccess(@Validated(Create.class) ProductOrder productOrder,
                                            BindingResult result,
                                            ModelMap model,
                                            @RequestParam(value = "coupNo", required = false) Integer coupNo,
                                            HttpSession session) {
        Member member;
        Object memNo = 0; // 声明并初始化memNo为Object类型

        if (session.getAttribute("member") == null) {
            memNo = session.getAttribute("memNo"); // 将memNo设为session中的memNo值
        } else {
            member = (Member) session.getAttribute("member"); // 强制转换为Member类型
            memNo = member.getMemNo();
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
        Member member = (Member) session.getAttribute("member"); // 强制转换为Member类型
        Integer memNo = member.getMemNo();
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