package com.ren.productcategory.controller;

import com.ren.productcategory.entity.ProductCategory;

import com.ren.productcategory.service.ProductCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backend/productCategory")
public class ProductCategoryController {
    
    @Autowired
    private ProductCategoryServiceImpl productCategorySvc;

    @GetMapping("/listOneProductCategory")
    public ProductCategory getProductCategory(@PathVariable Integer pCatNo) {
        return productCategorySvc.getOneProductCatagory(pCatNo);
    }

    @GetMapping("/listAllProductCategories")
    public List<ProductCategory> getAllProductCategories() {
        return productCategorySvc.getAll();
    }

    @PostMapping("/addProductCategory")
    public ProductCategory addProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategorySvc.addProductCategory(productCategory);
    }

    @PutMapping("/updateProductCategory")
    public ProductCategory updateProductCategory(@PathVariable Integer productCatNo, @RequestBody ProductCategory productCategory) {
        // Ensure the productNo in the path matches the productNo in the request body
        if (!productCatNo.equals(productCategory.getProductCatNo())) {
            throw new IllegalArgumentException("Path variable productNo must match the productNo in the request body");
        }
        return productCategorySvc.updateProductCategory(productCategory);
    }

//    @DeleteMapping("/productCategories/{pCatNo}")
//    public void deleteProductCategory(@PathVariable Integer pCatNo) {
//        productCategorySvc.deleteProductCategory(pCatNo);
//    }
}
