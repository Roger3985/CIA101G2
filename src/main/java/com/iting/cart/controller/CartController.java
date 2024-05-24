//package com.iting.cart.controller;
//
//
//import com.iting.cart.service.CartService;
//import com.iting.productorder.entity.ProductOrder;
//import com.iting.productorder.service.ProductOrderService;
//import com.roger.member.entity.unique.Create;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BeanPropertyBindingResult;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import javax.validation.Valid;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//@Controller
//@Validated
//@RequestMapping("/cart")
//public class CartController {
//    @Autowired
//    CartService cartSvc;
//
//
//@GetMapping("addcart")
//public String addcart(ModelMap model) {
//    Cart cart = new Cart();
//    model.addAttribute("cart", cart);
//    return "frontend/cart/addCart";
//}
//
//    @GetMapping("Cart")
//    public String Cart() {
//        return "frontend/cart/Cart";
//    }
//    @GetMapping("select_page")
//    public String select_page() {
//        return "frontend/cart/CartSelectPage";
//    }
////    @GetMapping("cartbymemno")
////    public String cartbymemno() {
////        return "frontend/cart/cartbymemno";
////    }
//@PostMapping("deletecartsuccess")
//public String delete(@RequestParam(name="compositeKey.memNo") Integer memNo,
//                     @RequestParam(name="compositeKey.productNo") Integer productNo,
//                     ModelMap model) {
//    // 删除购物车中的商品
//    cartSvc.deleteBymemNoAndProductNo( productNo,memNo);
//
//    // 获取更新后的购物车列表
//    List<Cart> updatedCartList = cartSvc.findByCompositeKey(memNo);
//    model.addAttribute("cartListData", updatedCartList);
//    return "frontend/cart/Cart";
//}
//
//    @PostMapping("addcartsuccess")
//    public String insert(@Validated(Create.class) Cart cart, BindingResult result, @RequestParam(name="compositeKey.memNo") String memNo, ModelMap model) {
//
//        if (result.hasErrors()) {
//            model.addAttribute("errors",result.getFieldErrors());
//
//            return "frontend/cart/addcart";
//        } else {
//            cartSvc.updateCart(cart);
//            List<Cart> updatedCartList = cartSvc.findByCompositeKey(Integer.valueOf(memNo));
//            model.addAttribute("cartListData", updatedCartList);
//            return "redirect:/cart/Cart";
//        }
//    }
//
//    public BindingResult removeFieldError(Cart cart, BindingResult result, String removedFieldname) {
//        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
//                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
//                .collect(Collectors.toList());
//        result = new BeanPropertyBindingResult(cart, "cart");
//        for (FieldError fieldError : errorsListToKeep) {
//            result.addError(fieldError);
//        }
//        return result;
//    }
//    @ExceptionHandler(value = { ConstraintViolationException.class })
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
//        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//        StringBuilder strBuilder = new StringBuilder();
//        for (ConstraintViolation<?> violation : violations ) {
//            strBuilder.append(violation.getMessage() + "<br>");
//        }
//        String message = strBuilder.toString();
//        return new ModelAndView("frontend/cart/addcart", "errorMessage", "請修正以下錯誤:<br>"+message);
//    }
//
//        @PostMapping("/CartfromMemno")
//        public String Cart (@ModelAttribute("cartListData") List < Cart > cartListData) {
//            if (cartListData == null || cartListData.isEmpty()) {
//                // 处理空购物车列表的情况，你可以将其重定向到其他页面或者给出错误提示
//                return "redirect:/emptyCart"; // 重定向到空购物车页面
//            } else {
//                return "frontend/cart/Cart"; // 显示购物车页面
//            }
//        }
//
//        @GetMapping("/emptyCart")
//        public String showEmptyCartPage () {
//            return "frontend/cart/EmptyCart"; // 显示空购物车页面
//        }
//
//        @ModelAttribute("cartListData")
//        protected List<Cart> referenceListData_Mbr (Model model, @RequestParam(name = "memNo", required = false) String
//        memNo){
//            if (memNo == null || memNo.isEmpty()) {
//                return Collections.emptyList();
//            } else {
//                model.addAttribute("Cart", new Cart());
//                List<Cart> cart = cartSvc.findByCompositeKey(Integer.valueOf(memNo));
//                return cart;
//            }
//        }
//
//
//    }
package com.iting.cart.controller;


import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;

import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.entity.uniqueAnnotation.Create;
import oracle.sql.BLOB;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Controller
@Validated
@RequestMapping("/frontend")
public class CartController {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    CartService cartSvc;
    @Autowired
    CouponService couponService;
    @Autowired
    ProductPictureServiceImpl productPictureService;
    @Autowired
    ProductOrderService productOrderService;


    @GetMapping("/cart/addcart")
    public String addcart(ModelMap model) {
        CartRedis cartRedis = new CartRedis();

        model.addAttribute("cartRedis", cartRedis);
        return "frontend/cart/addCart";
    }




