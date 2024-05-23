package com.roger.notice.controller;

import com.chihyun.mycoupon.entity.MyCoupon;
import com.chihyun.mycoupon.model.MyCouponService;
import com.roger.member.entity.Member;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.impl.NoticeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend/notice")
public class NoticeControllerFrontEnd {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Autowired
    private MyCouponService myCouponSvc;

    /**
     * 前往查看未讀訊息
     * @return
     */
    @GetMapping("/newMemberNoticeData")
    public String newMemberNoticeData() {
        return "/frontend/notice/newMemberNotice";
    }


    @PostMapping("/newMemberNotice")
    @ResponseBody
    public ResponseEntity<String> newMemberNotice(@RequestBody Map<String, String> requestBody,
                                                  HttpSession session,
                                                  Model model) {
        String motNo = requestBody.get("motNo");

        // 從 HTTP 會話中獲取當前已登入的會員跟通知資料（如果需要的話）
         Member member = (Member) session.getAttribute("loginsuccess");

        // 根據 motNo 從數據庫中獲取新的通知消息
         Notice newNoticeDate = noticeService.getOneNotice(Integer.valueOf(motNo));
         newNoticeDate.setNotStat((byte) 1);

        // 從會話中獲取當前會員的未讀通知列表
        int unreadNotices = (int) session.getAttribute("unreadNoticeCount");

        // 更新通知狀態（如果需要的話）
         noticeService.updateNotice(newNoticeDate);

        // 更新未讀通知消息數量
        if (unreadNotices != 0) {
            // 確認通知後未讀消息的數量減一
            int unreadNoticeCount = unreadNotices - 1;
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);
        }

        // 更新新的通知消息 session（如果需要的話）
         session.setAttribute("notice", newNoticeDate);

        // 返回成功的響應
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/somePage")
    public String getUnreadNotice(Model model,
                                  HttpSession session) {
        // 獲取未讀取通知的數量
//        int unreadNoticeCount = noticeService.getUnreadNoticeCount();

//        session.setAttribute("unreadNoticeCount", unreadNoticeCount);

        return "somePage";
    }


    /**
     * 前往查看已讀訊息
     * @return
     */
    @GetMapping("/oldMemberNoticeData")
    public String oldMemberNoticeData() {
        return "/frontend/notice/oldMemberNotice";
    }

    /**
     * 前往查看全部通知消息(包括已讀跟未讀)
     * @return
     */
    @GetMapping("/memberNoticeData")
    public String memberNoticeData(ModelMap modelMap,
                                   HttpSession session) {

        // 從 HTTP 會話中獲取當前已登入的會員跟通知資料
        Member myData = (Member) session.getAttribute("loginsuccess");
        List<Notice> notice = (List<Notice>) session.getAttribute("noticeList");

        // 如果會員未登錄，重定向到登錄頁面
        if (myData == null) {
            return "redirect:/frontend/member/loginMember";
        }

        if (myData != null) {
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(myData.getMemNo());
            modelMap.addAttribute("noticeList", noticeList);
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

        // 將會員資料添加到模型中
        modelMap.addAttribute("myData", myData);
        modelMap.addAttribute("notice", notice);
        modelMap.addAttribute("myCouponQTY", myCouponQTY);


        return "/frontend/notice/oneMemberNotice";
    }

    /**
     * 接收一個 POST 請求，用於刪除通知消息。
     * @param motNo 要刪除的通知消息的 ID
     * @param session HttpSession 物件，用於獲取當前已登入的會員
     * @return ResponseEntity 包含刪除操作的結果和相應的狀態碼
     */
    @PostMapping("/deleteNotice/{motNo}")
    @ResponseBody
    public ResponseEntity<String> deleteNotice(@PathVariable Integer motNo,
                                               HttpSession session) {
        // 從 HTTP 會話中獲取當前已登入的會員
        Member member = (Member) session.getAttribute("loginsuccess");

        if (member == null) {
            return new ResponseEntity<>("未登錄", HttpStatus.UNAUTHORIZED);
        }

        // 根據 motNo 從數據庫中刪除通知消息
        noticeService.deleteNoticeByMotNo(motNo);

        // 返回成功的響應
        return new ResponseEntity<>("通知已成功刪除", HttpStatus.OK);
    }


    @PostMapping("/markAllAsRead")
    @ResponseBody
    public ResponseEntity<String> markAllAsRead(HttpSession session) {
        // 從 HTTP 會話中獲取已登入的會員
        Member member = (Member) session.getAttribute("loginsuccess");

        if (member == null) {
            return new ResponseEntity<>("未登錄", HttpStatus.UNAUTHORIZED);
        }

        // 獲取該會員的所有通知
        List<Notice> noticeList = noticeService.getAll();

        if (noticeList.isEmpty()) {
            return new ResponseEntity<>("無通知消息", HttpStatus.NOT_FOUND);
        }

        // 將所有的通知訊息標記為已讀
        for (Notice notice : noticeList) {
            notice.setNotStat((byte) 1);
            noticeService.updateNotice(notice);
        }

        // 更新會話中的未讀通知訊息
        session.setAttribute("unreadNoticeCount", 0);

        // 返回成功的響應
        return new ResponseEntity<>("所有未讀訊息已標記為已讀", HttpStatus.OK);
    }

    /**
     * 控制器方法，用於刪除登錄會員的所有已讀通知。
     *
     * @param session 包含已登錄會員的HTTP會話。
     * @return ResponseEntity，帶有描述操作狀態的消息。
     */
    @PostMapping("/deleteAllNotices")
    @ResponseBody
    public ResponseEntity<String> deleteAllNotices(HttpSession session) {
        // 從HTTP會話中獲取已登錄的會員
        Member member = (Member) session.getAttribute("loginsuccess");

        // 檢查會員是否已登錄
        if (member == null) {
            return new ResponseEntity<>("未登錄", HttpStatus.UNAUTHORIZED);
        }

        // 獲取該會員的所有已讀通知
        List<Notice> unReadNoticeList = noticeService.findNoticesByMemberMemNoAndNotStat(member.getMemNo(), (byte) 1);

        // 檢查是否有任何通知
        if (unReadNoticeList.isEmpty()) {
            return new ResponseEntity<>("無已讀通知消息", HttpStatus.NOT_FOUND);
        }

        // 刪除所有通知
        for (Notice notice : unReadNoticeList) {
            noticeService.deleteNoticeByMotNo(notice.getMotNo()); // 假設 noticeService.deleteNotice() 通過ID刪除通知
        }

//        // 更新會話中的未讀通知計數
//        session.setAttribute("unreadNoticeCount", 0);

        // 返回成功的響應
        return new ResponseEntity<>("所有通知消息已刪除", HttpStatus.OK);
    }

}
