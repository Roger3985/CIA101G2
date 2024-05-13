package com.yu.rental.controller;

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
@RequestMapping("/frontend") //對應資料夾路徑
public class RentalControllerFrontEnd {

    /**前端網頁需求：
     *
     * 左上角的關鍵字查詢
     * 點入商品欄後，顯示個別商品、全部商品
     * 排序方法：最新上架、價格低~高、價格高~低、最熱門
     * 點選"加入我的最愛"，即可新增查看
     *個別品項篩選 (針對類別、金額、Size)
     *
     */

    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;

////////////////////////////////////////////////////////////////////////////////////////////////
    // 測試區 //

    //排序方法：價格低~高、價格高~低
    @GetMapping("/sortAllPrice")
    public String sortAllPrice(@RequestParam("sortType") String sortType, Model model) {

        //判斷選擇哪種方式
        if("low_to_high".equals(sortType)){
            List<Rental> sortList = rentalService.findAllSort();
            model.addAttribute("rentalListData", sortList); // 顯示價格由小到大
        } else if("high_to_low".equals(sortType)){
            List<Rental> sortDESCList = rentalService.findAllSortDESC();
            model.addAttribute("rentalListData", sortDESCList); // 顯示價格由大到小
        } else {
            List<Rental> defaultSortList = rentalService.findAllSort();
            model.addAttribute("rentalListData", defaultSortList); // 預設價格由小到大
        }

        return "/frontend/rental/select_page"; // 查詢完成後轉交
    }
////////////////////////////////////////////////////////////////////////////////////////////////


    //顯示首頁
    @GetMapping("/frontend/index")
    public String index() {
        return "/frontend/index";
    }

    //瀏覽全部租借品頁面
    @GetMapping("/rental/rentalShop")
    public String rentalShop() {
        return "/frontend/rental/rentalShop";
    }


    //顯示單筆租借品頁面 (需求為查看商品頁面，故使用Get請求)
    @GetMapping("/rental/showOneRental")
    public String showOneRental(@RequestParam(value = "rentalNo") Integer rentalNo, ModelMap model) {
        //建立返回數據的對象
        Rental rental = rentalService.findByNo(rentalNo);
        model.addAttribute("rentalCategory", new RentalCategory());
        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCatListData",rentalCatListData); //所有租借品類別資訊
        if (rental == null) {
            model.addAttribute("errors", "errors");
            return "/frontend/rental/rentalShop";
        }
        model.addAttribute("rental", rental);
        return "/frontend/rental/showOneRental";
    }

//    //顯示租借品篩選
//    @GetMapping("/selectPage")
//    public String selectPage(ModelMap model) {
//        Rental rental = new Rental();
//        model.addAttribute("rental", rental);
//        return "/frontend/rental/select_page";
//    }
//
//


//    //顯示租借品新增
//    @GetMapping("/addRental")
//    public String addRentalFrom(ModelMap model) {
//        Rental rental = new Rental();
//        model.addAttribute("rental", rental);
//        return "/frontend/rental/addRental";
//    }


//    //顯示租借品修改
//    @GetMapping("/updateRental")
//    public String updateRental(Model model) {
//        //建立返回數據的對象
//        List<Rental> rentalList = rentalService.findAll();
//        model.addAttribute("rental", rentalList);
//        model.addAttribute("rentalCategory", rentalCategoryService.findAll());
//        model.addAttribute("rental", rentalList.get(0));
//        return "/frontend/rental/updateRental";
//    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //處理單筆商品查詢(依租借品編號)
    @PostMapping("/getOne_For_Display")
    public String getOne_For_Display(@RequestParam("rentalNo") String rentalNo, ModelMap model) {

        Rental rental = rentalService.getOneRental(Integer.valueOf(rentalNo));
        List<Rental> list = rentalService.findAll();
        model.addAttribute("list", list);
        model.addAttribute("rentalCategory", new RentalCategory());
        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCatListData",rentalCatListData);

        if (rental == null) {
            model.addAttribute("errors", "errors");
            return "/frontend/rental/select_page";
        }
        model.addAttribute("rental", rental);
        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
    }


//    //處理查詢(依租借品的顏色)
//    @PostMapping("/getDisplayColor")
//    public String getDisplayColor(@RequestParam("rentalColor") String rentalColor, ModelMap model) {
//
//        List<Rental> colorList = rentalService.getRentalColor(rentalColor); //找出符合相關顏色的，放入list
//        model.addAttribute("colorList", colorList);
//        model.addAttribute("rentalCategory", new RentalCategory());
//        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
//        model.addAttribute("rentalCatListData",rentalCatListData);
//
//        if (rentalColor == null) {
//            model.addAttribute("errors", "errors");
//            return "/frontend/rental/select_page";
//        }
//        model.addAttribute("rentalColor", rentalColor);
//        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }


//    //處理查詢(依租借品的尺寸)
//    @PostMapping("/getDisplayRentalSize")
//    public String getDisplayRentalSize(@RequestParam("rentalSize") String rentalSize, ModelMap model) {
//
//        List<Rental> sizeList = rentalService.getRentalSize(Integer.valueOf(rentalSize));
//        model.addAttribute("sizeList", sizeList);
//        model.addAttribute("rentalCategory", new RentalCategory());
//        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
//        model.addAttribute("rentalCatListData",rentalCatListData);
//
//        if (rentalSize == null) {
//            model.addAttribute("errors", "errors");
//            return "/frontend/rental/select_page";
//        }
//        model.addAttribute("rentalSize", rentalSize);
//        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }





