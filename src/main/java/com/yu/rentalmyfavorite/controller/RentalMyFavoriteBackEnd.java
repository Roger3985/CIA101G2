package com.yu.rentalmyfavorite.controller;

import com.roger.member.entity.Member;
import com.roger.member.service.impl.MemberServiceImpl;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rentalmyfavorite") //對應資料夾路徑
public class RentalMyFavoriteBackEnd {


    @Autowired  // 自動裝配
    private RentalMyFavoriteServiceImpl rentalMyFAVService;
    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired  // 自動裝配
    private MemberServiceImpl memberService;


    //顯示後台首頁
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }


    //顯示全部租借品照片頁面 (後台)
    @GetMapping("/listAllRentalMyFAV")
    public String listAllRentalMyFAV() {
        return "/backend/rentalmyfavorite/listAllRentalMyFAV";
    }


    //顯示後台 select_page
    @GetMapping("/selectRentalMyFAV")
    public String selectPage(ModelMap model) {
        RentalMyFavorite rentalMyFavorite = new RentalMyFavorite();
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/selectRentalMyFAV";
    }


    //顯示後台 addRentalMyFAV
    @GetMapping("/addRentalMyFAV")
    public String addRentalMyFAV(ModelMap model) {
        RentalMyFavorite rentalMyFavorite = new RentalMyFavorite();
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/addRentalMyFAV";
    }


    //處理查詢
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam("rentalNo") String rentalNo, @RequestParam("memNo") String memNo, ModelMap model) {

        RentalMyFavorite rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
        List<RentalMyFavorite> rentalCatList = rentalMyFAVService.findAll();
        model.addAttribute("rentalCatList", rentalCatList);

        List<Rental> rentalList = rentalService.findAll();
        model.addAttribute("rental", new Rental());
        model.addAttribute("rentalList", rentalList);

        List<Member> memberList = memberService.findAll();
        model.addAttribute("member", new Member());
        model.addAttribute("memberList", memberList);


        if (rentalMyFavorite == null) {
            model.addAttribute("errors", "errors");
            return "/backend/rentalmyfavorite/selectRentalMyFAV";
        }
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/listOneRentalMyFAV"; // 查詢完成後轉交
    }

    //萬用查詢
    @GetMapping("/search")
    public String search(@RequestParam(value = "rentalNo", required = false) Integer rentalNo,
                         @RequestParam(value = "memNo", required = false) Integer memNo,
                         @RequestParam(value = "rentalFavTime",required = false) Timestamp rentalFavTime,
                         ModelMap modelMap) {

        Map<String, Object> map = new HashMap<>();

        if (rentalNo != null) {
            map.put("rentalNo", rentalNo);
        } else if (memNo != null) {
            map.put("memNo", memNo);
        } else if (rentalFavTime != null) {
            map.put("rentalFavTime", rentalFavTime);
        }


        List<RentalMyFavorite> rentalMyFAVList = rentalMyFAVService.searchRentalMyFAVs(map);


        modelMap.addAttribute("rentalMyFAVList", rentalMyFAVList);
        modelMap.addAttribute("search", "true");

        return "/backend/rentalmyfavorite/selectRentalMyFAV";

    }


    // 處理修改資料
    @PostMapping("updateRentalMyFAV")
    public String updateRentalMyFAV(@RequestParam("rentalNo") Integer rentalNo,
                                    @RequestParam("memNo") Integer memNo, BindingResult result, ModelMap model) {

//        //如果沒有在最愛清單內，返回原Update頁面
//        if (rentalMyFAVService.findByIdRentalNoAndIdMemNo(rentalNo, memNo) == null) {
//            return "/backend/rentalmyfavorite/updateRentalMyFAV";
//        }
//
//        // 將資料添加到 ModelMap 中
//        RentalMyFavorite.CompositeFavorite compositeFavorite = new RentalMyFavorite.CompositeFavorite(rentalNo, memNo);
//
//        RentalMyFavorite list = rentalMyFAVService.updateRentalFav(rentalMyFavorite);
////        rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//        model.addAttribute("list", list);
        return "/backend/rentalmyfavorite/listOneRentalMyFAV";
    }


    //處理單筆修改
    @PostMapping("getOneUpdate")
    public String getOneUpdate(@RequestParam("rentalNo") Integer rentalNo, @RequestParam("memNo") Integer memNo, ModelMap model) {

        RentalMyFavorite rentalMyFavorite = rentalMyFAVService.findByIdRentalNoAndIdMemNo(rentalNo, memNo);
        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
        return "/backend/rentalmyfavorite/updateRentalMyFAV"; // 查詢完成後轉交
    }

    //處理新增
//    @PostMapping("/addRentalMyFAV")
//    public String addRentalMyFAV(@Valid RentalMyFavorite rentalMyFavorite,BindingResult result, ModelMap model) throws IOException {
//
//        if (result.hasErrors()) { //若有錯誤
//            //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//            }
//            return "/backend/rentalmyfavorite/addRentalMyFAV";
//        }
//
//            // 將資料添加到 ModelMap 中
//            rentalMyFAVService.addRentalFav(rentalMyFavorite);
//            model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//
//            return "/backend/rentalmyfavorite/listAllRentalMyFAV";
//        }

    //處理新增
    @PostMapping("/addRentalMyFAV")
    @ResponseBody
    public ResponseEntity<String> addRentalMyFAV(@RequestBody RentalMyFavorite rentalMyFavorite) {

        return ResponseEntity.ok("成功");
    }

//        if (result.hasErrors()) { //若有錯誤
//            //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//            }
//            return "/backend/rentalmyfavorite/addRentalMyFAV";
//        }
//
//            // 將資料添加到 ModelMap 中
//            rentalMyFAVService.addRentalFav(rentalMyFavorite);
//            model.addAttribute("rentalMyFavorite", rentalMyFavorite);






    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalMyFavorite所有資料庫
     */
    @ModelAttribute("rentalMyFAVList")
    protected List<RentalMyFavorite> referenceListData() {
        List<RentalMyFavorite> rentalMyFAVList = rentalMyFAVService.findAll();
        return rentalMyFAVList; //取得RentalMyFavorite列表
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
     * 提供所有租借品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
     * referenceMapData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalCategory列表。
     */
    @ModelAttribute("memberListData")
    protected List<Member> referenceListDataMember() {
        List<Member> memberList = memberService.findAll();
        return memberList;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalNo & memNo 對應的RentalMyFavorite資料庫
     */
    @ModelAttribute("rentalMyFAVData")
    protected RentalMyFavorite referenceData(@RequestParam(value = "rentalNo", required = false) Integer rentalNo,
                                             @RequestParam(value = "memNo", required = false) Integer memNo) {

        if (rentalNo != null && memNo != null) {
            RentalMyFavorite list = rentalMyFAVService.findByIdRentalNoAndIdMemNo(rentalNo,memNo);
            return list; //取得RentalMyFavorite列表
        }
        return null;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceMemNoData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與memNo 對應的RentalMyFavorite資料庫
     */
    @ModelAttribute("memNoData")
    protected RentalMyFavorite referenceMemNoData(@RequestParam(value = "memNo", required = false) Integer memNo) {

        if (memNo != null) {
            RentalMyFavorite list = rentalMyFAVService.findByMemNo(memNo);
            return list; //取得RentalMyFavorite列表
        }
        return null;
    }



}


