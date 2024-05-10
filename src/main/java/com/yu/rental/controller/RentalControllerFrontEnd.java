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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend/rental") //對應資料夾路徑
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



    //顯示首頁
    @GetMapping("/")
    public String index() {
        return "/index";
    }

    //顯示所有租借品
    @GetMapping("/listAllRental")
    public String listAllRental() {
        return "/frontend/rental/listAllRental";
    }


    //顯示租借品篩選
    @GetMapping("/selectPage")
    public String selectPage(ModelMap model) {
        Rental rental = new Rental();
        model.addAttribute("rental", rental);
        return "/frontend/rental/select_page";
    }

    
    //顯示單一租借品
//    @GetMapping("/listOneRental")  //required = true：請求參數不可為null(預設)
//    public String getOneRental(@RequestParam(value = "rentalNo",required = true) Integer rentalNo, ModelMap model) {
//        //建立返回數據的對象
//        Rental rental = rentalService.findByNo(rentalNo);
//        model.addAttribute("rental", rental);
//        return "/frontend/rental/listOneRental";
//    }

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
    

    //處理查詢(依租借品的顏色)
    @PostMapping("/getDisplayColor")
    public String getDisplayColor(@RequestParam("rentalColor") String rentalColor, ModelMap model) {

        List<Rental> colorList = rentalService.getRentalColor(rentalColor); //找出符合相關顏色的，放入list
        model.addAttribute("colorList", colorList);
        model.addAttribute("rentalCategory", new RentalCategory());
        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCatListData",rentalCatListData);

        if (rentalColor == null) {
            model.addAttribute("errors", "errors");
            return "/frontend/rental/select_page";
        }
        model.addAttribute("rentalColor", rentalColor);
        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
    }


    //處理查詢(依租借品的尺寸)
    @PostMapping("/getDisplayRentalSize")
    public String getDisplayRentalSize(@RequestParam("rentalSize") String rentalSize, ModelMap model) {

        List<Rental> sizeList = rentalService.getRentalSize(Integer.valueOf(rentalSize));
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("rentalCategory", new RentalCategory());
        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCatListData",rentalCatListData);

        if (rentalSize == null) {
            model.addAttribute("errors", "errors");
            return "/frontend/rental/select_page";
        }
        model.addAttribute("rentalSize", rentalSize);
        return "/frontend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
    }


    //排序方法：最新上架、價格低~高、價格高~低、最熱門
    @PostMapping("sortingToCheap")
    public String sortingToCheap(@RequestParam("rentalPrice") String rentalPrice, ModelMap model) {
        BigDecimal getPrice = BigDecimal.valueOf(Double.parseDouble(rentalPrice));
        List<Rental> cheapList = rentalService.getRentalPriceDESC(getPrice);
        model.addAttribute("cheapList", cheapList);

        return "/frontend/rental/listAllRental"; // 查詢完成後轉交listAllRental.html
    }

    @PostMapping("sortingToExpensive")
    public String sortingToExpensive(@RequestParam("rentalPrice") String rentalPrice, ModelMap model) {
        BigDecimal getPrice = BigDecimal.valueOf(Double.parseDouble(rentalPrice));
        List<Rental> expensiveList = rentalService.getRentalPrice(getPrice);
        model.addAttribute("expensiveList", expensiveList);

        return "/frontend/rental/listAllRental"; // 查詢完成後轉交listAllRental.html
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
     * @return rental所有資料庫
     */
    @ModelAttribute("rentalListData") //取出rental資料庫
    protected List<Rental> referenceListData() {
        List<Rental> rentalList = rentalService.findAll();
        return rentalList; //取得Rental列表
    }
    
    
}