    //關鍵字查詢(依租借品的名稱 "模糊查詢")
//    @PostMapping("/getQueryRentalName")
//    public String getQueryRentalName(@RequestParam("rentalName") String rentalName, ModelMap model) {
//
//        List<Rental> nameList = rentalService.getRentalName(rentalName);
//        model.addAttribute("nameList", nameList);
//
//        if (rentalName == null) {
//            model.addAttribute("errors", "errors");
//            return "/frontend/rental/select_page";
//        }
//        model.addAttribute("rentalName", rentalName);
//        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }

    //處理複合查詢
    @PostMapping("search")
    public String search(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        //建立返回數據的對象
        List<Rental> queryList = rentalService.searchRentals(map);
        model.addAttribute("queryList", queryList);
        return "frontend/rental/listAllRental"; //結果傳至listAllRental
    }




//排序方法：最新上架 (顯示近期上架，可依據已上架產品+租借品狀態為上架中作為排序依據)
//處理單個類別查詢(依租借品的類別編號)  --------> 租借品類別去實作給前端網頁







    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalNo對應的rental資料庫
     */
    @ModelAttribute("rentalData") // 以rentalNo搜索，取出對應的rental資料庫
    protected Rental referenceData(@RequestParam(value = "rentalNo", required = false) Integer rentalNo) {
        if (rentalNo != null) {
            Rental list = rentalService.findByNo(rentalNo);
            return list; //取得Rental列表
        }
        return null;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return rental所有資料庫
     */
    @ModelAttribute("rentalListData") //取出rental資料庫
    protected List<Rental> referenceListData() {
        List<Rental> rentalList = rentalService.findAll();
        return rentalList; //取得Rental列表
    }

    /**
     * 提供所有租借品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
     * referenceMapData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalCategory列表。
     */
    @ModelAttribute("rentalCatListData")
    protected List<RentalCategory> referenceListDataCat() {
        List<RentalCategory> rentalCatList =rentalCategoryService.findAll();
        return rentalCatList;
    }

    /**
     *前端透過Ajax方式傳送Json資料，由此處控制器方法來接收JSON資料
     *必須使用@RequestBody註釋
     */
    @PostMapping("/json")
    public String handleJson(@RequestBody Rental rental){
        System.out.println(rental);
        return "finish";
    }
}
