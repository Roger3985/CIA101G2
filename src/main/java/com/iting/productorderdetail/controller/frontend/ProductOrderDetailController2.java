package com.iting.productorderdetail.controller.frontend;

import com.iting.cart.entity.CartRedis;
import com.iting.cart.service.impl.CartServiceImpl;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
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
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Controller
@Validated
@RequestMapping("/frontend/productorderdetail")
public class ProductOrderDetailController2 {
    @Autowired
    ProductOrderDetailService productOrderDetailSvc;
    @Autowired
    CartServiceImpl cartService;
    @Autowired
    ProductPictureServiceImpl productPictureService;
    @PostMapping("productorderdetail")
    public String getOne_For_Display(@RequestParam("productOrdNo") Integer productOrdNo,
                                     ModelMap model,
                                     HttpSession session){

        List<ProductOrderDetail> productOrderDetail = productOrderDetailSvc.findByCompositeKey(productOrdNo);

        for (ProductOrderDetail orderDetail : productOrderDetail) {
            Integer productNo = orderDetail.getProduct().getProductNo();
            List<ProductPicture> productPictures = productPictureService.getByProductNo(productNo);
            if (productPictures != null && !productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                Integer firstproductNo = firstProductPicture.getProduct().getProductNo();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                session.setAttribute("productImage" + firstproductNo, base64Image);
                model.addAttribute("productImage" + firstproductNo, base64Image);
            }}
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
                                      @RequestParam(value = "productScore", required = false) String productScore,
                                      @RequestParam("productNo") Integer productNo,
                                      @RequestParam("productOrdNo") Integer productOrdNo,
                                      ModelMap model) {
        ProductOrderDetail productOrderDetail2 = productOrderDetailSvc.findByproductOrdNoAndproductNo(productOrdNo, productNo);
        productOrderDetail2.setProductComContent(productComContent);

        if (productScore != null && !productScore.isEmpty()) {
            try {
                int score = Integer.parseInt(productScore);
                productOrderDetail2.setProductScore(score);
            } catch (NumberFormatException e) {
                // 如果無法解析為整數，您可以採取適當的處理措施，這裡假設設置為空值
                productOrderDetail2.setProductScore(null);
            }
        } else {
            // 如果productScore為空，將其設置為空值
            productOrderDetail2.setProductScore(null);
        }

        productOrderDetailSvc.updateProductOrderDetail(productOrderDetail2);

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailSvc.findByCompositeKey(productOrdNo);
        model.addAttribute("productOrderDetails", productOrderDetailList);

        return "frontend/cart/ProductScorce";
    }



}
