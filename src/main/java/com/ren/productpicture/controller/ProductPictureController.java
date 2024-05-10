package com.ren.productpicture.controller;

import com.Entity.ProductPicture;
import com.ren.productpicture.service.ProductPictureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
