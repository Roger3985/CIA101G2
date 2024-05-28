package com.iting.productorder.controller.backend;


import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import com.iting.cart.service.CartService;
import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.roger.member.entity.Member;
import com.roger.member.entity.uniqueAnnotation.Create;
import com.roger.member.service.MemberService;
import org.slf4j.LoggerFactory;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("productOrder")
@RequestMapping("/backend/productorder")
public class ProductOrderController2 {

    @Autowired
    ProductOrderService productOrderSvc;
    @Autowired
    CartService cartSve;
    @Autowired
    MemberService memberService;
    @Autowired
    CouponService couponService;

    @GetMapping("addProductOrder")
    public String addProductOrder(ModelMap model) {
        ProductOrder productOrder = new ProductOrder();
        model.addAttribute("productOrder", productOrder);
        model.addAttribute("memberList", memberService.findAll());
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

    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    productOrder.setProductOrdTime(timestamp);

    BigDecimal productAllPrice = productOrder.getProductAllPrice();
    BigDecimal coupDisc = couponService.getOneCoupon(Integer.valueOf(coupNo)).getCoupDisc();

    // 检查productAllPrice和coupDisc是否为空
    if (productAllPrice != null ) {
        productOrder.setProductRealPrice(productAllPrice.multiply(coupDisc));
        productOrder.setProductDisc(productAllPrice.subtract(productAllPrice.multiply(coupDisc)));
    } else {
        // 处理为空的情况，例如设为0或者其他默认值
        productOrder.setProductRealPrice(BigDecimal.ZERO);
        productOrder.setProductDisc(BigDecimal.ZERO); // 设置 productDisc 的默认值
    }

    // 余下代码不变
    productOrderSvc.addProductOrder(productOrder);
    List<ProductOrder> list = productOrderSvc.getAll();
    model.addAttribute("productorderListData", list);
    model.addAttribute("success", "- (新增成功)");
    return "redirect:/backend/productorder/selectProductOrder";
}


    @PostMapping("update")
    public String update(@Validated(Create.class) ProductOrder productOrder, BindingResult result, ModelMap model,
                         @RequestParam(name="coupon.coupNo") String coupNo ,@RequestParam(name = "member.memNo")
                         String memNo) {
        result = removeFieldError(productOrder, result, "upFiles");

        if (coupNo == null || coupNo.isEmpty()) {

            productOrder.getCoupon().setCoupNo(1);
        } else {

            productOrder.getCoupon().setCoupNo(Integer.parseInt(coupNo));
        }

        if (result.hasErrors()) {
            return "backend/productorder/update_productorder_input";
        }
        productOrder.setMemNo(Integer.valueOf(memNo));
        productOrderSvc.updateProductOrder(productOrder);

        productOrder = productOrderSvc.getOneProductOrder(Integer.valueOf(productOrder.getProductOrdNo()));
        model.addAttribute("productOrder", productOrder);
        return "backend/productorder/listOneProductOrder";

    }
    @GetMapping("updateProductOrder")
    public String getOne_For_Update( ModelMap model) {
        ProductOrder productOrder = new ProductOrder();
        model.addAttribute("productOrder", productOrder);
        model.addAttribute("memberList", memberService.findAll());
        return "backend/productorder/update_productOrder_input"; // 查詢完成後轉交update_emp_input.html
    }
    @PostMapping("updateProductOrder")
    public String getOne_For_Update(@RequestParam("productOrdNo") String productOrdNo, ModelMap model) {

        ProductOrder productOrder = productOrderSvc.getOneProductOrder(Integer.valueOf(productOrdNo));
        model.addAttribute("productOrder", productOrder);
        model.addAttribute("memberList", memberService.findAll());

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
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append("<br>");
        }

        List<ProductOrder> list = productOrderSvc.getAll();
        model.addAttribute("productOrderListData", list);
        model.addAttribute("productOrder", new ProductOrder());

        String message = strBuilder.toString();

        return new ModelAndView("backend/productorder/update_productOrder_input", "errorMessage", "請修正以下錯誤:<br>" + message);
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
@PostMapping("/getProductOrderInstantly")
@ResponseBody
public ResponseEntity<ProductOrder> getProductOrderInstantly(@RequestParam Integer productOrdNo) {
    // 根據商品編號查詢商品詳情
    ProductOrder productOrder = productOrderSvc.getOneProductOrder(productOrdNo);
    // 返回查詢結果
    return ResponseEntity.ok().body(productOrder);
}
    @PostMapping("/getcoupNoInstantly")
    @ResponseBody
    public ResponseEntity<ProductOrder> updatePriceInstantly(@RequestParam(required = false) Integer coupNo, @RequestParam Integer productOrdNo) {
        // 如果coupNo为空值，则设置为1
        if (coupNo == null) {
            coupNo = 1;
        }

        // 根据优惠券编号查询产品订单详情
        ProductOrder productOrder = productOrderSvc.getProductOrderByCoupon(coupNo, productOrdNo);

        // 返回查询结果
        return ResponseEntity.ok().body(productOrder);
    }


    @PostMapping("/coupNoInstantly")
    @ResponseBody
    public ResponseEntity<Map<String, BigDecimal>> coupNoInstantly(@RequestParam String coupNo, @RequestParam BigDecimal productAllPrice) {
        CouponService couponService = new CouponService();
        BigDecimal coupDisc = couponService.getOneCoupon(Integer.parseInt(coupNo)).getCoupDisc();
        BigDecimal productRealPrice = productAllPrice.multiply(coupDisc);

        Map<String, BigDecimal> responseData = new HashMap<>();

        responseData.put("productDisc", coupDisc);
        responseData.put("productRealPrice", productRealPrice);

        return ResponseEntity.ok().body(responseData);
    }





    @ModelAttribute("MemberDataList")
    protected List<Member> referenceListData(Model model) {

        List<Member> list = memberService.findAll();
        return list;
    }

    @PostMapping("getOne")
    public String getOne(@RequestParam("memNo") String memNo, ModelMap model) {

        List<ProductOrder> productorderListData = productOrderSvc.findByMember(Integer.valueOf(memNo));
        model.addAttribute("productorderListData", productorderListData);
        return "/backend/productorder/MemberProductOrder";
    }




}