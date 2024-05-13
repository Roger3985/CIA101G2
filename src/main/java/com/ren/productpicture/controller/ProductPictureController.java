package com.ren.productpicture.controller;

import com.ren.productcategory.entity.ProductCategory;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.ProductPictureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/productpicture")
public class ProductPictureController {
    
    @Autowired
    private ProductPictureServiceImpl productPictureSvc;

    @GetMapping("/selectProductPicture")
    public String toProductPicture() {
        return "/backend/productpicture/selectProductPicture";
    }

    @GetMapping("/listOneProductPicture")
    public String getProductPicture(@PathVariable Integer productPicNo) {
        return "/backend/productpicture/listOneProductPicture";
    }

    @GetMapping("/listAllProductCategories")
    public String getAllProductCategories() {
        return "/backend/productpicture/listAllProductPicture";
    }

    @PostMapping("/addProductPicture")
    public String addProductPicture(@RequestBody ProductPicture productPicture) {
        return "/backend/productpicture/addProductPicture";
    }

    @PutMapping("/updateProductPicture")
    public String updateProductPicture(@PathVariable Integer productCatNo, @RequestBody ProductPicture productPicture) {
        return "/backend/productpicture/updateProductPicture";
    }

//    @DeleteMapping("/productCategories/{productPicNo}")
//    public void deleteProductPicture(@PathVariable Integer productPicNo) {
//        productPictureSvc.deleteProductPicture(productPicNo);
//    }
    
    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param productPicture 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(ProductPicture productPicture, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(productPicture, "productPicture");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }
}
