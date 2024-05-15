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


import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.roger.member.entity.uniqueAnnotation.Create;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Controller
@Validated
@RequestMapping("/frontend/cart")
public class CartController {
    @Autowired
    CartService cartSvc;


    @GetMapping("addcart")
    public String addcart(ModelMap model) {
        CartRedis cartRedis = new CartRedis();

        model.addAttribute("cartRedis", cartRedis);
        return "frontend/cart/addCart";
    }

//
@GetMapping("Cart")
public String Cart(HttpSession session, ModelMap model) {
    // 从会话中获取 memNo 的值
 
    Integer memNo = (Integer) session.getAttribute("memNo");

    // 如果 memNo 为空，则需要进行相应的处理，比如重定向到登录页面或者给出提示信息
    if (memNo == null) {
        // 处理 memNo 为空的情况，比如重定向到登录页面
        return "redirect:/login";
    }

    // 根据 memNo 获取购物车数据
    List<CartRedis> cartListData = cartSvc.findByCompositeKey(memNo);

    // 将购物车数据添加到模型中
    model.addAttribute("cartListData", cartListData);

    // 返回购物车页面
    return "frontend/cart/Cart";
}


//    @GetMapping("select_page")
//    public String select_page() {
//        return "frontend/cart/CartSelectPage";
//    }
////    @GetMapping("cartbymemno")
////    public String cartbymemno() {
////        return "frontend/cart/cartbymemno";
////    }

//    @PostMapping("/deletecartsuccess")
//    public String delete(@RequestParam(name="memNo") Integer memNo,
//                         @RequestParam(name="productNo") Integer productNo,
//                         ModelMap model) {
//
//        cartSvc.deleteBymemNoAndProductNo(memNo, productNo);
//        List<CartRedis> updatedCartList = cartSvc.findByCompositeKey(memNo);
//        model.addAttribute("cartListData", updatedCartList);
//        return "frontend/cart/Cart";
//    }

    @PostMapping("addcartsuccess")
    public String insert(@Validated(Create.class) CartRedis cartRedis, BindingResult result, @RequestParam(name="memNo") Integer memNo, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors());
            return "frontend/cart/addCart";
        } else {


                cartSvc.updateCart(cartRedis);
                List<CartRedis> cartListData = cartSvc.findByCompositeKey(memNo);
                model.addAttribute("cartListData", cartListData);
                return "frontend/cart/Cart";

        }
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




//        @PostMapping("/updateQuantity")
//        @ResponseBody
//        public String updateQuantity(@RequestParam("productNo") String productNo,
//                                     @RequestParam("memNo") String memNo,
//                                     @RequestParam("quantity") int quantity) {
//            // 这里可以编写更新商品数量的逻辑，例如更新数据库中的商品数量
//            // 你可以使用 productNo 和 memNo 来确定要更新的商品和会员
//            // quantity 是新的商品数量
//
//            // 这里只是简单地返回一个成功的消息，你应该根据实际情况返回相应的结果
//            return "Quantity updated successfully";
//
//    }

    @PostMapping("/deleteInstantly")
    public String deleteInstantly(@RequestParam("productNo") Integer productNo,
                                  @RequestParam("memNo") Integer memNo,
                                  HttpSession session,
                                  ModelMap model) {

        // 执行删除操作
        cartSvc.deleteBymemNoAndProductNo(memNo, productNo);
        // 获取更新后的购物车数据

        // 返回购物车页面
        session.setAttribute("memNo", memNo);
        return "redirect:/frondend/cart/Cart";
    }



}
