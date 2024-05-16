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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rental") //對應資料夾路徑
public class RentalControllerBackEnd {

    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;


    //顯示後台首頁
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }


    //顯示全部租借品頁面 (後台)
    @GetMapping("/listAllRental")
    public String listAllRental() {
        return "/backend/rental/listAllRental";
    }


    //顯示單筆租借品頁面 (後台)
//    @GetMapping("listOneRental")
//    public String listOneRental(@RequestParam(value = "rentalNo") Integer rentalNo, ModelMap model) {
//        //建立返回數據的對象
//        Rental rental = rentalService.findByNo(rentalNo);
//        model.addAttribute("rental", rental);
//        return "/backend/rental/listOneRental";
//    }

    //顯示後台 select_page.html
    @GetMapping("/selectPage")
    public String selectPage(ModelMap model) {
        Rental rental = new Rental();
        rental.setRentalNo(0); // 初始化rentalNo
        model.addAttribute("rental", rental);
        return "/backend/rental/select_page";
    }

    //顯示後台 addRental.html
    @GetMapping("/addRental")
    public String addRental(ModelMap model) {
        Rental rental = new Rental();
        model.addAttribute("rental", rental);
        return "/backend/rental/addRental";
    }

    //顯示後台 updateRental.html
    @GetMapping("/updateRental")
    public String updateRental(ModelMap model) {
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);
        model.addAttribute("rentalList", rentalList.get(0));
        model.addAttribute("rentalCategory", rentalCategoryService.findAll());
        return "/backend/rental/updateRental";
    }

    //針對特定租借品編號做update
    @GetMapping("/updateRental/{rentalNo}")
    public String OneToUpdate(@PathVariable Integer rentalNo, ModelMap model) {
        Rental rental = rentalService.findByNo(rentalNo);
        model.addAttribute("rental", rental);
        return "/backend/rental/updateRental";
    }

    //處理單筆查詢(依rentalNo)
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam(value = "rentalNo",required = false) Integer rentalNo, ModelMap model) {

        Rental rental = rentalService.findByNo(rentalNo);
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);

        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCategory", new RentalCategory());
        model.addAttribute("rentalCatListData",rentalCatListData);

        if (rental == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "/backend/rental/select_page";
        }
        model.addAttribute("rental", rental);
        return "/backend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
    }

    //處理查詢(依rentalStat)
    @PostMapping("getDisplayRentalStat")
    public String getDisplayRentalStat(@RequestParam("rentalStat") String rentalStat, ModelMap model) {

        List<Rental> rentalStatList = rentalService.findByStat(Byte.valueOf(rentalStat)); // 修改此处调用的方法
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);

        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCategory", new RentalCategory());
        model.addAttribute("rentalCatListData", rentalCatListData);

        if (rentalList.isEmpty()) {
            model.addAttribute("errorMessage", "查無資料");
            return "/backend/rental/select_page";
        }
        model.addAttribute("rentalStatList", rentalStatList);
        return "/backend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
    }


