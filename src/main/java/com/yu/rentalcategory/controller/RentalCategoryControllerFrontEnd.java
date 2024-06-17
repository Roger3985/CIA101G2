package com.yu.rentalcategory.controller;

import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalcategory.entity.RentalCategory;
import com.yu.rentalcategory.service.RentalCategoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend/rentalcategory") //對應資料夾路徑
public class RentalCategoryControllerFrontEnd {


    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;


    //顯示單一類商品查詢(依類別編號)
    @GetMapping("/showOneRentalCat")
    public String showOneRentalCat(@RequestParam(value = "rentalCatNo") Integer rentalCatNo,
                                   ModelMap model) {
        //建立返回數據的對象
        RentalCategory rentalCategory = rentalCategoryService.findByCatNo(rentalCatNo);
        List<RentalCategory> rentalCatList = rentalCategoryService.findAll();
        model.addAttribute("rentalCatList", rentalCatList);

        // 根據分類編號查詢相應的租賃資料
        List<Rental> rentalList = rentalService.findByRentalCategoryRentalCatNo(rentalCatNo);
        model.addAttribute("rentalList", rentalList);

        if (rentalCategory == null) {
            model.addAttribute("errorMessage", "errors");
            return "/frontend/rental/rentalShop";
        }

        model.addAttribute("rentalCategory", rentalCategory);
        return "/frontend/rentalcategory/showOneRentalCat";
    }


    //關鍵字查詢(依租借品的名稱 "模糊查詢")
    @PostMapping("/getQueryRentalName")
    public String getQueryRentalName(@RequestParam("rentalName") String rentalName, ModelMap model) {

        List<Rental> nameList = rentalService.getRentalName(rentalName);
        model.addAttribute("nameList", nameList);

        if (rentalName == null) {
            model.addAttribute("errors", "errors");
            return "/frontend/rental/select_page";
        }
        model.addAttribute("rentalName", rentalName);
        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
    }

    /**
     * Map中存储了请求参数的键值，键是参数的名称，值是参数的值数组
     *通过HttpServletRequest获取了请求的参数Map，并将其传递给searchRentals()方法执行查询。
     *查询结果存储在queryList中，然后将其添加到model中，以便在视图
     *
     */
    //處理複合查詢
    @PostMapping("search")
    public String search(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        //建立返回數據的對象
        List<RentalCategory> queryList = rentalCategoryService.searchRentalCats(map);
        model.addAttribute("queryList", queryList);
        return "frontend/rentalcategory/listAllRentalCategory"; //結果傳至listAllRental
    }


    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalCatNo對應的rental資料庫
     */
    @ModelAttribute("rentalCatData") // 以rentalNo搜索，取出對應的rental資料庫
    protected RentalCategory referenceData(@RequestParam(value = "rentalCatNo", required = false) Integer rentalCatNo) {
        if (rentalCatNo != null) {
            RentalCategory catList = rentalCategoryService.findByCatNo(rentalCatNo);
            return catList; //取得Rental列表
        }
        return null;
    }


    /**
     * 提供所有租借品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalCategory列表。
     */
    @ModelAttribute("rentalCatListData")
    protected List<RentalCategory> referenceListData() {
        List<RentalCategory> rentalCatList =rentalCategoryService.findAll();
        return rentalCatList;
    }


    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return rental所有資料庫
     */
    @ModelAttribute("rentalListData") //取出rental資料庫
    protected List<Rental> referenceListDataRental() {
        List<Rental> rentalList = rentalService.findAll();
        return rentalList; //取得Rental列表
    }
    /**
     *前端透過Ajax方式傳送Json資料，由此處控制器方法來接收JSON資料
     *必須使用@RequestBody註釋
     */
    @PostMapping("/json")
    public String handleJson(@RequestBody RentalCategory rentalCategory){
        System.out.println(rentalCategory);
        return "finish";
    }


}