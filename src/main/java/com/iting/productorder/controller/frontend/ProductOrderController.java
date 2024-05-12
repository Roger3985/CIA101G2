package com.iting.productorder.controller.frontend;

import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.ren.product.entity.Product;
import com.roger.member.entity.uniqueAnnotation.Create;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("productOrder")
@RequestMapping("/frontend/productorder")

public class ProductOrderController {
    @Autowired
    ProductOrderService productOrderSvc;
    @Autowired
    CartService cartSve;
    @PostMapping("insertOrder")
    public String insertOrder(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpServletRequest request) {
        ProductOrder productOrder = productOrderSvc.addOneProductOrder(cartRedis);
        model.addAttribute("productOrder", productOrder);
        HttpSession session=request.getSession();
        session.setAttribute("productOrder", productOrder); // 將訂單存儲在會話中
        return "frontend/cart/CartToProductOrderDetail";
    }
}