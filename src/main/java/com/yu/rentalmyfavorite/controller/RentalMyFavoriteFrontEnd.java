package com.yu.rentalmyfavorite.controller;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.google.gson.Gson;
import com.roger.member.entity.Member;
import com.roger.member.service.impl.MemberServiceImpl;
import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalcategory.service.RentalCategoryServiceImpl;
import com.yu.rentalmyfavorite.dto.AddToWishList;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    private RentalServiceImpl rentalService;
    @Autowired
    private MyCouponService myCouponSvc;

    /**
     * 功能：查詢最愛清單
     *
     * 確認會員是否登入。
     * 從資料庫獲取會員的願望清單。
     * 使用Redis從會話中獲取商品詳細資料並設置會話屬性。
     * 返回包含會員和願望清單數據的視圖。
     */
    // 瀏覽全部最愛清單頁面 (前台)
    @GetMapping("/myRentalFAV")
    public String myRentalFAV(AddToWishList addToWishList,
                              ModelMap model, HttpSession session) {

        // 檢查用戶是否登錄，重定向到登錄頁面
        Member myData = (Member) session.getAttribute("loginsuccess");
        if (myData == null) {
            return "redirect:/frontend/member/loginMember";
        }

        Integer memNo = myData.getMemNo();  //取得目前的會員編號

        List<AddToWishList> addToWishData = rentalMyFAVService.getWishFromRedis(memNo);

        // 遍歷 addToWishData，將資料儲存到 session 中
        for (AddToWishList wish : addToWishData) {

            Integer myRentalNo = wish.getRentalNo();
            Rental rental = rentalService.getOneRental(myRentalNo);

            //資料存入session
            session.setAttribute("rentalName" + myRentalNo, rental.getRentalName());
            session.setAttribute("rentalSize" + myRentalNo, rental.getRentalSize());
            session.setAttribute("rentalPrice" + myRentalNo, rental.getRentalPrice());
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

        model.addAttribute("myData", myData);  //會員資訊
        model.addAttribute("addToWishData", addToWishData); //最愛清單
        return "/frontend/rentalmyfavorite/myRentalFAV";
    }


    /**
     * 功能：處理新增最愛清單
     *
     * 確認會員是否登入。
     * 取得會員編號並創建一個新的願望清單項目。
     * 新增項目到資料庫和Redis。
     */
    @PostMapping("/addWish")
    @ResponseBody
    public String addWish(@RequestParam("rentalNo") String rentalNo,
                          ModelMap model, HttpSession session) {

        Map<String, String> response = new HashMap<>();

        // 使用 Gson 對象的 toJson 方法，將 response 轉換為 JSON 格式
        Gson gson = new Gson();

        // 從 HTTP 會話中獲取當前已登入的會員資料
        Member myData = (Member) session.getAttribute("loginsuccess");
        System.out.println("抓到 myData：" + myData);

        // 如果會員未登錄，返回錯誤信息
        if (myData == null) {
            response.put("message", "請登入會員再進行操作");
            return gson.toJson(response);
        }

        try {
            // 取得會員編號
            Integer memNo = myData.getMemNo();
            System.out.println("抓到 memNo：" + memNo);

            // 建立 AddToWishList 對象
            AddToWishList addToWishList = new AddToWishList();
            addToWishList.setMemNo(memNo); // 匯入會員編號
            addToWishList.setRentalNo(Integer.valueOf(rentalNo)); // 匯入租借品編號

            // 使用 addRentalFav 方法加入到 redis 資料庫
            rentalMyFAVService.addRentalFav(addToWishList);
            System.out.println("抓到 wishDetails (我是去 redis)：" + addToWishList);

            // 取得會員的我的最愛清單，更新到 model 和 session
            List<AddToWishList> rentalMyFAVs = rentalMyFAVService.getWishFromRedis(myData.getMemNo());
            model.addAttribute("rentalMyFAVs", rentalMyFAVs);
            session.setAttribute("rentalMyFAVs", rentalMyFAVs);
            response.put("message", "Success");

            System.out.println("抓到 rentalMyFAVs：" + rentalMyFAVs);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        // 使用 Gson 將 response Map 轉換為 JSON 字符串
        String jsonResponse = gson.toJson(response);
        return jsonResponse;
    }


    /**
     * 功能：刪除最愛清單
     *
     * 確認會員是否登入。
     * 從Redis刪除指定的願望清單項目。
     * 更新並返回會員的願望清單和操作結果。
     */
    @PostMapping("/delete")
    @ResponseBody
    public List<AddToWishList> delete(@RequestParam("rentalNo") Integer rentalNo,
                                      @RequestParam("memNo") Integer memNo,
                                      HttpSession session) {

        rentalMyFAVService.deleteWish(memNo, rentalNo); // 從 Redis 刪除
        List<AddToWishList> addToWishList = rentalMyFAVService.getWishFromRedis(memNo); // 更新清單
        session.setAttribute("memNo", memNo);
        System.out.println("成功刪除");

        return addToWishList;
    }

}


