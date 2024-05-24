package com.yu.rentSet.controller;

import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.service.impl.RentalOrderServiceImpl;
import com.yu.rentSet.entity.RentSet;
import com.yu.rentSet.service.RentSetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rentset") //對應資料夾路徑
public class RentSetControllerBackEnd {

    /**
     * 註：
     *    進行CRUD操作時，確保在保存實體前設置外來鍵關聯。
     *    例： address(從)、user(主)
     *         User user = new User();
     *         Address address = new Address();
     *         address.setUser(user);  //設置外來鍵關聯
     *         userRepository.save(user);
     */


    @Autowired  // 自動裝配
    private RentalOrderServiceImpl rentalOrderService;
    @Autowired
    private RentSetServiceImpl rentSetService;


    //顯示首頁 (後台)
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }

    //顯示查詢頁面 (後台)
    @GetMapping("/selectRentSet")
    public String selectRentSet(ModelMap model) {
        RentSet rentSet = new RentSet();
        rentSet.setRentalOrdNo(0); // 初始化rentalOrdNo
        model.addAttribute("rentSet", rentSet);
        return "/backend/rentset/selectRentSet";
    }

    //顯示全部租借方案頁面 (後台)
    @GetMapping("/listAllRentSet")
    public String listAllRentSet() {
        return "/backend/rentset/listAllRentSet";
    }

    //顯示新增頁面 (後台)
    @GetMapping("/addRentSet")
    public String addRentSet(ModelMap model) {
        RentSet rentSet = new RentSet();
        model.addAttribute("rentSet", rentSet);
        return "/backend/rentset/addRentSet";
    }


    //處理單筆查詢(依rentalOrdNo)
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam(value = "rentalOrdNo", required = false) Integer rentalOrdNo,
                                ModelMap model) {

        RentSet rentSet = rentSetService.findByRentalOrdNo(rentalOrdNo);
        List<RentSet> rentSetList = rentSetService.findAll();
        model.addAttribute("rentSetList", rentSetList);

        if (rentSet == null) {
            model.addAttribute("errors", "查無資料");
            return "/backend/rentset/selectRentSet";
        }
        model.addAttribute("rentSet", rentSet);
        return "/backend/rentset/listOneRentSet";
    }


    //處理單筆修改 (依rentalOrdNo)
    @PostMapping("getOneUpdate")
    public String getOneUpdate(@RequestParam("rentalOrdNo") Integer rentalOrdNo, ModelMap model) {

        RentSet rentSet = rentSetService.findByRentalOrdNo(rentalOrdNo);
        List<RentSet> rentSetList = rentSetService.findAll();
        model.addAttribute("rentSetList", rentSetList);

        if (rentSet == null) {
            model.addAttribute("errors", "查無資料");
            return "/backend/rentset/selectRentSet";
        }
        model.addAttribute("rentSet", rentSet);
        return "/backend/rentset/updateRentSet";
    }


    // 處理修改資料
    @PostMapping("updateRentSet")
    public String updateRentSet(@Valid RentSet rentSet, ModelMap model){

        // 將資料添加到 ModelMap 中
        rentSetService.updateRentSet(rentSet);
        rentSet = rentSetService.findByRentalOrdNo(rentSet.getRentalOrdNo());
        model.addAttribute("rentSet", rentSet);
        return "/backend/rentset/listOneRentSet";
    }


    // 處理新增資料
    @PostMapping("/addRentSet")
    public String addRentSet(@Valid RentSet rentSet, BindingResult result, ModelMap model){

        System.out.println("第一抓取資料"+rentSet);

        if (result.hasErrors()) {
            return "/backend/rentset/addRentSet";
        }

        System.out.println("第三抓取資料"+rentSet);

        // 將資料添加到 ModelMap 中
        rentSetService.addRentSet(rentSet);
        rentSet = rentSetService.findByRentalOrdNo(rentSet.getRentalOrdNo());
        model.addAttribute("rentSet", rentSet);
        System.out.println("有新增哦"+rentSet);
        return "redirect:/backend/rentset/listAllRentSet";
    }


    //萬用查詢
    @GetMapping("search")
    public String search(@RequestParam(value = "rentalOrdNo", required = false) Integer rentalOrdNo,
                         @RequestParam(value = "rentalSetName", required = false) String rentalSetName,
                         @RequestParam(value = "rentalSetDays",required = false) Byte rentalSetDays,
                         ModelMap modelMap) {

        Map<String, Object> map = new HashMap<>();

        if (rentalOrdNo != null) {
            map.put("rentalOrdNo", rentalOrdNo);
        } else if (rentalSetName != null) {
            map.put("rentalSetName", rentalSetName);
        } else if (rentalSetDays != null) {
            map.put("rentalSetDays", rentalSetDays);
        }

        List<RentSet> rentSetList = rentSetService.searchRentSets(map);
        modelMap.addAttribute("rentSetList", rentSetList);
        modelMap.addAttribute("search", "true");

        return "/backend/rentalmyfavorite/selectRentalMyFAV";
    }


    /**
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalOrdNo對應的RentSet資料庫
     */
    @ModelAttribute("rentSetData")
    protected RentSet referenceData(@RequestParam(value = "rentalOrdNo", required = false) Integer rentalOrdNo) {
        if (rentalOrdNo != null) {
            RentSet rentSetData = rentSetService.findByRentalOrdNo(rentalOrdNo);
            return rentSetData;
        }
        return null;
    }

    /**
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentSet所有資料庫
     */
    @ModelAttribute("rentSetListData")
    protected List<RentSet> referenceListData() {
        List<RentSet> rentSetList = rentSetService.findAll();
        return rentSetList; //取得RentSet列表
    }

    /**
     * 提供所有租借品資料列表供視圖渲染使用。
     * referenceListDataOrder()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalOrder列表。
     */
    @ModelAttribute("rentalOrderListData")
    protected List<RentalOrder> referenceListDataOrder() {
        List<RentalOrder> rentalOrderList =rentalOrderService.getAll();
        return rentalOrderList;
    }


}
