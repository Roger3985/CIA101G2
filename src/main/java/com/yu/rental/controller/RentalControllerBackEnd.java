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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rental") //對應資料夾路徑
public class RentalControllerBackEnd {

    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;

    //顯示首頁////////////////////////////////////////////////////////////////////////////////////////////////////////////待處理(無法顯示正確畫面，404)
    @GetMapping("backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }

    //顯示後台 select_RentalCategory_page.html//////////////////////////////////////////////////////////////////////////待處理(無法單一查詢租借品名稱)
    @GetMapping("selectPage")
    public String selectPage(ModelMap model) {
        Rental rental = new Rental();
        rental.setRentalNo(0); // 初始化rentalNo
        model.addAttribute("rental", rental);
        return "/backend/rental/select_page";
    }

    //顯示後台 listAllRental.html
    @GetMapping("listAllRental")
    public String listAllRental() {
        return "/backend/rental/listAllRental";
    }

    //顯示後台 showOneRental.html
    @GetMapping("listOneRental")  //required = true：請求參數不可為null(預設)
    public String listOneRental(@RequestParam(value = "rentalNo",required = true) Integer rentalNo, ModelMap model) {
        //建立返回數據的對象
        Rental rental = rentalService.findByNo(rentalNo);
        model.addAttribute("rental", rental);
        return "/backend/rental/listOneRental";
    }

    //顯示後台 addRental.html
    @GetMapping("addRental")
    public String addRental(ModelMap model) {
        Rental rental = new Rental();
        model.addAttribute("rental", rental);
        return "/backend/rental/addRental";
    }

    //顯示後台 updateRental.html////////////////////////////////////////////////////////////////////////////////////////待處理(頁面可顯示，但沒選項)
    @GetMapping("updateRental")
    public String updateRental(ModelMap model) {
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);
        model.addAttribute("rentalList", rentalList.get(0));
        model.addAttribute("rentalCategory", rentalCategoryService.findAll());
        return "/backend/rental/updateRental";
    }


    //處理單筆查詢(依編號)
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam("rentalNo") String rentalNo, ModelMap model) {

        Rental rental = rentalService.getOneRental(Integer.valueOf(rentalNo));
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
        rental = rentalService.getOneRental(Integer.valueOf(rental.getRentalNo()));
        model.addAttribute("rental", rental);

        return "/backend/rental/listAllRental";
    }

    //處理新增 (路徑參數，{rentalNo}為動態值)
//    @PostMapping("/addRental/{rentalNo}")
//    @ResponseBody
//    public String getRentalById(@PathVariable Integer rentalNo, ModelMap model) {
//        if(rentalNo == null){
//            return "backend/rental/addRental";
//        }
//        // 使用Rental進行使用者查詢操作
//        rentalService.findByNo(rentalNo); //新增
//        return "Rental details for rentalNo: " + rentalNo;
//    }


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
    @PostMapping("updateRental")/////////////////////////////////////////////////////////////////////////////////////待處理(頁面可顯示，但沒選項)
    public String updateRental(@Validated Rental rental, BindingResult result, ModelMap model){

        if (rental != null) {
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
        }else{
            return "/backend/rental/updateRental";
        }
        // 將資料添加到 ModelMap 中
        rentalService.updateRental(rental);
        rental = rentalService.getOneRental(Integer.valueOf(rental.getRentalNo()));
        model.addAttribute("rental", rental);
        return "/backend/rental/listOneRental";
    }


    //處理單筆修改
    @PostMapping("getOneUpdate")////////////////////////////////////////////////////////////////////////////////待處理(頁面可顯示，但沒選項)
    public String getOneUpdate(@RequestParam("rentalNo") String rentalNo, @RequestParam("rentalCatNo") String rentalCatNo ,ModelMap model) {
        Rental rental = rentalService.getOneRental(Integer.valueOf(rentalNo));
        model.addAttribute("rental", rental);
        RentalCategory rentalCategory = rentalCategoryService.getOneRentalCat(Integer.valueOf(rentalCatNo));
        model.addAttribute("rentalCategory", rentalCategory);
        return "/backend/rental/updateRental"; // 查詢完成後轉交update_rental.html
    }


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