package com.ren.productreview.controller;

import com.ren.productreview.entity.ProductReview;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/productreview")
public class ProductReviewController {

    @GetMapping("/selectProductReview")
    public String toSelect() {
        return "backend/productreview/selectProductReview";
    }

    @GetMapping("/addProductReview")
    public String toAddProductReview() {

        return "backend/productreview/addProductReview";
    }

    @GetMapping("/listOneProductReview")
    public String getOneProductReview() {
        return "backend/productreview/listOneProductReview";
    }

    @GetMapping("/listAllProductReviews")
    public String getAllProductReviews() {
        return "backend/productreview/listAllProductReviews";
    }

    @GetMapping("/updateProductReview")
    public String toUpdateProductReview() {
        return "backend/productreview/updateProductReview";
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
