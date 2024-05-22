package com.yu.rentalmyfavorite.controller;

import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.roger.member.service.impl.MemberServiceImpl;
import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontEnd/rentalmyfavorite") //對應資料夾路徑
public class RentalMyFavoriteFrontEnd {


    @Autowired  // 自動裝配
    private RentalMyFavoriteServiceImpl rentalMyFAVService;
    @Autowired
    private RentalServiceImpl rentalService;
    @Autowired
    private MemberServiceImpl memberService;


    //瀏覽全部最愛清單頁面 (前台)
    @GetMapping("/rentalFAVList")
    public String rentalFAVList() {
        return "/frontend/rentalmyfavorite/rentalFAVList";
    }


    //瀏覽全部最愛清單頁面 (前台)  -->最新
    @GetMapping("/myRentalFAV")
    public String myRentalFAV() {
        return "/frontend/rentalmyfavorite/myRentalFAV";
    }

///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 前往查看最愛清單的頁面。 (須加入會員)
     * 此方法處理 HTTP GET 請求到 '/frontend/member/memberData' URL 路徑，
     * 從會話中獲取當前已登入的會員資料並將其添加到 'ModelMap' 中。
     *
     * @param model 包含模型屬性的 'ModelMap'。
     * @param session session HTTP 會話物件，用來儲存和訪問當前已經登入的會員。
     * @return 要呈現的視圖名稱 "oneMember.html"。
     */
    @PostMapping("/myRentalFAV/{memNo}")
    public String showMemFAVList(@PathVariable(value = "memNo") Integer memNo,
                                 HttpSession session, ModelMap model){

        // 從 HTTP 會話中獲取當前已登入的會員資料
        Member myData = (Member) session.getAttribute("loginsuccess");

        // 如果會員未登錄，重定向到登錄頁面
        if (myData == null) {
            return "redirect:/frontend/member/loginMember";
        } else {
            // 從 Redis 取得最愛清單
            Map<Object, Object> memFAVList = rentalMyFAVService.getWishList(memNo);
            System.out.println("抓取:"+ memFAVList);
            model.addAttribute("memFAVList", memFAVList);  //此處須顯示已篩選的該會員清單
        }

        model.addAttribute("myData", myData);// 將會員資料放入model

        return "/frontend/rentalmyfavorite/rentalFAVList"; //轉交給List畫面做顯示
    }


    //    處理新增最愛清單
    @PostMapping("addToWishList")
    public ResponseEntity<?> addToWishList(@RequestBody RentalMyFavorite rentalMyFavorite) {

        try {
            // 將前端傳入的日期 原 ISO 8601型態 轉為 Timestamp型態
            Timestamp rentalFavTime = Timestamp.from(Instant.parse(rentalMyFavorite.getRentalFavTime().toString()));
            rentalMyFavorite.setRentalFavTime(rentalFavTime);

            // 新增品項至資料庫，資料會於
            rentalMyFAVService.addRentalFav(rentalMyFavorite);

            //品項加入redis中
//            rentalMyFAVService.addToWishList(rentalMyFavorite.getMemNo(), Map.of(
//                    "rentalNo", rentalMyFavorite.getRentalNo().toString(),
//                    "itemName", rentalMyFavorite.getItemName(),
//                    "rentalFavTime", rentalMyFavorite.getRentalFavTime().toString()
//            ));
            return ResponseEntity.status(HttpStatus.CREATED).body("已加入願望清單!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("加入失敗!");
        }
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