//    //處理單筆查詢(依rentalName)
//    @PostMapping("getDisplayName/{rentalName}")
//    public String getDisplayName(@RequestParam("rentalName") String rentalName, ModelMap model) {
//
//        Rental rental = rentalService.findByRentalName(rentalName);
//        List<Rental> rentalList = rentalService.findAll();
//        model.addAttribute("rentalList", rentalList);
//
//        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
//        model.addAttribute("rentalCategory", new RentalCategory());
//        model.addAttribute("rentalCatListData",rentalCatListData);
//
//        if (rental == null) {
//            model.addAttribute("errorMessage", "查無資料");
//            return "/backend/rental/select_page";
//        }
//        model.addAttribute("rental", rental);
//        return "/backend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }
//
//    關鍵字查詢(依rentalName "模糊查詢")
//    @PostMapping("/getQueryRentalName")
//    public String getQueryRentalName(@RequestParam("rentalName") String rentalName, ModelMap model) {
//
//        List<Rental> nameList = rentalService.getRentalName(rentalName);
//        model.addAttribute("nameList", nameList);
//
//        if (rentalName == null) {
//            model.addAttribute("errors", "errors");
//            return "/backend/rental/select_page";
//        }
//        model.addAttribute("rentalName", rentalName);
//        return "/backend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }
//
//
//    //處理查詢(依rentalColor)
//    @PostMapping("/getDisplayColor/{rentalColor}")
//    public String getDisplayColor(@PathVariable String rentalColor, ModelMap model) {
//
//        Rental rental = rentalService.getRentalColor(rentalColor); //找出符合相關顏色的，放入list
//        List<Rental> rentalList = rentalService.findAll();
//        model.addAttribute("rentalList", rentalList);
//
//        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
//        model.addAttribute("rentalCategory", new RentalCategory());
//        model.addAttribute("rentalCatListData",rentalCatListData);
//
//        if (rental == null) {
//            model.addAttribute("errors", "errors");
//            return "/backend/rental/select_page";
//        }
//        model.addAttribute("rental", rental);
//        return "/backend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }
//
//
//    //處理查詢(依rentalSize)
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
//            return "/backend/rental/select_page";
//        }
//        model.addAttribute("rentalSize", rentalSize);
//        return "/backend/rental/listOneRental"; // 查詢完成後轉交listOneRental.html
//    }
//
//



    /**
     * Map<String, String[]> 應用於從HTTP請求中取得參數時，處理表單提交、REST API請求做使用
     *透過HttpServletRequest獲得請求參數，將參數傳遞 searchRentals()方法執行。
     *查詢結果儲存於queryList中，最後由model顯示在網頁
     *
     */
    //處理複合查詢
    @PostMapping("search")
    public String search(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        //建立返回數據的對象
        List<Rental> queryList = rentalService.searchRentals(map);
        model.addAttribute("queryList", queryList);
        return "backend/rental/listAllRental"; //結果傳至listAllRental
    }


    //處理新增
    @PostMapping("addRental")
    public String addRental(@Validated(Rental.AddRentalGroup.class) Rental rental, BindingResult result, ModelMap model) {

        if (rental != null) {
            if (result.hasErrors()) { //若有錯誤
                //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
                List<FieldError> fieldErrors = result.getFieldErrors();
                for (int i = 0, len = fieldErrors.size(); i < len; i++) {
                    FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
                    model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
                    model.addAttribute("rental", rental);
                }
                return "/backend/rental/addRental";
            }
        }else{
            return "/backend/rental/addRental";
        }
        // 將資料添加到 ModelMap 中
        rentalService.addRental(rental);
        rental = rentalService.findByNo(Integer.valueOf(rental.getRentalNo()));
        model.addAttribute("rental", rental);

        return "/backend/rental/listAllRental";
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


    //處理修改
    @PostMapping("updateRental")
    public String updateRental(@Valid @RequestBody Rental rental, @PathVariable Integer rentalNo,
                               BindingResult result, ModelMap model){


        if (result.hasErrors()) { //若有錯誤
            //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
                model.addAttribute("rental", rental);
            }
            return "/backend/rental/updateRental";
        }

        if (rental != null) {
            return "/backend/rental/updateRental";
        }
        // 將資料添加到 ModelMap 中
        rentalService.updateRental(rental);
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);
        return "/backend/rental/listOneRental";
    }


//    //處理單筆修改
//    @PostMapping("getOneUpdate")
//    public String getOneUpdate(@RequestParam("rentalNo") Integer rentalNo, @RequestParam("rentalCatNo") String rentalCatNo ,ModelMap model) {
//        Rental rental = rentalService.findByNo(rentalNo);
//        model.addAttribute("rental", rental);
//        RentalCategory rentalCategory = rentalCategoryService.findByCatNo(Integer.valueOf(rentalCatNo));
//        model.addAttribute("rentalCategory", rentalCategory);
//        return "/backend/rental/updateRental"; // 查詢完成後轉交update_rental.html
//    }


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


}