//package com.roger.member.controller;
//
//import com.roger.member.entity.Member;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import javax.servlet.http.HttpSession;
//
//@Slf4j
//@Controller
//public class FrontEndIndexController {
//
////    /**
////     * 處理根 URL ("/") 的 GET 請求。
////     * <p>
////     * 前台頁面尚未完成。
////     * </p>
////     *
////     * @return 要渲染的視圖名稱「oneMember」。
////     */
////    @GetMapping("/")
////    public String frontEndIndex() {
////        // TODO: 前台頁面尚未完成
////        return "frontend/index2";
////    }
//
//    /**
//     * 處理根 URL ("/") 的 GET 請求。
//     * <p>
//     * 前台頁面尚未完成。
//     * </p>
//     *
//     * @param session 用於檢查用戶登入狀態的 HttpSession。
//     * @param model   用於將消息傳遞給視圖的 Model。
//     * @return 要渲染的視圖名稱「frontend/index2」或「login」。
//     */
//    @GetMapping("/")
//    public String frontEndIndex(HttpSession session, Model model) {
//
//        // 檢查會員是否登入
//        Member loginMember = (Member) session.getAttribute("loginsuccess");
//
//        // 如果會員未登入
//        if (loginMember == null) {
//            // 將消息添加到模型中
//            model.addAttribute("message", "尚未登入，無法使用");
//
////            // 可以重定向到登入頁面
////            return "redirect:/frontend/member/loginMember";
//
//             // 或返回一個特定的視圖顯示未登入消息
//             return "frontend/notLoggedIn";
//        }
//
//        // TODO 前台頁面尚未完成
//        // 如果會員已經登入，繼續顯示前端頁面
//        return "frontend/index2";
//    }
//
//}
