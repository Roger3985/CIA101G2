package com.iting.productorderdetail.controller.frontend;

import com.iting.productorder.entity.ProductOrder;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
import com.roger.member.entity.uniqueAnnotation.Create;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Controller
@Validated
@RequestMapping("/frontend/productorderdetail")
public class ProductOrderDetailController2 {
    @Autowired
    ProductOrderDetailService productOrderDetailSvc;
 @PostMapping("productorderdetail")
    public String getOne_For_Display(@RequestParam("productOrdNo") Integer productOrdNo, ModelMap model){

        List<ProductOrderDetail> productOrderDetail = productOrderDetailSvc.findByCompositeKey(productOrdNo);

        model.addAttribute("productOrderDetails", productOrderDetail);
        return "frontend/cart/ProductScorce";
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
    }
    @PostMapping("/senddetailInstantly")
    public String senddetailInstantly(@RequestParam("productComContent") String productComContent,
                                      @RequestParam("productScore") Integer productScore,
                                      @RequestParam("productNo") Integer productNo,
                                      @RequestParam("productOrdNo") Integer productOrdNo,
                                      ModelMap model) {
        ProductOrderDetail productOrderDetail2 = productOrderDetailSvc.findByproductOrdNoAndproductNo(productOrdNo, productNo);
        productOrderDetail2.setProductComContent(productComContent);
        productOrderDetail2.setProductScore(productScore);
        productOrderDetailSvc.updateProductOrderDetail(productOrderDetail2);

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailSvc.findByCompositeKey(productOrdNo);
        model.addAttribute("productOrderDetails", productOrderDetailList);

        return "frontend/cart/ProductScorce";
    }


}
