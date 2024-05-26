package com.yu.rentalmyfavorite.controller;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.google.gson.Gson;
import com.roger.member.entity.Member;
import com.roger.member.service.impl.MemberServiceImpl;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalcategory.entity.RentalCategory;
import com.yu.rentalcategory.service.RentalCategoryServiceImpl;
import com.yu.rentalmyfavorite.dto.AddToWishList;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping("/frontend/rentalmyfavorite") //對應資料夾路徑
public class RentalMyFavoriteFrontEnd {

    @Autowired  // 自動裝配
    private RentalMyFavoriteServiceImpl rentalMyFAVService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;
    @Autowired
    private RentalServiceImpl rentalService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private MyCouponService myCouponSvc;

    /**
     * 確認會員是否登入。
     * 從資料庫獲取會員的願望清單。
     * 使用Redis從會話中獲取商品詳細資料並設置會話屬性。
     * 返回包含會員和願望清單數據的視圖。
     */
    // 瀏覽全部最愛清單頁面 (前台)
    @GetMapping("/myRentalFAV")
    public String myRentalFAV(ModelMap model, HttpSession session) {

        // 檢查用戶是否登錄，重定向到登錄頁面
        Member myData = (Member) session.getAttribute("loginsuccess");
        if (myData == null) {
            return "redirect:/frontend/member/loginMember";
        }
        Integer memNo = myData.getMemNo();
        System.out.println("你目前的會員編號：" + memNo);

        List<AddToWishList> addToWishData = rentalMyFAVService.getWishFromRedis(memNo);
        System.out.println("1、你目前的addToWishData清單內容：" + addToWishData);

        // 遍歷 addToWishData，將資料存儲到 session 中
        for (AddToWishList wish : addToWishData) {

            Integer myRentalNo = wish.getRentalNo();

            Rental rental = rentalService.getOneRental(myRentalNo);
            session.setAttribute("rentalName" + myRentalNo, rental.getRentalName());
            session.setAttribute("rentalSize" + myRentalNo, rental.getRentalSize());

            // 查詢rentalCategory表格中的rentalDesPrice屬性
            RentalCategory rentalCategory = rentalCategoryService.findByCatNo(myRentalNo);
            if (rentalCategory != null) {
                session.setAttribute("rentalDesPrice" + myRentalNo, rentalCategory.getRentalDesPrice().toString());
            }

            System.out.println("已取得內容 --> " + "myRentalNo： " + myRentalNo + "RentalName： " + rental.getRentalName());
            System.out.println("已取得內容 --> " + "myRentalNo： " + myRentalNo + "rentalSize： " + rental.getRentalSize());
            assert rentalCategory != null; //測試
            System.out.println("已取得內容 --> " + "myRentalNo： " + myRentalNo + "rentalSize： " + rentalCategory.getRentalDesPrice().toString());

            System.out.println("================================================================================");
        }

        List<MyCoupon> list = myCouponSvc.getAllMyCouponMem(myData.getMemNo());
        System.out.println(list);
        List<MyCoupon> showMyCoupon = new ArrayList<>();
        for (MyCoupon mycoupons : list) {
            if (mycoupons.getCoupUsedStat() == 0) {
                showMyCoupon.add(mycoupons);
            }
        }
        int myCouponQTY = showMyCoupon.size();
        model.addAttribute("myCouponQTY", myCouponQTY);


        System.out.println("你要傳送的資料 --> " + "myCouponQTY： " + myCouponQTY);
        System.out.println("你要傳送的資料 --> " + "myData： " + myData);
        System.out.println("你要傳送的資料 --> " + "addToWishData： " + addToWishData);

        model.addAttribute("myData", myData);  //會員資訊
        model.addAttribute("addToWishData", addToWishData); //最愛清單
        return "/frontend/rentalmyfavorite/myRentalFAV";
    }


