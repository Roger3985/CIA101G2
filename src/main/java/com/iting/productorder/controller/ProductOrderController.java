package com.iting.productorder.controller;


import com.chihyun.coupon.entity.Coupon;
import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.CartService;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.roger.member.entity.Member;
import com.roger.member.entity.uniqueAnnotation.Create;
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
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("productOrder")
@RequestMapping("/backend/productorder")
public class ProductOrderController {

    @Autowired
    ProductOrderService productOrderSvc;
    @Autowired
    CartService cartSve;

    @GetMapping("addProductOrder")
    public String addProductOrder(ModelMap model) {
        ProductOrder productOrder = new ProductOrder();
        model.addAttribute("productOrder", productOrder);
        return "backend/productorder/addProductOrder";
    }

    @GetMapping("addProductOrder1")
    public String addProductOrder1(ModelMap model) {
        ProductOrder productOrder = new ProductOrder();
        model.addAttribute("productOrderDetail", productOrder.getProductOrderDetails());
        return "frontend/cart/CartToProductOrderDetail";
    }

    @GetMapping("selectProductOrder2")
    public String select_page(Model model) {
        return "backend/productorder/select_page";
    }
//
//    @PostMapping("insert1")
//    public String insert(@Validated(Create.class) ProductOrder productOrder, BindingResult result, ModelMap model) {
//
//        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//        productOrderSvc.addProductOrder(productOrder);
//        List<ProductOrder> list = productOrderSvc.getAll();
//        model.addAttribute("productorderListData", list);
//        model.addAttribute("success", "- (新增成功)");
//        return "frontend/productorderdetail/CartToProductOrder"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
//    }
    @PostMapping("insert")
    public String insert(@Validated(Create.class) ProductOrder productOrder, BindingResult result, ModelMap model, @RequestParam(name="coupon.coupNo") String coupNo) {
        result = removeFieldError(productOrder, result, "upFiles");
        if (coupNo == null || coupNo.isEmpty()) {

            productOrder.getCoupon().setCoupNo(1);
        } else {

            productOrder.getCoupon().setCoupNo(Integer.parseInt(coupNo));
        }

        if (result.hasErrors()) {

            return "backend/productorder/addProductOrder";
        }

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        productOrderSvc.addProductOrder(productOrder);
        List<ProductOrder> list = productOrderSvc.getAll();
        model.addAttribute("productorderListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/backend/productorder/listAllProductOrder";
    }
    @PostMapping("update")
    public String update(@Validated(Create.class) ProductOrder productOrder, BindingResult result, ModelMap model,
                         @RequestParam(name="coupon.coupNo") String coupNo) {
        result = removeFieldError(productOrder, result, "upFiles");

        if (coupNo == null || coupNo.isEmpty()) {

            productOrder.getCoupon().setCoupNo(1);
        } else {

            productOrder.getCoupon().setCoupNo(Integer.parseInt(coupNo));
        }

        if (result.hasErrors()) {
            return "backend/productorder/update_productorder_input";
        }

        productOrderSvc.updateProductOrder(productOrder);
        model.addAttribute("success", "- (修改成功)");
        productOrder = productOrderSvc.getOneProductOrder(Integer.valueOf(productOrder.getProductOrdNo()));
        model.addAttribute("productOrder", productOrder);
        return "backend/productorder/listOneProductOrder";
    }
    @PostMapping("updateProductOrder")
    public String getOne_For_Update(@RequestParam("productOrdNo") String productOrdNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/

        ProductOrder productOrder = productOrderSvc.getOneProductOrder(Integer.valueOf(productOrdNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("productOrder", productOrder);
        return "backend/productorder/update_productOrder_input"; // 查詢完成後轉交update_emp_input.html
    }
    @PostMapping("findByMember")
    public String findByMember(@RequestParam("memNo") String memNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        // EmpService empSvc = new EmpService();

        List<ProductOrder> productOrder = productOrderSvc.findByMember(Integer.valueOf(memNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("productOrder", productOrder);
        return "backend/productorder/update_productOrder_input"; // 查詢完成後轉交update_emp_input.html
    }

    @PostMapping("insertOrder")
    public String insertOrder(@Validated(Create.class) CartRedis cartRedis, BindingResult result, ModelMap model, HttpServletRequest request) {
        ProductOrder productOrder = productOrderSvc.addOneProductOrder(cartRedis);
        model.addAttribute("productOrder", productOrder);
        HttpSession session=request.getSession();
        session.setAttribute("productOrder", productOrder); // 將訂單存儲在會話中
        return "frontend/cart/CartToProductOrderDetail";
    }

    @PostMapping("insertproductOrdersuccess")
    public String insertproductOrdersuccess(@Valid ProductOrder productOrder, BindingResult result, ModelMap model) {
//        result = removeFieldError(productOrderDetail, result, "upFiles");
//        if (result.hasErrors() ) {
//            return "backend/productorderdetail/addProductOrderDetail";
//        }
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        productOrderSvc.addOneProductOrderSuccess(productOrder);
//        List<ProductOrderDetail> list = productOrderDetailSvc.getAll();
//        model.addAttribute("productorderdetailListData", list);
//        model.addAttribute("success", "- (新增成功)");
        return "redirect:/backend/productorder/selectProductOrder";
    }



    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(ProductOrder productOrder, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(productOrder, "productOrder");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage() + "<br>");
        }

        List<ProductOrder> list = productOrderSvc.getAll();
        model.addAttribute("productOrderListData", list);
        model.addAttribute("productOrder", new ProductOrder());

        String message = strBuilder.toString();

        // Return to the "addProductOrderDetail" page
        return new ModelAndView("backend/productorderdetail/addProductOrder", "errorMessage", "請修正以下錯誤:<br>"+message);
    }

    @GetMapping("/selectProductOrder")
    public String listAllProductOrder(Model model) {

        return "backend/productorder/listAllProductOrder";
    }


    @ModelAttribute("productorderListData") // for select_page.html 第135行用
    protected List<ProductOrder> referenceListData_Dept(Model model) {
        model.addAttribute("ProductOrder", new ProductOrder()); // for select_page.html 第133行用
        List<ProductOrder> list = productOrderSvc.getAll();
        return list;
    }
// 取出購物車商品資訊
//    @GetMapping("/getFromCart")         // 因為 scan 要用 string 所以乾脆不轉型了
//    public ResponseEntity<?> getFromCart(@RequestParam String memNo, ModelMap model) {
//
//        Map<String, Map<String, String>> map = cartSve.getFromCart(memNo);
//        System.out.println(map);
//        return ResponseEntity.status(HttpStatus.OK).body(map);
//
//    }

}