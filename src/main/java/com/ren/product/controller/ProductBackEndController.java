package com.ren.product.controller;

import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import com.ren.productcategory.service.impl.ProductCategoryServiceImpl;
import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ren.util.Constants.FIRST;

@Controller
@RequestMapping("/backend/product")
public class ProductBackEndController {

    @Autowired
    private ProductServiceImpl productSvc;

    @Autowired
    private ProductCategoryServiceImpl productCategorySvc;

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
    public String getProduct(@RequestParam Integer productNo, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
        }
        Product product = productSvc.getOneProduct(productNo);
        model.addAttribute("", product);

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
     * @param result 格式錯誤時，會存放Entity設置的錯誤訊息
     * @param model 如果輸入錯誤，將錯誤訊息渲染到前端顯示
     * @return 如果有錯誤訊息則forward回新增頁面，如果成功則重導到全部清單列表
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
     * 從listAll點集修改時前往更新頁面
     *
     * @param model 因畫面要顯示為點擊的商品資料，執行查詢單項方法後放入，並放入商品類別Service放入
     * @param productNo 傳入商品編號於後續查詢
     * @return forward到修改頁面
     */
    @GetMapping("/listToUpdateProduct")
    public String toUpdateProduct(ModelMap model, @RequestParam Integer productNo) {
        model.addAttribute("product", productSvc.getOneProduct(productNo));
        System.out.println(productNo);
        System.out.println(productSvc.getOneProduct(productNo).getProductNo());
        if (model.getAttribute("productCategoryList") == null) {
            model.addAttribute("productCategoryList", productCategorySvc.getAll());
        }
        return "backend/product/updateProduct";
    }


    /**
     * 點選側邊欄連結前往更新頁面
     *
     * @param list 訪問這個控制器時用於渲染的商品清單，傳入方法內用於將列表的第一項商品渲染到前端
     * @param model 將原本訪問網頁的商品清單第一項商品與商品類別清單加入ModelMap，渲染前端
     * @return forward到更新頁面
     */
    @GetMapping("/updateProduct")
    public String toUpdateProduct(@ModelAttribute("productList") List<Product> list, ModelMap model) {
        model.addAttribute("productCategoryList", productCategorySvc.getAll());
        model.addAttribute("product", list.get(FIRST));
        return "backend/product/updateProduct";
    }

    /**
     *
     * @param product
     * @param result
     * @param model
     * @return
     */
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
    @PostMapping("/changeProduct")
    public String changeProductStat(@RequestParam Integer productNo) {
        productSvc.onShelf(productNo);
        return "backend/product/listAllProducts";
    }

    @DeleteMapping("/products/{productNo}")
    public void deleteProduct(@PathVariable Integer productNo) {
        productSvc.deleteProduct(productNo);
    }

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
     * @return 以ResponseEntity返回到前端顯示
     */
    @PostMapping("/getProductInstantly")
    @ResponseBody
    public ResponseEntity<Product> getProductInstantly(@RequestParam Integer productNo) {
        // 根據商品編號查詢商品詳情
        Product product = productSvc.getOneProduct(productNo);
        // 返回查詢結果
        System.out.println("即時更新成功");
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("listTop10Products")
    public String top10Products(Model model) {
        List<Product> list = productSvc.getTopPopular();
        model.addAttribute("productList", list);

        return "backend/product/listTop10Products";
    }

    // 首頁更新營業額等Daily資料
    // 1.每日營業額
    // 2.賣出數量
    // 3.最熱賣商品


    // 更新

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

    private BindingResult removeAllErrors(Product product, BindingResult result) {

        List<String> list = new ArrayList<>();

        list.add("productName");
        list.add("productInfo");
        list.add("productSize");
        list.add("productColor");
        list.add("productPrice");
        list.add("productStat");
        list.add("productSalQty");
        list.add("productComPeople");
        list.add("productComScore");
        list.add("productOnShelf");
        list.add("productOffShelf");

        // 過濾這個Entity所有的錯誤訊息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(list.contains(fieldname)))
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
