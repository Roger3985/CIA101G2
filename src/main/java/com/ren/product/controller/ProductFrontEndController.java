package com.ren.product.controller;

import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import com.ren.productreview.service.impl.ProductReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ren.util.Constants.YES;

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
    @GetMapping("/visitProduct")
    public String toVisitProduct() {
        return "frontend/product/visitProduct";
    }

    @GetMapping("oneProduct")
    public String toOneProduct() {
        return "frontend/product/oneProduct";
    }

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
