package com.ren;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/frontend")
public class FrontendIndexController {

    @Autowired
    ProductServiceImpl productSvc;

    @Autowired
    RentalServiceImpl rentalSvc;

    /**
     * 前往前台首頁
     *
     * @return forward to frontend index
     */
    @GetMapping("/index")
    public String toFrontendIndex(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        for (var cookie : cookies) {
            System.out.println("cookieName: " + cookie.getName() + ", cookieValue: " + cookie.getValue());
        }
        return "frontend/index";
    }

    @GetMapping("/searchTest")
    public String toSearch() {

        return "backend/searchTest";
    }

    @ResponseBody
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam("query") String keyword,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "5") Integer size) throws JsonProcessingException {
        Map<String, Object> searchResult = new HashMap<>();
        Page<Product> productSearch = productSvc.searchProducts(keyword, page, size);
        Page<Rental> rentalSearch = rentalSvc.searchRentals(keyword, page, size);

        // 將商品和租借品合併並限制總數不超過5個
        List<Object> combinedResults = Stream.concat(productSearch.getContent().stream(), rentalSearch.getContent().stream())
                .limit(5)
                .collect(Collectors.toList());

        searchResult.put("results", combinedResults);
        searchResult.put("hasMoreResults", productSearch.getNumberOfElements() + rentalSearch.getNumberOfElements() > 5);

        return searchResult;
    }

    @GetMapping("/searchMore")
    public String searchMore(@RequestParam("searchQuery") String keyword,
                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "5") Integer size,
                             Model model) {
        // 根據關鍵字做全文搜尋
        Page<Product> productSearch = productSvc.searchProducts(keyword, page, size);
        Page<Rental> rentalSearch = rentalSvc.searchRentals(keyword, page, size);

        // 將搜尋結果加入model
        model.addAttribute("productResults", productSearch.getContent());
        model.addAttribute("rentalResults", rentalSearch.getContent());
        model.addAttribute("productPage", productSearch);
        model.addAttribute("rentalPage", rentalSearch);
        model.addAttribute("searchQuery", keyword);

        return "frontend/searchMore";
    }

//    @GetMapping("/searchResult")
//    public String searchResult(@RequestParam("searchQuery") String keyword,
//                             @RequestParam(value = "page", defaultValue = "0") Integer page,
//                             @RequestParam(value = "size", defaultValue = "5") Integer size,
//                             Model model) {
//        // 根據關鍵字做全文搜尋
//        Page<Product> productSearch = productSvc.searchProducts(keyword, page, size);
//        Page<Rental> rentalSearch = rentalSvc.searchRentals(keyword, page, size);
//
//        // 將搜尋結果加入model
//        model.addAttribute("productResults", productSearch.getContent());
//        model.addAttribute("rentalResults", rentalSearch.getContent());
//        model.addAttribute("productPage", productSearch);
//        model.addAttribute("rentalPage", rentalSearch);
//        model.addAttribute("searchQuery", keyword);
//
//        return "frontend/searchMore";
//    }


}
