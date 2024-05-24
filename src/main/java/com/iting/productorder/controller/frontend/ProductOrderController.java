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
    private ProductServiceImpl productService;
    @Autowired
    private ProductOrderService productOrderSvc;
    @Autowired
    private CartService cartSvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductOrderDetailService productOrderDetailService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private MyCouponService myCouponService;
    @Autowired
    private ProductPictureServiceImpl productPictureService;

    private static final int DEFAULT_COUPON_NUMBER = 1;

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
        Member myData = (Member) session.getAttribute("loginsuccess");
        session.setAttribute("loginsuccess",myData);
        if (myData == null) {
            session.setAttribute("location", "/frontend/productorder/submitOrder");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("frontend/member/loginMember");
        }

        ProductOrder productOrder = createProductOrder(productByrName, productByrPhone, productByrEmail,
                productRcvName, productRcvPhone, productAddr,
                productTakeMethod, productPayMethod, productAllPrice,
                coupNo, myData);

        String result = productOrderSvc.addOneProductOrderSuccess(productOrder);
        cartSvc.deleteBymemNo(myData.getMemNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    private ProductOrder createProductOrder(String productByrName, String productByrPhone, String productByrEmail,
                                            String productRcvName, String productRcvPhone, String productAddr,
                                            Byte productTakeMethod, Byte productPayMethod, BigDecimal productAllPrice,
                                            Integer coupNo, Member myData) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setMember(myData);
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

        Optional<MyCoupon> myCouponOptional = myCouponService.getOneMyCoupon(coupNo, myData.getMemNo());
        myCouponOptional.ifPresent(myCoupon -> {
            myCoupon.setCoupUsedStat((byte) 1);
            productOrder.setCoupon(myCoupon.getCoupon());
        });

        return productOrder;
    }
    @PostMapping("insertProductOrderSuccess")
    public ResponseEntity<String> insertProductOrderSuccess(@RequestParam("productByrName") java.lang.String productByrName,
                                             @RequestParam("productByrPhone") java.lang.String productByrPhone,
                                             @RequestParam("productByrEmail") java.lang.String productByrEmail,
                                             @RequestParam("productRcvName") java.lang.String productRcvName,
                                             @RequestParam("productRcvPhone") java.lang.String productRcvPhone,
                                             @RequestParam("productTakeMethod") Byte productTakeMethod,
                                             @RequestParam("productAddr") java.lang.String productAddr,
                                             @RequestParam("productPayMethod") Byte productPayMethod,
                                             @RequestParam("productAllPrice") BigDecimal productAllPrice,
                                             @RequestParam(value = "coupNo", required = false) Integer coupNo,
                                             HttpSession session) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        session.setAttribute("loginsuccess",myData);
        if (myData == null) {
            session.setAttribute("location", "/frontend/productorder/submitOrder");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("frontend/member/loginMember");
        }

        ProductOrder productOrder = createProductOrder(productByrName, productByrPhone, productByrEmail,
                productRcvName, productRcvPhone, productAddr,
                productTakeMethod, productPayMethod, productAllPrice,
                coupNo, myData);

        productOrderSvc.addOneOrderSuccess(productOrder);
        cartSvc.deleteBymemNo(myData.getMemNo());

        return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);}


    @PostMapping("insertOrder")
    public String insertOrder(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpSession session) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        if (myData == null) {
            return "redirect:/frontend/member/loginMember";
        }

        Integer memNo = myData.getMemNo();
        ProductOrder productOrder = createProductOrderFromCart(cartRedis, memNo);
        session.setAttribute("coupons", getValidCoupons(memNo));
        model.addAttribute("coupons", getValidCoupons(memNo));
        model.addAttribute("productOrder", productOrder);
        session.setAttribute("productOrder", productOrder);

        return "frontend/cart/CartToProductOrderDetail";
    }

    private ProductOrder createProductOrderFromCart(CartRedis cartRedis, Integer memNo) {
        ProductOrder productOrder = productOrderSvc.addOneProductOrder(cartRedis);
        productOrder.setMember(memberService.findByNo(memNo));
        productOrder.setProductOrdStat((byte) 40);
        productOrder.setProductStat((byte) 0);
        return productOrder;
    }

    private List<MyCoupon> getValidCoupons(Integer memNo) {
        return myCouponService.getAllMyCouponMem(memNo).stream()
                .filter(coupon -> coupon.getCoupUsedStat() != 1)
                .collect(Collectors.toList());
    }

    @PostMapping("/coupNoInstantly")
    @ResponseBody
    public ResponseEntity<String> updatePriceInstantly(@RequestParam("coupno.coupNo") String coupNo,
                                                       @RequestParam("productAllPrice") BigDecimal productAllPrice) {
        int couponNumber = (coupNo == null || coupNo.isEmpty()) ? DEFAULT_COUPON_NUMBER : Integer.parseInt(coupNo);
        Coupon coupon = couponService.getOneCoupon(couponNumber);

        if (coupon == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon not found");
        }

        BigDecimal productRealPrice = productAllPrice.multiply(coupon.getCoupDisc());
        BigDecimal productDisc = productAllPrice.subtract(productRealPrice);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("productDisc", productDisc.toString());
        jsonObject.addProperty("productRealPrice", productRealPrice.toString());

        return ResponseEntity.ok().body(jsonObject.toString());
    }

    @GetMapping("CartEnd")
    public String CartEnd(ModelMap model, HttpSession session) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        Integer memNo = myData.getMemNo();
        List<ProductOrder> list = productOrderSvc.findByMember(memNo);
        model.addAttribute("productorderListData", list);
        return "frontend/cart/CartEnd";
    }


    @PostMapping("loginPage")
    public String loginPage(ModelMap model, HttpSession session) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        return "frontend/product/visitProduct";
    }

    @PostMapping("MemberGetAll")
    public String getAll(@RequestParam("productOrdNo") Integer productOrdNo, @RequestParam("productNo") Integer productNo, ModelMap model) {
        ProductOrderDetail productOrderDetail = productOrderDetailService.findByproductOrdNoAndproductNo(productOrdNo, productNo);
        model.addAttribute("productOrderDetail", productOrderDetail);
        Product product = productService.getOneProduct(productNo);
        model.addAttribute("product", product);
        return "frontend/cart/ProductScorce";
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
}