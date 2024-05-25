package com.ren.productcategory.controller;

import com.ren.productcategory.entity.ProductCategory;

import com.ren.productcategory.service.impl.ProductCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/productcategory")
public class ProductCategoryController {
    
    @Autowired
    private ProductCategoryServiceImpl productCategorySvc;

    @GetMapping("/selectProductCategory")
    public String getProductCategory() {
        return "backend/productcategory/selectProductCategory";
    }

    @GetMapping("/listOneProductCategory")
    public String getProductCategory(@RequestParam Integer productCatNo) {
        return "backend/productcategory/listOneProductCategory";
    }

    @GetMapping("/listAllProductCategories")
    public String getAllProductCategories() {
        return "backend/productcategory/listAllProductCategories";
    }

    @GetMapping("/addProductCategory")
    public String toAddProductCategory(ModelMap model) {
        model.addAttribute("productCategory", new ProductCategory());
        return "backend/productcategory/addProductCategory";
    }

    @GetMapping("/updateProductCategory/{productCatNo}")
    public String toUpdateProductCategory(@PathVariable Integer productCatNo,
                                        ModelMap model) {
        model.addAttribute("productCategory", productCategorySvc.getOneProductCategory(productCatNo));
        return "backend/productcategory/updateProductCategory";
    }

    @GetMapping("/updateProductCategory")
    public String toUpdateProductCategory(@ModelAttribute("productCategoryList") List<ProductCategory> list,
                                        ModelMap model) {
        model.addAttribute("productCategory", list.get(0));
        return "backend/productcategory/updateProductCategory";
    }

    @ModelAttribute("productCategoryList")
    public List<ProductCategory> getProductCategoryList() {
        return productCategorySvc.getAll();
    }

//    @DeleteMapping("/productCategories/{pCatNo}")
//    public void deleteProductCategory(@PathVariable Integer pCatNo) {
//        productCategorySvc.deleteProductCategory(pCatNo);
//    }

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param productCategory 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(ProductCategory productCategory, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(productCategory, "productCategory");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }
}