    @PostMapping("/cart/ProductOrderSuccess")
    public String ProductOrderSuccess(HttpSession session,Model model) {
//        Member myData = (Member) session.getAttribute("loginsuccess");
//        Integer memNo = myData.getMemNo();
//        List<ProductOrder> list = productOrderService.findByMember(memNo);
//        model.addAttribute("productorderListData", list);
        return "frontend/cart/ProductOrderPaySuccess";

    }
    @GetMapping("/cart/ProductOrderSuccessful")
    public String ProductOrderSuccessful(HttpSession session,Model model) {
        Member myData = (Member) session.getAttribute("loginsuccess");
        Integer memNo = myData.getMemNo();
        List<ProductOrder> list = productOrderService.findByMember(memNo);
        model.addAttribute("productorderListData", list);
        return "frontend/cart/ProductOrderSuccess";

    }


    //
//    @GetMapping("/cart/addcartsuccess")
//    public String insert(@Validated(Create.class) CartRedis cartRedis,HttpSession session, Model model) {
//        // 从会话中获取 memNo 的值
//        Member member = (Member) session.getAttribute("user");
//        if (member == null) {
//            session.setAttribute("location", "/frontend/cart/addcartsuccess");
//            return "redirect:/login";
//        }
//        Integer memNo = member.getMemNo();
//        cartSvc.updateCart(cartRedis);
//        List<CartRedis> cartListData = cartSvc.findByCompositeKey(memNo);
//        model.addAttribute("cartListData", cartListData);
//        return getCart(memNo, model, "/frontend/cart/Cart");
//    }
//    private String getCart(Integer memNo, Model model, String viewName) {
//        List<CartRedis> cartListData = cartSvc.findByCompositeKey(memNo);
//
////         檢查購物車是否為空
//        if (cartListData.isEmpty()) {
//            model.addAttribute("cartListData", true);
//        }
//        // Calculate the total
//        int total = 0;
//        for (CartRedis item : cartRedisList) {
//            total += item.getSubtotalAmount();
//        }
//        int shippingCost = total > 500 ? 0 : 100;

    // Add total to the model
//        model.addAttribute("total", total);
//        model.addAttribute("shippingCost", shippingCost);
//        model.addAttribute("cartListData", cartListData);
//        model.addAttribute("memNo", memNo);
//        return viewName;
//    }
    //加入商品至購物車
    @GetMapping("/cart/addcartsuccess")
    public String insert(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpSession session) {

        Integer memNo;
        Member member = new Member();
        session.setAttribute("member", member);
        if (session.getAttribute("loginsuccess") == null) {
            String sessionId = session.getId();
            memNo = Math.abs(sessionId.hashCode());
            session.setAttribute("memNo", memNo);

        } else {
            member = (Member) session.getAttribute("loginsuccess"); // 强制转换为Member类型
            memNo = member.getMemNo();
        }

        // 将memNo设置在cartRedis中
        cartRedis.setMemNo(memNo); // 将memNo强制转换为Integer类型
        List<CartRedis> cartListData = cartSvc.findByCompositeKey((Integer) memNo); // 将memNo强制转换为Integer类型


        for (CartRedis cartItem : cartListData) {
            Integer cartProductNo = cartItem.getProductNo();
            List<ProductPicture> productPictures = productPictureService.getByProductNo(cartProductNo);
            if (productPictures != null && !productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                Integer productNo = firstProductPicture.getProduct().getProductNo();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                session.setAttribute("productImage" + productNo, base64Image);
                model.addAttribute("productImage" + productNo, base64Image);
            }
        }


        model.addAttribute("cartListData", cartListData);

        return "frontend/cart/Cart";
    }


//    public BindingResult removeFieldError(CartRedis cart, BindingResult result, String removedFieldname) {
//        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
//                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
//                .collect(Collectors.toList());
//        result = new BeanPropertyBindingResult(cart, "cart");
//        for (FieldError fieldError : errorsListToKeep) {
//            result.addError(fieldError);
//        }
//        return result;
//    }
//    @ExceptionHandler(value = { ConstraintViolationException.class })
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
//        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//        StringBuilder strBuilder = new StringBuilder();
//        for (ConstraintViolation<?> violation : violations ) {
//            strBuilder.append(violation.getMessage() + "<br>");
//        }
//        String message = strBuilder.toString();
//        return new ModelAndView("frontend/cart/addcart", "errorMessage", "請修正以下錯誤:<br>"+message);
//    }
////
//    @PostMapping("/CartfromMemno")
//    public String Cart (@ModelAttribute("cartListData") List < CartRedis > cartListData) {
//        if (cartListData == null || cartListData.isEmpty()) {
//            // 处理空购物车列表的情况，你可以将其重定向到其他页面或者给出错误提示
//            return "redirect:/emptyCart"; // 重定向到空购物车页面
//        } else {
//            return "frontend/cart/Cart"; // 显示购物车页面
//        }
//    }
//
//    @GetMapping("/emptyCart")
//    public String showEmptyCartPage () {
//        return "frontend/cart/EmptyCart"; // 显示空购物车页面
//    }
//
////        @ModelAttribute("cartListData")
////        protected List<CartRedis> referenceListData_Mbr (Model model, @RequestParam(name = "memNo", required = false) String
////        memNo){
////            if (memNo == null || memNo.isEmpty()) {
////                return Collections.emptyList();
////            } else {
////                model.addAttribute("Cart", new CartRedis());
////                List<CartRedis> cart = cartSvc.findByCompositeKey(Integer.valueOf(memNo));
////                return cart;
////            }
////        }
//
//


