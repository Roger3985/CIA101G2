package com.ren.product.controller;

import com.ren.product.dto.ProductDTO;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import com.ren.productreview.entity.ProductReview;
import com.ren.productreview.service.impl.ProductReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/frontend/product")
public class ProductFrontEndController {

    @Autowired
    private ProductServiceImpl productSvc;

    @Autowired
    private ProductPictureServiceImpl productPictureSvc;

    @Autowired
    private ProductReviewServiceImpl productReviewSvc;

    // 前往商品瀏覽頁面
    @GetMapping("/visitProduct-list")
    public String toVisitProductList(ModelMap model) {
        model.addAttribute("productDTOList", productSvc.getVisitProduct(productSvc.getAll()));
        String[] sizes = {"XS", "S", "M", "L", "XL", "2L"};
        model.addAttribute("sizes", sizes);

        return "frontend/product/visitProduct-list";
    }

//    @GetMapping("/visitProduct")
//    public String toVisitProduct(ModelMap model) {
//        model.addAttribute("productDTOList", productSvc.getVisitProduct(productSvc.getAll()));
//        String[] sizes = {"XS", "S", "M", "L", "XL", "2L"};
//        model.addAttribute("sizes", sizes);
//
//        return "frontend/product/visitProduct";
//    }

    @GetMapping("/visitProduct")
    public String toVisitProduct(ModelMap model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productPage = productSvc.getVisitProduct(productSvc.getAll(), pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("productDTOList", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        String[] sizes = {"XS", "S", "M", "L", "XL", "2L"};
        model.addAttribute("sizes", sizes);

        return "frontend/product/visitProduct";
    }

    @GetMapping("/filterProducts")
    public String filterProducts(@RequestParam Map<String, String> filters, ModelMap model) {
        Set<String> keys = filters.keySet();
        List<List<Product>> middleManipulation = new ArrayList<>();
        for (String key : keys) {
            List<Product> list = null;
            switch (key) {
                case "color":
                    list = productSvc.getProductsByColor(filters.get(key));
                    break;
                case "size":
                    list = productSvc.getProductsBySize(Integer.valueOf(filters.get(key)));
                    break;
                case "price":
                    String[] prices = filters.get(key).split("-");
                    BigDecimal minPrice = new BigDecimal(prices[0]);
                    BigDecimal maxPrice = new BigDecimal(prices[1]);
                    list = productSvc.getProductsByPrice(minPrice, maxPrice);
                    break;
            }
            middleManipulation.add(list);
        }

        List<Product> totalList = middleManipulation.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        model.addAttribute("productDTOList", productSvc.getVisitProduct(totalList));
        String[] sizes = {"XS", "S", "M", "L", "XL", "2L"};
        model.addAttribute("sizes", sizes);
        return "frontend/product/visitProduct :: product-fragment";
    }

    @GetMapping("/oneProduct")
    public String toOneProduct(@RequestParam("productNo") Integer productNo,
                               ModelMap model) {
        ProductDTO productDTO = productSvc.getOneProductDTO(productNo);
        List<ProductPicture> productPictures = productPictureSvc.getByProductNo(productNo);
        var picNoList = new ArrayList<Integer>();
        Integer totalPictures = 0;
        for (var productPicture : productPictures) {
            picNoList.add(productPicture.getProductPicNo());
            totalPictures++;
        }

//        model.addAttribute("product", product);
//        model.addAttribute("productReviewList", list);
//        model.addAttribute("totalReviews", totalReviews);
//        model.addAttribute("productScore", productScore);
        model.addAttribute("picNoList", picNoList);
        model.addAttribute("totalPictures", totalPictures);
        model.addAttribute("productDTO", productDTO);
        return "frontend/product/oneProduct";
    }

//    @GetMapping("/Product")
//    public String search(@RequestParam("productNo") Integer productNo,
//                         ModelMap model) {
//        Page<Product> products = productSvc.searchProducts(keyword, page, size);
//        model.addAttribute("products", products.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", products.getTotalPages());
//        return "frontend/product/oneProduct";
//    }

    @GetMapping
    public String recommendProducts() {

        return "";
    }


    // 加入購物車

    // 前往結帳畫面

    // 確認是否有無登入

    // 書籤的篩選功能

    // 複合查詢
    // 使用cookie追蹤瀏覽資訊
    @PostMapping()
    public void cookieAdd() {
//        Optional<Cookie> userCookie = Optional.ofNullable(((HttpServletRequest) req)
//                        .getCookies())
//                .flatMap(this::userCookie);
//        Cookie cookie = null;
//        // 確認使用者是否要自動登入
//        if (autoLogin == YES) {
//            String cookieName = administrator.getAdmNo().toString();
//            String cookieValue = administrator.getTitle().getTitleNo().toString();
//            cookie = new Cookie(cookieName, cookieValue);
//            cookie.setMaxAge(864000);
//            ((HttpServletResponse) res).addCookie(cookie);
//            System.out.println("我要Cookie");
//        }
    }

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param product 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(Product product, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(product, "product");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }
}