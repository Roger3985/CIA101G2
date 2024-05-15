package com.iting.productorderdetail.controller.backend;

import com.iting.productorder.entity.ProductOrder;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Validated
@RequestMapping("/backend/productorderdetail")
public class ProductOrderDetailController {
    @Autowired
    ProductOrderDetailService productOrderDetailSvc;




//跳轉到查詢全部訂單明細listAllProductOrderDetail.html
    @GetMapping("selectProductOrderDetail")
    public String listAllProductOrderDetail(Model model) {
        return "backend/productorderdetail/listAllProductOrderDetail";
    }
//跳轉到該新增addProductOrderDetail.html頁面
    @GetMapping("addProductOrderDetail")
    public String addProductOrderDetail(ModelMap model) {
        ProductOrderDetail productOrderDetail = new ProductOrderDetail();
        model.addAttribute("productOrderDetail", productOrderDetail);
        return "backend/productorderdetail/addProductOrderDetail";
    }
//點擊訂單明細新增按鈕,成功新增跳轉到listAllProductOrderDetail.html
    @PostMapping("insertproductOrderDetail")
    public String insertproductOrderDetail(@Valid ProductOrderDetail productOrderDetail, BindingResult result, ModelMap model) {
//        result = removeFieldError(productOrderDetail, result, "upFiles");
//        if (result.hasErrors() ) {
//            return "backend/productorderdetail/addProductOrderDetail";
//        }
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        productOrderDetailSvc.addProductOrderDetail(productOrderDetail);
        List<ProductOrderDetail> list = productOrderDetailSvc.getAll();
        model.addAttribute("productorderdetailListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/backend/productorderdetail/listAllProductOrderDetail"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
    }

//從訂單編號查詢多筆訂單明細
//查詢成功後成功後轉交listOneProductOrderDetail.html
    @PostMapping("productorderdetail")
    public String getOne_For_Display(@RequestParam("productOrdNo") String productOrdNo, ModelMap model){

        List<ProductOrderDetail> productOrderDetail = productOrderDetailSvc.findByCompositeKey(Integer.valueOf(productOrdNo));

        model.addAttribute("productOrderDetail", productOrderDetail);
        return "backend/productorderdetail/productorderdetail";
}

//更新商品訂單明細頁面
//修改成功後轉交listOneProductOrderDetail.html
    @PostMapping("update")
    public String update(@Validated(Create.class)  ProductOrderDetail productOrderDetail, BindingResult result, ModelMap model,
                         @RequestParam("productOrdNo")String productOrdNo, @RequestParam("productNo")String productNo
    ){
        result = removeFieldError(productOrderDetail, result, "upFiles");
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始修改資料 *****************************************/
        // EmpService empSvc = new EmpService();
        if (result.hasErrors()) {
            return "backend/productorderdetail/updateProductOrderDetail";
        }
        productOrderDetailSvc.updateProductOrderDetail(productOrderDetail);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        productOrderDetail = productOrderDetailSvc.findByproductOrdNoAndproductNo(Integer.valueOf(productOrdNo),Integer.valueOf(productNo));
        model.addAttribute("productOrderDetail", productOrderDetail);
        return "backend/productorderdetail/listOneProductOrderDetail";
    }
    //查詢該筆訂單明細,查詢成功後跳轉到updateProductOrderDetail.html更新訂單明細
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("productOrdNo") String productOrdNo, @RequestParam("productNo") String productNo,ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        ProductOrderDetail productOrderDetail = productOrderDetailSvc.findByproductOrdNoAndproductNo(Integer.valueOf(productOrdNo),Integer.valueOf(productNo));
        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("productOrderDetail", productOrderDetail);
        return "backend/productorderdetail/updateProductOrderDetail";
    }


    public BindingResult removeFieldError(ProductOrderDetail productOrderDetail, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(productOrderDetail, "productOrderDetail");
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
        //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
        List<ProductOrderDetail> list = productOrderDetailSvc.getAll();
        model.addAttribute("productOrderDetailListData", list);     // for select_page.html 第97 109行用
        model.addAttribute("productOrderDetail", new ProductOrderDetail());  // for select_page.html 第133行用

        String message = strBuilder.toString();
        return new ModelAndView("backend/productorderdetail/updateProductOrderDetail", "errorMessage", "請修正以下錯誤:<br>"+message);
    }




    @ModelAttribute("productOrderDetailListData")
    protected List<ProductOrderDetail> referenceListData(Model model) {

        List<ProductOrderDetail> list = productOrderDetailSvc.getAll();
        return list;
    }}



