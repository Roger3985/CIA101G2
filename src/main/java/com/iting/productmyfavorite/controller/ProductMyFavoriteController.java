package com.iting.productmyfavorite.controller;

import com.iting.productmyfavorite.entity.ProductMyFavorite;
import com.iting.productmyfavorite.service.impl.ProductMyFavoriteImpl;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.roger.member.entity.Member;
import com.roger.member.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backend/productmyfavorite")
public class ProductMyFavoriteController {
    @Autowired  // 自動裝配
    private ProductMyFavoriteImpl productMyFAVService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private MemberServiceImpl memberService;
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }

    //顯示查詢頁面 (後台)
    @GetMapping("/selectProductMyFavorite")
    public String selectPage(ModelMap model) {
        ProductMyFavorite productMyFavorite = new ProductMyFavorite();
        model.addAttribute("productMyFavorite", productMyFavorite);
        return "/backend/productmyfavorite/listAllProductMyFAV";
    }

    //顯示全部最愛清單頁面 (後台)
    @GetMapping("/listAllProductMyFAV")
    public String listAllProductMyFAV() {
        return "/backend/productmyfavorite/listAllProductMyFAV";
    }

    //顯示新增頁面 (後台)
    @GetMapping("/addProductMyFAV")
    public String addProductMyFAV(ModelMap model) {
        ProductMyFavorite productMyFavorite = new ProductMyFavorite();
        model.addAttribute("productMyFavorite", productMyFavorite);
        return "/backend/productmyfavorite/addProductMyFAV";
    }


    //處理單筆查詢(依productNo)
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam(value = "productNo", required = false) String productNo, ModelMap model) {

        List<ProductMyFavorite> productMyFavorites = productMyFAVService.findByCompositeKey(Integer.valueOf(productNo));
        model.addAttribute("productMyFavorites", productMyFavorites);
        return "/backend/productmyfavorite/listOneProductMyFAV"; // 查詢完成後轉交
    }
    //處理單筆修改(依productNo)
    @PostMapping("getOne")
    public String getOne(@RequestParam("memNo") String memNo, ModelMap model) {

        List<ProductMyFavorite> productMyFavorites = productMyFAVService.findByMemNo(Integer.valueOf(memNo));
        model.addAttribute("productMyFavorites", productMyFavorites);
        return "/backend/productmyfavorite/listOneProductMyFAV";
    }
    //處理單筆修改(依productNo)
     @PostMapping("getOneUpdate")
    public String getOneUpdate(@RequestParam("productNo") String productNo, @RequestParam("memNo") String memNo, ModelMap model) {

        List<ProductMyFavorite> productMyFavorite = productMyFAVService.findByProductProductNoAndMemberMemNo(Integer.valueOf(productNo),Integer.valueOf(memNo));
        model.addAttribute("productMyFavorite", productMyFavorite);
        return "/backend/productmyfavorite/updateProductMyFAV";
    }

    // 萬用查詢
//   @GetMapping("/search")
//    public String search(@RequestParam(value = "productNo", required = false) Integer productNo,
//                         @RequestParam(value = "memNo", required = false) Integer memNo,
//                         @RequestParam(value = "productFavTime",required = false) Timestamp productFavTime,
//                         ModelMap modelMap) {
//
//        Map<String, Object> map = new HashMap<>();
//
//        if (productNo != null) {
//            map.put("productNo", productNo);
//        } else if (memNo != null) {
//            map.put("memNo", memNo);
//        } else if (productFavTime != null) {
//            map.put("productFavTime", productFavTime);
//        }
//
//        List<ProductMyFavorite> productMyFAVList = productMyFAVService.searchProductMyFAVs(map);
//        modelMap.addAttribute("productMyFAVList", productMyFAVList);
//        modelMap.addAttribute("search", "true");
//
//        return "/backend/productmyfavorite/selectProductMyFAV";
//    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return ProductMyFavorite所有資料庫
     */
    @ModelAttribute("productMyFAVList")
    protected List<ProductMyFavorite> referenceListData() {
        List<ProductMyFavorite> productMyFAVList = productMyFAVService.findAll();
        return productMyFAVList; //取得ProductMyFavorite列表
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return product所有資料庫
     */
    @ModelAttribute("productListData") //取出product資料庫
    protected List<Product> referenceListDataProduct() {
        List<Product> productList = productService.getAll();
        return productList; //取得Product列表
    }

    /**
     * 提供所有產品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
     * referenceMapData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return Member所有資料庫
     */
    @ModelAttribute("memberListData")
    protected List<Member> referenceListDataMember() {
        List<Member> memberList = memberService.findAll();
        return memberList;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與productNo & memNo 對應的ProductMyFavorite資料庫
     */
    @ModelAttribute("productMyFAVData")
    protected ProductMyFavorite referenceData(@RequestParam(value = "productNo", required = false) Integer productNo,
                                              @RequestParam(value = "memNo", required = false) Integer memNo) {

        if (productNo != null && memNo != null) {
            ProductMyFavorite list = productMyFAVService.findByProductNoAndMemNo(productNo,memNo);
            return list; //取得ProductMyFavorite列表
        }
        return null;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceMemNoData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與memNo 對應的ProductMyFavorite資料庫
     */
    @ModelAttribute("memNoData")
    protected List<ProductMyFavorite> referenceMemNoData(@RequestParam(value = "memNo", required = false) Integer memNo) {

        if (memNo != null) {
            List<ProductMyFavorite> list = productMyFAVService.findByMemNo(memNo);
            return list; //取得ProductMyFavorite列表
        }
        return null;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceMemNoData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與productNo 對應的ProductMyFavorite資料庫
     */
    @ModelAttribute("productNoData")
    protected List<ProductMyFavorite>  referenceProductNoData(@RequestParam(value = "productNo", required = false) Integer productNo) {

        if (productNo != null) {
            List<ProductMyFavorite> list = productMyFAVService.findByMemNo(productNo);
            return list;
        }
        return null;
    }
}
