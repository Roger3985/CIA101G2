package com.yu.rental.controller;

import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalcategory.entity.RentalCategory;
import com.yu.rentalcategory.service.RentalCategoryServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rental") //對應資料夾路徑
public class RentalControllerBackEnd {

    // 自動裝配
    @Autowired
    private RentalServiceImpl rentalService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;

//處理單筆查詢(依rentalNo)
@PostMapping("getDisplayRentalSize")
public String getDisplayRentalSize(@RequestParam(value = "rentalSize", required = false) String rentalSize, ModelMap model) {

    List<Rental> sizeData = rentalService.findByRentalColor(rentalSize);  //取得對應數據
    List<Rental> rentalList = rentalService.findAll();
    model.addAttribute("rentalList", rentalList); //取的findAll()資料

    List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
    model.addAttribute("rentalCategory", new RentalCategory());
    model.addAttribute("rentalCatListData",rentalCatListData);

    if (sizeData == null) {
        model.addAttribute("errors", "查無資料");
        return "/backend/rental/select_page";
    }
    model.addAttribute("sizeData", sizeData);
    return "/backend/rental/listOneRental";
}

    //顯示首頁 (後台)
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }

    //顯示查詢頁面 (後台)
    @GetMapping("/selectPage")
    public String selectPage(ModelMap model) {
        Rental rental = new Rental();
        rental.setRentalNo(0); // 初始化rentalNo
        model.addAttribute("rental", rental);
        return "/backend/rental/select_page";
    }

    //顯示全部租借品頁面 (後台)
    @GetMapping("/listAllRental")
    public String listAllRental() {
        return "/backend/rental/listAllRental";
    }

    //顯示新增頁面 (後台)
    @GetMapping("/addRental")
    public String addRental(ModelMap model) {
        Rental rental = new Rental();
        model.addAttribute("rental", rental);
        return "/backend/rental/addRental";
    }


    //處理單筆查詢(依rentalNo)
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam(value = "rentalNo", required = false) Integer rentalNo, ModelMap model) {

        Rental rental = rentalService.findByNo(rentalNo);  //取得對應數據
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);

        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCategory", new RentalCategory());
        model.addAttribute("rentalCatListData",rentalCatListData);

        if (rental == null) {
            model.addAttribute("errors", "查無資料");
            return "/backend/rental/select_page";
        }
        model.addAttribute("rental", rental);
        return "/backend/rental/listOneRental";
    }

    //處理單筆修改(依rentalNo)
    @PostMapping("getOneUpdate")
    public String getOneUpdate(@RequestParam("rentalNo") Integer rentalNo, ModelMap model) {

        Rental rental = rentalService.findByNo(rentalNo);
        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rentalList", rentalList);

        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCategory", new RentalCategory());
        model.addAttribute("rentalCatListData",rentalCatListData);

        if (rental == null) {
            model.addAttribute("errors", "查無資料");
            return "/backend/rental/select_page";
        }
        model.addAttribute("rental", rental);
        return "/backend/rental/updateRental";
    }


    // 處理修改資料
    @PostMapping("updateRental")
    public String updateRental(@Validated(Rental.UpdateRentalGroup.class) Rental rental,
                               BindingResult result, ModelMap model) {

        //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
                model.addAttribute("rental", rental);
            }
            return "/backend/rental/updateRental";
        }
        // 將資料添加到 ModelMap 中
        rentalService.updateRental(rental);
        rental = rentalService.findByNo(rental.getRentalNo());
        model.addAttribute("rental", rental);
        return "/backend/rental/listOneRental";
    }


    // 處理新增資料
    @PostMapping("addRental")
    public String addRental(@Validated(Rental.AddRentalGroup.class) Rental rental,
                            BindingResult result, ModelMap model) {

        //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
        if (rental != null) {
            if (result.hasErrors()) {
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
        rental = rentalService.findByNo(rental.getRentalNo());
        model.addAttribute("rental", rental);

        return "/backend/rental/listAllRental";
    }

    /**
     * Map<String, String[]> 應用於從HTTP請求中取得參數時，處理表單提交、REST API請求做使用
     *透過HttpServletRequest獲得請求參數，將參數傳遞 searchRentals()方法執行。
     *查詢結果儲存於queryList中，最後由model顯示在網頁
     *
     */
    //處理複合查詢
    @GetMapping("search")
    public String search(@RequestParam(required = false) Integer rentalNo,
                         @RequestParam(required = false) Integer rentalCatNo,
                         @RequestParam(required = false) String rentalName,
                         @RequestParam(required = false) BigDecimal rentalPrice,
                         @RequestParam(required = false) Integer rentalSize,
                         @RequestParam(required = false) String rentalColor,
                         @RequestParam(required = false) String rentalInfo,
                         @RequestParam(required = false) Byte rentalStat,
                         ModelMap modelMap) {

        Map<String, Object> map = new HashMap<>();

        if (rentalNo != null) {
            map.put("rentalNo", rentalNo);
        } else if (rentalCatNo != null) {
            map.put("rentalCatNo", rentalCatNo);
        } else if (rentalName != null) {
            map.put("rentalName", rentalName);
        } else if (rentalPrice != null) {
            map.put("rentalPrice", rentalPrice);
        } else if (rentalSize != null) {
            map.put("rentalSize", rentalSize);
        } else if (rentalColor != null) {
            map.put("rentalColor", rentalColor);
        } else if (rentalInfo != null) {
            map.put("rentalInfo", rentalInfo);
        } else if (rentalStat != null) {
            map.put("rentalStat", rentalStat);
        }

        List<Rental> rentalList = rentalService.searchRentals(map);
        modelMap.addAttribute("rentalList", rentalList);
        modelMap.addAttribute("search", "true");

        return "/backend/rental/listOneRental";
    }


    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalNo對應的rental資料庫
     */
    @ModelAttribute("rentalData") // 以rentalNo查詢，取出對應的rental資料庫
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
    @ModelAttribute("rentalListData")
    protected List<Rental> referenceListData() {
        List<Rental> rentalList = rentalService.findAll();
        return rentalList;
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