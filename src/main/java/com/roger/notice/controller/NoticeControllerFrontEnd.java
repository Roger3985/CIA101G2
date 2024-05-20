package com.roger.notice.controller;

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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend/notice")
public class NoticeControllerFrontEnd {

    @Autowired
    private NoticeServiceImpl noticeService;


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

        // 將會員資料添加到模型中
        modelMap.addAttribute("myData", myData);
        modelMap.addAttribute("notice", notice);


        return "/frontend/notice/oneMemberNotice";
    }





}
