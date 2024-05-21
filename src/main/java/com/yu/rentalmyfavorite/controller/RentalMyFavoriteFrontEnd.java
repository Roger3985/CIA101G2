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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

///////////////////////////////////////////////////////////////////////////////////////
    // (待議: 加入會員才可察看最愛清單)
    //若未登入，顯示會員登入畫面
    @PostMapping("/loginPage")
    public String login(@RequestParam String userId,
                        @RequestParam String admPwd,
                        @RequestParam Byte autoLogin,
                        HttpSession session, ModelMap model,
                        HttpServletRequest req, HttpServletResponse res){

        if (userId == null || userId.trim().equals("")){
            model.addAttribute("idError","請輸入用戶名稱");
            return "backend/login";
        }

        //判斷帳號是否已登入
        //若無登入，即導向登入頁面
        //若已登入，即可繼續操作
//        if (memberService.loginState())

        return userId;
    }
///////////////////////////////////////////////////////////////////////////////////////

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



    //    處理新增最愛清單
    @PostMapping("addToWish")
    public ResponseEntity<?> addToWish(@RequestBody RentalMyFavorite rentalMyFavorite) {

        try {
            // 將前端傳入的日期 原 ISO 8601型態 轉為 Timestamp型態
            Timestamp rentalFavTime = Timestamp.from(Instant.parse(rentalMyFavorite.getRentalFavTime().toString()));
            rentalMyFavorite.setRentalFavTime(rentalFavTime);

            // 新增品項至資料庫，資料會於
            rentalMyFAVService.addRentalFav(rentalMyFavorite);
            return ResponseEntity.status(HttpStatus.CREATED).body("已加入願望清單!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("加入失敗!");
        }
    }


//    //處理新增最愛清單 (在rentalShop瀏覽頁面加入最愛品項)
//    // 並將前端傳入的日期 原 ISO 8601型態 轉為 Timestamp型態
//    @PostMapping("addToWishList")
//    public String addToWishList(@RequestParam(value = "rentalNo", required = false) Integer rentalNo,
//                                @RequestParam(value = "memNo", required = false) Integer memNo,
//                                @RequestParam(value = "rentalFavTime",required = false) Timestamp rentalFavTime,
//                                ModelMap modelMap) {
//
//        Map<String, Object> map = new HashMap<>();
//
//        if (rentalNo != null) {
//            map.put("rentalNo", rentalNo);
//        } else if (memNo != null) {
//            map.put("memNo", memNo);
//        } else if (rentalFavTime != null) {
//            map.put("rentalFavTime", rentalFavTime);
//        }
//
//        List<RentalMyFavorite> rentalMyFAVList = rentalMyFAVService.searchRentalMyFAVs(map);
//        modelMap.addAttribute("rentalMyFAVList", rentalMyFAVList);
//        modelMap.addAttribute("search", "true");
//
//        return "/frontend/rental/rentalShop";
//    }



    //取得該會員的最愛清單 (於清單顯示畫面)
    @GetMapping("/rental/rentalFAVList/{memNo}")
    public String showMemFAVList(@PathVariable("memNo") Integer memNo, Model model) {

        //判斷如果會員不為空，取出該會員的最愛清單，並顯示於頁面
        if(memNo != null){
            List<RentalMyFavorite>  getFAVList = rentalMyFAVService.getFAVByMemNo(memNo);  //取出會員編號
            System.out.println("抓取:");
            model.addAttribute("getFAVList", getFAVList);  //此處須顯示已篩選的該會員清單
        }
        return "/frontend/rental/rentalFAVList"; //轉交給List畫面
    }



    //處理單筆修改(依rentalNo)
//    @PostMapping("getOneUpdate")
//    public String getOneUpdate(@RequestParam("rentalNo") String rentalNo, @RequestParam("memNo") String memNo, ModelMap model) {
//
//        List<RentalMyFavorite> rentalMyFavorite = rentalMyFAVService.findByRentalRentalNoAndMemberMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//        model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//        return "/frontEnd/rentalmyfavorite/updateRentalMyFAV";
//    }

    // 處理修改資料
//    @PostMapping("updateRentalMyFAV")
//    public String updateRentalMyFAV(RentalMyFavorite rentalMyFavorite,
//                                    @RequestParam("rentalNo") String rentalNo,
//                                    @RequestParam("memNo") String memNo, ModelMap model) {
//
//        //如果在清單內，可做修改
//        if (rentalMyFAVService.findByIdRentalNoAndIdMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo)) != null) {
//
//            //匯入修改資料
//            rentalMyFAVService.updateRentalFav(rentalMyFavorite);
//            //
//            List<RentalMyFavorite> rentalMyFavorite = rentalMyFAVService.findByRentalRentalNoAndMemberMemNo(Integer.valueOf(rentalNo),Integer.valueOf(memNo));
//            model.addAttribute("rentalMyFavorite", rentalMyFavorite);
//            return "/frontEnd/rentalmyfavorite/listOneRentalMyFAV";
//        }
//        return "/frontEnd/rentalmyfavorite/updateRentalMyFAV";
//    }



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


