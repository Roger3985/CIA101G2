package com.ren.product.controller;

import com.Entity.Product;
import com.Entity.ProductCategory;
import com.ren.product.service.ProductServiceImpl;
import com.ren.productcategory.service.ProductCategoryServiceImpl;
import com.ren.productpicture.service.ProductPictureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.ren.util.Constants.FIRST;

@Controller
@RequestMapping("/backend/product")
public class ProductBackEndController {

    @Autowired
    private ProductServiceImpl productSvc;

    @Autowired
    private ProductCategoryServiceImpl productCategorySvc;

    @Autowired
    private ProductPictureServiceImpl productPictureSvc;

//    @Autowired
//    private ProductOrderServiceImpl productOrderSvc;

    /**
     * 前往商品管理頁面
     *
     * @return forward to selectProduct.html
     */
    @GetMapping("/selectProduct")
    public String toSelect() {
        return "backend/product/selectProduct";
    }

    @GetMapping("/listOneProduct")
    public String getProduct(@PathVariable Integer productNo, HttpSession session) {
        return "backend/product/listOneProduct";
    }

    /**
     * 前往商品列表
     *
     * @return forward to listAllProducts.html
     */
    @GetMapping("/listAllProducts")
    public String getAllProducts() {
        return "backend/product/listAllProducts";
    }

    /**
     * 前往增加商品頁面
     *
     * @param model add null Product and productCategoryList to model
     * @return forward to addProduct.html
     */
    @GetMapping("/addProduct")
    public String toAddProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("productCategoryList", productCategorySvc.getAll());
        return "backend/product/addProduct";
    }

    /**
     * 將前端輸入資料新增置資料庫
     *
     * @param product 如果輸入格式錯誤時，將返回前端輸入值，不讓使用者再次輸入相同內容
     * @param result
     * @param model
     * @return
     */
    @PostMapping("addProduct/add")
    public String addProduct(@Valid Product product, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("productCategoryList", productCategorySvc.getAll());
            model.addAttribute("errors", result.getAllErrors());
            return"backend/product/addProduct";
        } else {
            System.out.println("新增成功");
        }
        productSvc.addProduct(product);
        return "redirect:/backend/product/listAllProducts";
    }

    /**
     * 從listAll前往更新頁面
     *
     * @param model
     * @param productNo
     * @return
     */
    @GetMapping("/updateProduct/{productNo}")
    public String toUpdateProduct(ModelMap model, @PathVariable Integer productNo) {

        model.addAttribute("product", productSvc.getOneProduct(productNo));
        if (model.getAttribute("productCategoryList") == null) {
            model.addAttribute("productCategoryList", productCategorySvc.getAll());
        }
        return "backend/product/updateProduct";
    }

    /**
     * 點選側邊欄連結前往更新頁面
     *
     * @param model
     * @return
     */
    @GetMapping("/updateProduct")
    public String toUpdateProduct(ModelMap model) {
        List<Product> list = productSvc.getAll();
        model.addAttribute("productList", list);
        model.addAttribute("productCategoryList", productCategorySvc.getAll());
        model.addAttribute("product", list.get(FIRST));
        return "backend/product/updateProduct";
    }

    // 處理修改資料
    @PostMapping("updateProduct/update")
    public String updateProduct(@Valid Product product, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("productCategoryList", productCategorySvc.getAll());
            return "backend/product/updateProduct";
        } else {
            System.out.println("新增成功");
        }
        productSvc.updateProduct(product);
        return "redirect:/backend/product/listAllProducts";
    }

    // 商品上下架功能
    @PostMapping()
    public String changeProductStat() {

        return "";
    }

//    @DeleteMapping("/products/{pNo}")
//    public void deleteProduct(@PathVariable Integer pNo) {
//        productSvc.deleteProduct(pNo);
//    }

    /**
     * 提供所有商品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
     * 該屬性是一個包含所有會員的列表，由 productSvc.findAll() 方法獲取。
     *
     * @return 包含所有會員的列表。
     */
    @ModelAttribute("productList")
    protected List<Product> getAllData() {
        List<Product> list =productSvc.getAll();
        return list;
    }

    /**
     * 選取下拉式選單後即時更新商品資訊
     *
     * @param productNo 傳入主鍵搜尋
     * @return 返回到前端顯示
     */
    @PostMapping("/getProductInstantly")
    @ResponseBody
    public ResponseEntity<Product> getProductInstantly(@RequestParam Integer productNo) {
        // 根據商品編號查詢商品詳情
        Product product = productSvc.getOneProduct(productNo);
        // 返回查詢結果
        return ResponseEntity.ok().body(product);
    }

    // 首頁更新營業額等Daily資料
    // 1.每日營業額
    // 2.賣出數量
    // 3.最熱賣商品


    // 更新
}
