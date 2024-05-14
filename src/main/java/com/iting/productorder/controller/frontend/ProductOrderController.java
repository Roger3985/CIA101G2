package com.iting.productorder.controller.frontend;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@SessionAttributes("productOrder")
@RequestMapping("/frontend/productorder")

public class ProductOrderController {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    ProductOrderService productOrderSvc;
    @Autowired
    CartService cartSve;
    @Autowired
    MemberService memberService;
    @Autowired
    ProductOrderDetailService productOrderDetailService;

    @PostMapping("insertOrder")
    public String insertOrder(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpSession session,@RequestParam("memNo") Integer memNo) {

        ProductOrder productOrder = productOrderSvc.addOneProductOrder(cartRedis);

        Member member=memberService.findByNo(memNo);
        productOrder.setMember(member);
        productOrder.setProductOrdStat(Byte.valueOf((byte)40));
        productOrder.setProductStat(Byte.valueOf((byte)0));
        model.addAttribute("productOrder", productOrder);
        session.setAttribute("productOrder", productOrder); // 將訂單存儲在會話中
        return "frontend/cart/CartToProductOrderDetail";
    }

    @PostMapping("insertProductOrderSuccess")
    public String insertProductOrderSuccess(@Valid ProductOrder productOrder,@RequestParam("memNo") Integer memNo, ModelMap model) {
//        result = removeFieldError(productOrder, result, "upFiles");
//        if (result.hasErrors() ) {
//            return "frontend/cart/CartToProductOrderDetail";
//        }
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        productOrderSvc.addOneProductOrderSuccess(productOrder);
        List<ProductOrder> list= productOrderSvc.findByMember(memNo);
        model.addAttribute("productorderListData", list);
//        model.addAttribute("success", "- (新增成功)");
        cartSve.deleteBymemNo(memNo);
        return "frontend/cart/CartEnd";
    }
    @PostMapping("MemberGetAll")
    public String getAll(@RequestParam("productOrdNo") Integer productOrdNo,@RequestParam("productNo") Integer productNo, ModelMap model) {
        ProductOrderDetail productOrderDetail= productOrderDetailService.findByproductOrdNoAndproductNo(productOrdNo,productNo);
        model.addAttribute("productOrderDetail", productOrderDetail);
        Product product= productService.getOneProduct(productNo);
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
}