    @PostMapping("/cart/coupNoInstantly")
    @ResponseBody
    public Map<String, String> coupNoInstantly(@RequestParam("coupNo") String coupNo,
                                               @RequestParam("productAllPrice") String productAllPrice
    ) {
        Map<String, String> response = new HashMap<>();

        Coupon coupon = couponService.getOneCoupon(Integer.valueOf(coupNo));

        BigDecimal totalPrice = new BigDecimal(productAllPrice);
        BigDecimal productRealPrice = coupon.getCoupDisc().multiply(totalPrice);
        String realPriceStr = productRealPrice.toString();

        BigDecimal productDisc = totalPrice.subtract(productRealPrice);
        String productDiscStr = productDisc.toString();

        response.put("productRealPrice", realPriceStr);
        response.put("productDisc", productDiscStr);

        return response;
    }


    @PostMapping("/cart/deleteInstantly")
    @ResponseBody
    public List<CartRedis> deleteInstantly(@RequestParam("productNo") Integer productNo,
                                           @RequestParam("memNo") Integer memNo,
                                           HttpSession session) {
        cartSvc.deleteBymemNoAndProductNo(memNo, productNo);
        List<CartRedis> cartRedisList = cartSvc.findByCompositeKey(memNo);
        session.setAttribute("memNo", memNo);
        System.out.println("即时更新成功");
        return cartRedisList;
    }

    @PostMapping("/cart/updateBackendQuantity")
    @ResponseBody
    public List<CartRedis> updateBackendQuantity(@RequestParam("productNo") Integer productNo,
                                                 @RequestParam("memNo") Integer memNo,
                                                 @RequestParam("productBuyQty") Integer productBuyQty,
                                                 HttpSession session) {
        cartSvc.updateCart(productNo, memNo, productBuyQty);
        session.setAttribute("memNo", memNo);
        List<CartRedis> cartRedisList = cartSvc.findByCompositeKey(memNo);
        System.out.println("即时更新成功");
        return cartRedisList;
    }


    @PostMapping("/cart/addproducttocart")
    @ResponseBody
    public Map<String, String> addProductToCart(@RequestParam("productNo") String productNo,
                                                @RequestParam("productBuyQty") String productBuyQty,
                                                HttpSession session,
                                                Model model) {
        Map<String, String> response = new HashMap<>();
        Integer memNo;
        Member member = new Member();
        session.setAttribute("member", member);
        if (session.getAttribute("loginsuccess") == null) {
            String sessionId = session.getId();
            memNo = Math.abs(sessionId.hashCode());
            session.setAttribute("memNo", memNo);
        } else {
            member = (Member) session.getAttribute("loginsuccess");
            memNo = member.getMemNo();
        }
        try {
            if (productNo == null || productBuyQty == null) {
                throw new IllegalArgumentException("Product number and quantity must not be null.");
            }
            CartRedis cartRedis = new CartRedis();
            cartRedis.setMemNo(memNo);
            cartRedis.setProductNo(Integer.valueOf(productNo));
            cartRedis.setProductBuyQty(Integer.valueOf(productBuyQty));
            cartSvc.updateCart(cartRedis);

            List<ProductPicture> productPictures = productPictureService.getByProductNo(Integer.valueOf(productNo));
            if (productPictures != null && !productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                if (session.getAttribute("productImage" + productNo) == null) {
                    session.setAttribute("productImage" + productNo, base64Image);
                }
            }

            List<CartRedis> cartListData = cartSvc.findByCompositeKey(memNo);
            model.addAttribute("cartListData", cartListData);
            session.setAttribute("cartListData", cartListData);
            response.put("message", "Success");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/cart/minicart")
    @ResponseBody
    public Map<String, Object> minicart(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            // 如果用户未登录，则返回错误信息或者空购物车信息
            response.put("error", "用户未登录");
            return response;
        }

        Integer memNo = member.getMemNo();
        List<CartRedis> cartRedisList = cartSvc.findByCompositeKey(memNo);

        List<Map<String, Object>> cartItems = new ArrayList<>();
        for (CartRedis cart : cartRedisList) {
            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("productName", cart.getProductName());
            cartItem.put("productPrice", cart.getProductPrice());
            cartItem.put("productBuyQty", cart.getProductBuyQty());

            List<ProductPicture> productPictures = productPictureService.getByProductNo(Integer.valueOf(cart.getProductNo()));
            if (!productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                cartItem.put("firstPic", base64Image);
            }

            cartItems.add(cartItem);
        }

        response.put("cartItems", cartItems);
        return response;
    }
}
