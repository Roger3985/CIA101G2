//package com.yu.rentSet.controller;
//
//import com.howard.rentalorder.entity.RentalOrder;
//import com.howard.rentalorder.service.impl.RentalOrderServiceImpl;
//import com.yu.rentSet.entity.RentSet;
//import com.yu.rentSet.service.RentSetServiceImpl;
//import com.yu.rental.entity.Rental;
//import com.yu.rentalcategory.entity.RentalCategory;
//import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/backend/rentset") //對應資料夾路徑
//public class RentSetControllerBackEnd {
//
//    @Autowired  // 自動裝配
//    private RentalOrderServiceImpl rentalOrderService;
//    @Autowired
//    private RentSetServiceImpl rentSetService;
//
//
//    //顯示後台首頁
//    @GetMapping("/backendIndex")
//    public String backendIndex() {
//        return "/backend/index";
//    }
//
//
//    //顯示全部租借方案頁面 (後台)
//    @GetMapping("/listAllRentSet")
//    public String listAllRentSet() {
//        return "/backend/rentset/listAllRentSet";
//    }
//
//
//    //顯示後台select_page
//    @GetMapping("/selectPage")
//    public String selectPage(ModelMap model) {
//        RentSet rentSet = new RentSet();
//        model.addAttribute("rentSet", rentSet);
//        return "/backend/rentset/selectRentSet";
//    }
//
//    //顯示後台 addRentSet
//    @GetMapping("/addRentSet")
//    public String addRentSet(ModelMap model) {
//        RentSet rentSet = new RentSet();
//        model.addAttribute("rentSet", rentSet);
//        return "/backend/rentset/addRentSet";
//    }
//
//
//    //處理單筆查詢(依rentalOrdNo)
//    @PostMapping("getOneDisplay")
//    public String getOneDisplay(@RequestParam(value = "rentalOrdNo",required = false) Integer rentalOrdNo, ModelMap model) {
//
//        RentSet rentSet = rentSetService.findByRentalOrdNo(rentalOrdNo);
//        List<RentSet> rentSetList = rentSetService.findAll();
//        model.addAttribute("rentSetList", rentSetList);
//
//        List<RentalOrder> rentalOrderListData = rentalOrderService.getAll();
//        model.addAttribute("rentalOrder", new RentalOrder());
//        model.addAttribute("rentalOrderListData",rentalOrderListData);
//
//        if (rentSet == null) {
//            model.addAttribute("errorMessage", "查無資料");
//            return "/backend/rentset/selectRentSet";
//        }
//        model.addAttribute("rentSet", rentSet);
//        return "/backend/rentset/listOneRentSet"; // 查詢完成後轉交
//    }
//
//    //萬用查詢
//    @GetMapping("/search")
//    public String search(@RequestParam(value = "rentalOrdNo", required = false) Integer rentalOrdNo,
//                         @RequestParam(value = "rentalSetName", required = false) String rentalSetName,
//                         @RequestParam(value = "rentalSetDays",required = false) Byte rentalSetDays,
//                         ModelMap modelMap) {
//
//        Map<String, Object> map = new HashMap<>();
//
//        if (rentalOrdNo != null) {
//            map.put("rentalOrdNo", rentalOrdNo);
//        } else if (rentalSetName != null) {
//            map.put("rentalSetName", rentalSetName);
//        } else if (rentalSetDays != null) {
//            map.put("rentalSetDays", rentalSetDays);
//        }
//
//        List<RentSet> rentSetList = rentSetService.searchRentSets(map);
//
//        modelMap.addAttribute("rentSetList", rentSetList);
//        modelMap.addAttribute("search", "true");
//
//        return "/backend/rentalmyfavorite/selectRentalMyFAV";
//
//    }
//
//
//    //處理新增
//    @PostMapping("/addRentSet")
//    public String addRentSet(@Valid RentSet rentSet, BindingResult result, ModelMap model) {
//
//        if (rentSet != null) {
//            if (result.hasErrors()) { //若有錯誤
//                //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
//                List<FieldError> fieldErrors = result.getFieldErrors();
//                for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                    FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                    model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                    model.addAttribute("rentSet", rentSet);
//                }
//                return "/backend/rentset/addRentSet";
//            }
//        }else{
//            return "/backend/rentset/addRentSet";
//        }
//        // 將資料添加到 ModelMap 中
//        rentSetService.addRentSet(rentSet);
//        model.addAttribute("rentSet", rentSet);
//
//        return "/backend/rentset/listAllRentSet";
//    }
//
//
//    //處理單筆修改
//    @PostMapping("getOneUpdate")
//    public String getOneUpdate(@RequestParam("rentalOrdNo") Integer rentalOrdNo, ModelMap model) {
//
//        RentSet rentSet = rentSetService.findByRentalOrdNo(rentalOrdNo);
//        model.addAttribute("rentSet", rentSet);
//        return "/backend/rentset/updateRentSet"; // 查詢完成後轉交
//    }
//
//
//    //處理修改
//    @PostMapping("/updateRentSet")
//    public String updateRentSet(@Valid @RequestBody RentSet rentSet,
//                                BindingResult result, ModelMap model){
//
//
//        if (result.hasErrors()) { //若有錯誤
//            //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                model.addAttribute("rentSet", rentSet);
//            }
//            return "/backend/rentset/updateRentSet";
//        }
//
//        if (rentSet != null) {
//            return "/backend/rentset/updateRentSet";
//        }
//        // 將資料添加到 ModelMap 中
//        rentSetService.updateRentSet(rentSet);
//        List<RentSet> rentalSetList = rentSetService.findAll();
//        model.addAttribute("rentalSetList", rentalSetList);
//        return "/backend/rentset/listOneRentSet";
//    }
//
//
//    /**
//     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return 與rentalOrdNo對應的RentSet資料庫
//     */
//    @ModelAttribute("rentSetData")
//    protected RentSet referenceData(@RequestParam(value = "rentalOrdNo", required = false) Integer rentalOrdNo) {
//        if (rentalOrdNo != null) {
//            RentSet rentSetData = rentSetService.findByRentalOrdNo(rentalOrdNo);
//            return rentSetData; //取得RentSet列表
//        }
//        return null;
//    }
//
//    /**
//     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return RentSet所有資料庫
//     */
//    @ModelAttribute("rentSetList")
//    protected List<RentSet> referenceListData() {
//        List<RentSet> rentSetList = rentSetService.findAll();
//        return rentSetList; //取得RentSet列表
//    }
//
//    /**
//     * 提供所有租借品資料列表供視圖渲染使用。
//     * referenceListDataOrder()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return RentalOrder列表。
//     */
//    @ModelAttribute("rentalOrderListData")
//    protected List<RentalOrder> referenceListDataOrder() {
//        List<RentalOrder> rentalOrderListData =rentalOrderService.getAll();
//        return rentalOrderListData;
//    }
//
//
//}
