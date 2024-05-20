package com.roger.columnarticle.controller;

import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/frontend/columnarticle")
public class ColumnArticleControllerFrontEnd {

    @Autowired
    ColumnArticleService columnArticleService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NoticeService noticeService;

    /**
     * 前往專欄文章的網頁
     */
    @GetMapping("/listAllColumnArticle")
    public String listAllColumnArticle(Model model) {
        return "frontend/columnarticle/listAllColumnArticle";
    }

    /**
     * 提供所有專欄文章資料列表供視圖渲染使用。
     * 此方法使用`@ModelAttribute`註解，確保在處理請求時可用於視圖中的`columnArticleListData`屬性。
     * 該屬性是一個包含所有專欄文章的列表，由`columnArticleService.findAll()`方法獲取。
     *
     * @return 包含所有專欄文章的列表。
     */
    @ModelAttribute("columnArticleListData")
    protected List<ColumnArticle> referenceListData() {
        List<ColumnArticle> list = columnArticleService.findAll();
        return list;
    }

    /**
     * POST請求端點，用於對專欄文章進行點讚。
     *
     * @param memNo   會員編號。
     * @param artNo   專欄文章編號。
     * @param request HttpServletRequest 實例，用於檢查用戶登入狀態。
     * @return ResponseEntity<String>，如果操作成功則返回200 OK，並返回成功消息；如果用戶尚未登入則返回401 Unauthorized錯誤；如果操作失敗則返回500 Internal Server Error錯誤。
     */
    @PostMapping("/like/{memNo}/{artNo}")
    @ResponseBody
    public ResponseEntity<String> likeColumnArticle(@PathVariable Integer memNo,
                                                    @PathVariable Integer artNo,
                                                    HttpServletRequest request,
                                                    HttpSession session) {
        // 檢查用戶是否已經登入
        if (request.getRequestURI() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未登入，請登入後即可點讚文章");
        }

        // 登入成功後進行點讚的邏輯處理
        boolean success = columnArticleService.likeColumnArticle(memNo, artNo);

        if (success) {
            // 將會員資料存入會話
            session.setAttribute("loginsuccess", memberService.findByNo(memNo));

            // 新增點讚的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent("你成功為專欄文章的第"+ String.valueOf(artNo) + "篇:" + columnArticleService.getOneColumnArticle(artNo).getArtTitle() + " 點讚，感謝妳的支持。");
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);
            noticeService.addNotice(newNotice);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(memberService.findByNo(memNo));
            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(memberService.findByNo(memNo).getMemNo());

            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            return ResponseEntity.ok("點讚成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("點讚失敗");
        }
    }

//    @GetMapping("/memberClickLikeData")
//    public String memberClickLikeData(ModelMap modelMap, HttpSession session) {
//
//        Member member = (Member) session.getAttribute("loginsuccess");
//
//    }
}
