package com.yu.rentalmyfavorite.controller;

import com.howard.rentalorder.dto.SetToCart;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.roger.member.service.impl.MemberServiceImpl;
import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalmyfavorite.dto.addToWishList;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import com.yu.rentalpic.entity.RentalPic;
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
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontEnd") //對應資料夾路徑
public class RentalMyFavoriteFrontEnd {


    @Autowired  // 自動裝配
    private RentalMyFavoriteServiceImpl rentalMyFAVService;
    @Autowired
    private RentalServiceImpl rentalService;
    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private MemberRepository memberRepository;


    //瀏覽全部最愛清單頁面 (前台)
    @GetMapping("/rental/rentalFAVList")
    public String rentalFAVList() {
        return "/frontend/rental/rentalFAVList";
    }


//    處理新增最愛清單 (舊版)
//    @PostMapping("/addToWishList")
//    public String addToWishList(@RequestBody RentalMyFavorite rentalMyFavorite) {
//        // 將前端傳入的日期 原 ISO 8601型態 轉為 Timestamp型態
//        rentalMyFavorite.setRentalFavTime(Timestamp.from(Instant.parse(rentalMyFavorite.getRentalFavTime().toString())));
//
//        // 新增至資料庫
//        rentalMyFAVService.addRentalFav(rentalMyFavorite);
//        return "success";
//    }



    //    處理新增最愛清單 (新新新新新新 版 )
//    @PostMapping("/addToWishList")
//    public ResponseEntity<?> addToWishList(@RequestBody AddToWishList addToWishList) {
//
//        // 將前端傳入的日期 原 ISO 8601型態 轉為 Timestamp型態
//        Timestamp rentalFavTime = Timestamp.from(Instant.parse(addToWishList.getRentalFavTime()));
//        addToWishList.setRentalFavTime(rentalFavTime.toString());
//
//        Rental rental =  rentalRepository.findByRentalNo(addToWishList.getRentalNo());
//
//        Map<String, String> map = new HashMap<>();
//        map.put("rentalNo", String.valueOf(addToWishList.getRentalNo()));
//        map.put("rentalCatNo", String.valueOf(rental.getRentalCategory().getRentalCatNo()));
//        map.put("rentalName", rental.getRentalName());
//        map.put("rentalPrice", String.valueOf(rental.getRentalPrice()));
//        map.put("rentalDesPrice", String.valueOf(rental.getRentalCategory().getRentalDesPrice()));
//        map.put("rentalSize", String.valueOf(rental.getRentalSize()));
//        map.put("rentalColor", rental.getRentalColor());
//        map.put("rentalInfo", rental.getRentalInfo());
//        map.put("rentalStat", String.valueOf(rental.getRentalStat()));
//        rentalMyFAVService.addToWishList(addToWishList.getMemNo(), map);
//        return ResponseEntity.status(HttpStatus.CREATED).body(rental.getRentalName());
//    }
    
    



    //處理單筆修改(依rentalNo)
//    @PostMapping("getOneUpdate")
//    public String getOneUpdate(@RequestParam("rentalNo") String rentalNo, @RequestParam("memNo") String memNo, ModelMap model) {
//
//        List<RentalMyFavorite> rentalMyFavorite = rentalMyFAVService.findByRentalRentalNoAndMemberMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//        return "/frontEnd/rentalmyfavorite/updateRentalMyFAV";
//    }

//
//
//    // 處理修改資料
//    @PostMapping("updateRentalMyFAV")
//    public String updateRentalMyFAV(@Valid RentalMyFavorite rentalMyFavorite,
//                                    @RequestParam("rentalNo") String rentalNo,
//                                    @RequestParam("memNo") String memNo, BindingResult result, ModelMap model) {
//
//        //如果沒有在最愛清單內，返回原Update頁面
//        if (rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo)) == null) {
//            return "/frontEnd/rentalmyfavorite/updateRentalMyFAV";
//        }
//
//        //驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
//        if (result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//            }
//            return "/frontEnd/rentalmyfavorite/updateRentalMyFAV";
//        }
//        // 將資料添加到 ModelMap 中
//        rentalMyFAVService.updateRentalFav(rentalMyFavorite);
////        List<RentalMyFavorite> rentalMyFavorite = rentalMyFAVService.findByRentalRentalNoAndMemberMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//        return "/frontEnd/rentalmyfavorite/listOneRentalMyFAV";
//    }
//
//
//
//
//
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
     * @return Member所有資料庫
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

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceMemNoData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalNo 對應的RentalMyFavorite資料庫
     */
    @ModelAttribute("rentalNoData")
    protected RentalMyFavorite referenceRentalNoData(@RequestParam(value = "rentalNo", required = false) Integer rentalNo) {

        if (rentalNo != null) {
            RentalMyFavorite list = rentalMyFAVService.findByRentalNo(rentalNo);
            return list;
        }
        return null;
    }

}


