package com.ren;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ren.product.dto.ProductDTO;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
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
    @GetMapping("/")
    public String toFrontendIndex(HttpServletRequest req,
                                  ModelMap model,
                                  HttpSession session) {
//        Cookie[] cookies = req.getCookies();
//        for (var cookie : cookies) {
//            System.out.println("cookieName: " + cookie.getName() + ", cookieValue: " + cookie.getValue());
//        }
        String location = "";

        if ((location = (String) session.getAttribute("location")) != null) {
            return "redirect:" + location;
        }

        Pageable pageable = PageRequest.of(0, 10);
        String sortType = "newest";
        Page<ProductDTO> productPage = productSvc.getVisitProductFromRedis(pageable, sortType);

        List<Boolean> newProductList = new ArrayList<>();
        for (ProductDTO prodDTO : productPage.getContent()) {
            Boolean newProduct = false;
            if (prodDTO.getProductOnShelfSet() != null && !prodDTO.getProductOnShelfSet().isEmpty()) {
                List<Timestamp> list = new ArrayList<>(prodDTO.getProductOnShelfSet());
                list.sort(Comparator.naturalOrder());

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime dateTime = list.get(0).toLocalDateTime();
                Long daysDifference = ChronoUnit.DAYS.between(dateTime, now);
                if (daysDifference < 7) {
                    newProduct = true;
                }
                newProductList.add(newProduct);
            }
        }
        model.addAttribute("newProductList", newProductList);
        model.addAttribute("productDTOList", productPage.getContent());

        return "/index";
    }

    @GetMapping("/frontend/searchTest")
    public String toSearch() {

        return "backend/searchTest";
    }

    @ResponseBody
    @GetMapping("/frontend/search")
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

    @GetMapping("/frontend/searchMore")
    public String searchMore(@RequestParam("searchQuery") String keyword,
                             @RequestParam(value = "productPage", defaultValue = "0") Integer productPage,
                             @RequestParam(value = "rentalPage", defaultValue = "0") Integer rentalPage,
                             @RequestParam(value = "size", defaultValue = "5") Integer size,
                             Model model) {
        // 根據關鍵字做全文搜尋
        Page<Product> productSearch = productSvc.searchProducts(keyword, productPage, size);
        Page<Rental> rentalSearch = rentalSvc.searchRentals(keyword, rentalPage, size);

        // 將搜尋結果加入model
        model.addAttribute("productResults", productSearch.getContent());
        model.addAttribute("rentalResults", rentalSearch.getContent());
        model.addAttribute("productPage", productSearch);
        model.addAttribute("rentalPage", rentalSearch);
        model.addAttribute("searchQuery", keyword);

        return "frontend/searchMore";
    }


}
