package com.ren.productreview.controller;

import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productreview.entity.ProductReview;
import com.ren.productreview.service.impl.ProductReviewServiceImpl;
import com.roger.member.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/productreview")
public class ProductReviewController {

    @Autowired
    private ProductReviewServiceImpl productReviewSvc;

    @Autowired
    private ProductServiceImpl productSvc;

    @Autowired
    private MemberServiceImpl memberSvc;

    @GetMapping("/selectProductReview")
    public String toSelect() {
        return "backend/productreview/selectProductReview";
    }

    @GetMapping("/listOneProductReview")
    public String getOneProductReview() {
        return "backend/productreview/listOneProductReview";
    }

    @GetMapping("/listAllProductReviews")
    public String getAllProductReviews() {
        return "backend/productreview/listAllProductReviews";
    }

    @GetMapping("/addProductReview")
    public String toAddProductReview(ModelMap model) {
        model.addAttribute("productReview", new ProductReview());
        model.addAttribute("productList", productSvc.getAll());
        model.addAttribute("memberList", memberSvc.findAll());
        return "backend/productreview/addProductReview";
    }

    @PostMapping("/addProductReview/add")
    public String addProductReview(@Valid ProductReview productReview,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("productReview", productReview);
            model.addAttribute("productList", productSvc.getAll());
            model.addAttribute("memberList", memberSvc.findAll());
            return "backend/productreview/addProductReview";
        }

        redirectAttributes.addAttribute("success", "新增成功!");
        return "redirect:/backend/productreview/listAllProductReviews";
    }

    @GetMapping("/updateProductReview/{productNo}/{memNo}")
    public String toUpdateProductReview(@PathVariable Integer productNo,
                                        @PathVariable Integer memNo,
                                        ModelMap model) {
        model.addAttribute("productReview", productReviewSvc.getOneProductReview(productNo, memNo));
        model.addAttribute("productList", productSvc.getAll());
        model.addAttribute("memberList", memberSvc.findAll());
        return "backend/productreview/updateProductReview";
    }

    @GetMapping("/updateProductReview")
    public String toUpdateProductReview(@ModelAttribute("productReviewList") List<ProductReview> list,
                                        ModelMap model) {
        model.addAttribute("productReview", list.get(0));
        model.addAttribute("productList", productSvc.getAll());
        model.addAttribute("memberList", memberSvc.findAll());
        return "backend/productreview/updateProductReview";
    }

    @PostMapping("/updateProductReview/update")
    public String updateProductReview(@Valid ProductReview productReview,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("productReview", productReview);
            model.addAttribute("productList", productSvc.getAll());
            return "backend/productreview/updateProductReview";
        }

        productReviewSvc.updateProductReview(productReview);
        redirectAttributes.addAttribute("success", "修改成功!");
        return "redirect:/backend/productreview/listAllProductReviews";
    }

    @ModelAttribute("productReviewList")
    public List<ProductReview> getProductReviewList() {
        return productReviewSvc.getAll();
    }

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param productReview 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(ProductReview productReview, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(productReview, "productReview");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }
}