    /**
     * 確認會員是否登入。
     * 取得會員編號並創建一個新的願望清單項目。
     * 新增項目到資料庫和Redis。
     */
    // 處理新增最愛清單
    @PostMapping("/addWish")
    @ResponseBody
    public Map<String, String> addWish(@RequestParam("rentalNo") String rentalNo,
                                       ModelMap model, HttpSession session) {

        Map<String, String> response = new HashMap<>();

//        //使用Gson對象的toJson方法，將response轉換為JSON格式
//        Gson gson = new Gson();
//        String abc = gson.toJson(response);

        // 從 HTTP 會話中獲取當前已登入的會員資料
        Member myData = (Member) session.getAttribute("loginsuccess");
        System.out.println("抓到myData：" + myData);
        // 如果會員未登錄，返回錯誤信息
        if (myData == null) {
            response.put("message", "請登入會員再進行操作");
            return response;
        }

        try {
            // 取得會員編號
            Integer memNo = myData.getMemNo();
            System.out.println("抓到memNo：" + memNo);

            AddToWishList addToWishList = new AddToWishList();
            addToWishList.setMemNo(memNo); // 匯入會員編號
            addToWishList.setRentalNo(Integer.valueOf(rentalNo)); // 匯入租借品編號
            rentalMyFAVService.addRentalFav(addToWishList);
            System.out.println("抓到wishDetails (我是去redis)：" + addToWishList);

            List<AddToWishList> rentalMyFAVs = rentalMyFAVService.getWishFromRedis(myData.getMemNo());
            model.addAttribute("rentalMyFAVs", rentalMyFAVs);
            session.setAttribute("rentalMyFAVs", rentalMyFAVs);
            response.put("message", "Success");

            System.out.println("抓到rentalMyFAVs：" + rentalMyFAVs);

        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 確認會員是否登入。
     * 從Redis刪除指定的願望清單項目。
     * 更新並返回會員的願望清單和操作結果。
     */
    // 處理刪除最愛清單
    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam("rentalNo") Integer rentalNo,
                                      HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // 從 HTTP 會話中獲取當前已登入的會員資料
        Member myData = (Member) session.getAttribute("loginsuccess");

        // 如果會員未登錄，返回錯誤信息
        if (myData == null) {
            response.put("message", "User not logged in");
            return response;
        }

        try {
            // 取得會員編號
            Integer memNo = myData.getMemNo();

            rentalMyFAVService.deleteWish(memNo, rentalNo); // 從 Redis 刪除
            List<AddToWishList> addToWishList = rentalMyFAVService.getWishFromRedis(memNo); // 更新清單
            session.setAttribute("addToWishList", addToWishList);
            response.put("addToWishList", addToWishList);
            response.put("message", "Success");

        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

//    /**
//     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
//     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return RentalMyFavorite所有資料庫
//     */
//    @ModelAttribute("rentalMyFAVList")
//    protected List<RentalMyFavorite> referenceListData() {
//        return rentalMyFAVService.findAll();
//    }
//
//    /**
//     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
//     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return rental所有資料庫
//     */
//    @ModelAttribute("rentalListData") //取出rental資料庫
//    protected List<Rental> referenceListDataRental() {
//        List<Rental> rentalList = rentalService.findAll();
//        return rentalList; //取得Rental列表
//    }
//
//    /**
//     * 提供所有租借品資料列表供視圖渲染使用。
//     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
//     * referenceMapData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return Member所有資料庫
//     */
//    @ModelAttribute("memberListData")
//    protected List<Member> referenceListDataMember() {
//        List<Member> memberList = memberService.findAll();
//        return memberList;
//    }
//
//    /**
//     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
//     * referenceData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return 與rentalNo & memNo 對應的RentalMyFavorite資料庫
//     */
//    @ModelAttribute("rentalMyFAVData")
//    protected RentalMyFavorite referenceData(@RequestParam(value = "rentalNo", required = false) Integer rentalNo,
//                                             @RequestParam(value = "memNo", required = false) Integer memNo) {
//
//        if (rentalNo != null && memNo != null) {
//            RentalMyFavorite list = rentalMyFAVService.findByRentalNoAndMemNo(rentalNo, memNo);
//            return list; //取得RentalMyFavorite列表
//        }
//        return null;
//    }
//
//    /**
//     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
//     * referenceMemNoData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
//     *
//     * @return 與rentalNo 對應的RentalMyFavorite資料庫
//     */
//    @ModelAttribute("rentalNoData")
//    protected RentalMyFavorite referenceRentalNoData(@RequestParam(value = "rentalNo", required = false) Integer rentalNo) {
//
//        if (rentalNo != null) {
//            RentalMyFavorite list = rentalMyFAVService.findByRentalNo(rentalNo);
//            return list;
//        }
//        return null;
//    }

}


