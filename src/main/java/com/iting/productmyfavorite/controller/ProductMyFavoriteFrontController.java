package com.iting.productmyfavorite.controller;

import com.iting.cart.entity.CartRedis;
import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productmyfavorite.entity.ProductMyFavoriteRedis;
import com.iting.productmyfavorite.service.ProductMyFavoriteService;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.entity.uniqueAnnotation.Create;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/frontend/productmyfavorite")
public class ProductMyFavoriteFrontController {
    @Autowired
    ProductServiceImpl productServiceImpl;
@Autowired
    ProductMyFavoriteService productMyFavoriteService;
@Autowired
    ProductPictureServiceImpl productPictureService;
@Autowired
    ProductServiceImpl productService;
    @GetMapping("/myProductFAV")
    public String myProductFAV(ProductMyFavoriteRedis productMyFavoriteRedis,ModelMap model, HttpSession session) {

        Member member=new Member();

        member.setMemNo(3);
        session.setAttribute("member",member);
        member = (Member) session.getAttribute("member"); // 强制转换为Member类型




        productMyFavoriteRedis.setMemNo(member.getMemNo()); // 将memNo强制转换为Integer类型
        List<ProductMyFavoriteRedis> productMyFavoriteRedisList = productMyFavoriteService.findByKey(member.getMemNo());// 将memNo强制转换为Integer类型
        for (ProductMyFavoriteRedis oneproductMyFavorite : productMyFavoriteRedisList) {
            Integer myProductNo = oneproductMyFavorite.getProductNo();
            Product product=productServiceImpl.getOneProduct(myProductNo);
            session.setAttribute("productName"+myProductNo,product.getProductName() );
            session.setAttribute("productPrice"+myProductNo,product.getProductPrice() );
            List<ProductPicture> productPictures = productPictureService.getByProductNo(myProductNo);
            if (productPictures != null && !productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                Integer productNo=firstProductPicture.getProduct().getProductNo();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                session.setAttribute("productImage"+productNo, base64Image);
                model.addAttribute("productImage"+productNo, base64Image);
            }
        }


        model.addAttribute("productMyFavoriteRedisList", productMyFavoriteRedisList);

        return "/frontend/productmyfavorite/myProductFAV";
    }

    @PostMapping("/addProductTomyFAV")
    @ResponseBody
    public Map<String, String> addProductToCart(@RequestParam("productNo") String productNo,
                                                HttpSession session,
                                                Model model) {
        Map<String, String> response = new HashMap<>();
        Integer memNo;
        Member member = new Member();
        member.setMemNo(3);
        session.setAttribute("member", member);

        try {
            member = (Member) session.getAttribute("member");
            memNo = member.getMemNo();

            ProductMyFavoriteRedis productMyFavoriteRedis = new ProductMyFavoriteRedis();
            productMyFavoriteRedis.setMemNo(memNo);
            productMyFavoriteRedis.setProductNo(Integer.valueOf(productNo));
            productMyFavoriteService.addProductMyFavorite(productMyFavoriteRedis);

            List<ProductPicture> productPictures = productPictureService.getByProductNo(Integer.valueOf(productNo));
            if (productPictures != null && !productPictures.isEmpty()) {
                ProductPicture firstProductPicture = productPictures.get(0);
                byte[] firstPic = firstProductPicture.getProductPic();
                String base64Image = Base64.getEncoder().encodeToString(firstPic);
                if (session.getAttribute("productImage" + productNo) == null) {
                    session.setAttribute("productImage" + productNo, base64Image);
                }
            }

            List<ProductMyFavoriteRedis> productMyFavorites = productMyFavoriteService.findByKey(memNo);
            model.addAttribute("productMyFavorites", productMyFavorites);
            session.setAttribute("productMyFavorites", productMyFavorites);
            response.put("message", "Success");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
    @PostMapping("/deleteInstantly")
    @ResponseBody
    public List<ProductMyFavoriteRedis> deleteInstantly(@RequestParam("productNo") Integer productNo,
                                           @RequestParam("memNo") Integer memNo,
                                           HttpSession session) {
        productMyFavoriteService.delete(memNo, productNo);
        List<ProductMyFavoriteRedis> productMyFavoriteRedis = productMyFavoriteService.findByKey(memNo);
        session.setAttribute("memNo", memNo);
        System.out.println("即时更新成功");
        return productMyFavoriteRedis;
    }
}
