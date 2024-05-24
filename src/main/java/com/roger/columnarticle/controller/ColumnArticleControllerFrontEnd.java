package com.roger.columnarticle.controller;

import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.articlecollection.service.ArticleCollectionService;
import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.service.ClickLikeService;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.columnreply.entity.ColumnReply;
import com.roger.columnreply.service.ColumnReplyService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/frontend/columnarticle")
public class ColumnArticleControllerFrontEnd {

    @Autowired
    ColumnArticleService columnArticleService;

    @Autowired
    ArticleCollectionService articleCollectionService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ClickLikeService clickLikeService;

    @Autowired
    private ColumnReplyService columnReplyService;

    /**
     * 前往專欄文章的網頁
     * 設定每頁顯示的文章數量為 8
     */
    @GetMapping("/listAllColumnArticle")
    public String listAllColumnArticle(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "8") Integer size,
                                       ModelMap modelMap,
                                       HttpSession session) {


        // Integer size = 1;

        if (session.getAttribute("loginsuccess") != null) {
            Member member = (Member) session.getAttribute("loginsuccess");
            List<ClickLike> clickLikeList = clickLikeService.getLikedArticlesByMember(member.getMemNo());
            List<ArticleCollection> articleCollectionList = articleCollectionService.getArticleCollectionByMember(member.getMemNo());

            // 獲取上架中的文章列表
            List<ColumnArticle> publishedArticles = columnArticleService.getPublishedArticles();

            if (!publishedArticles.isEmpty()) {
                ColumnArticle firstArticle = publishedArticles.get(0);
                // 現在您可以使用 firstArticle 來訪問第一個文章的屬性和方法
                // 例如：firstArticle.getTitle()，firstArticle.getContent()，等等
                session.setAttribute("onePublishedArticles", firstArticle.getArtNo());
            }

            // 創建一個包含點讚文章編號的列表
            List<Integer> likedArtNoList = new ArrayList<>();
            for (ClickLike clickLike : clickLikeList) {
                likedArtNoList.add(clickLike.getCompositeClickLike().getArtNo());
            }

            // 創建一個包含收藏文章編號的列表
            List<Integer> articleCollections = new ArrayList<>();
            for (ArticleCollection articleCollection : articleCollectionList) {
                articleCollections.add(articleCollection.getCompositeArticleCollection().getArtNo());
            }

            // 將 clickLikeList 和 likedArtNoList 存入 session
            session.setAttribute("clickLikeList", clickLikeList);
            session.setAttribute("likedArtNoList", likedArtNoList);

            // 將 articleCollections 和 articleCollection 存入 session
            session.setAttribute("articleCollectionList", articleCollectionList);
            session.setAttribute("articleCollections", articleCollections);

            // 一頁展示 8 篇專欄文章
            Pageable pageable = PageRequest.of(page, size);
            Page<ColumnArticle> columnArticlePage = columnArticleService.getAllByArtStatColumnArticles(pageable);
            modelMap.addAttribute("columnArticlePage", columnArticlePage);
            System.out.println("我有幾篇文章: " + columnArticlePage);

            // 將文章的點讚數添加到模型中
            Map<Integer, Integer> likeCounts = new HashMap<>();
            for (ColumnArticle columnArticle : columnArticlePage) {
                int artNo = columnArticle.getArtNo();
                int likeCount = clickLikeService.getLikeCountByArtNo(artNo);
                likeCounts.put(artNo, likeCount);
            }

            modelMap.addAttribute("likeCounts", likeCounts);

            return "frontend/columnarticle/listAllColumnArticle";

        } else {
            // 一頁展示 8 篇專欄文章
            Pageable pageable = PageRequest.of(page, size);
            Page<ColumnArticle> columnArticlePage = columnArticleService.getAllByArtStatColumnArticles(pageable);
            modelMap.addAttribute("columnArticlePage", columnArticlePage);
            System.out.println("我有幾篇文章: " + columnArticlePage);

            return "frontend/columnarticle/listAllColumnArticle";
        }
    }



    /**
     * 處理顯示單個專欄文章的 GET 請求。
     * <p>
     * 此方法根據提供的文章編號（artNo）檢索特定的專欄文章。
     * 同時，它還獲取該文章的回應數量，並將文章和回應數量
     * 添加到 ModelMap 中，以便在視圖中使用。
     *
     * @param artNo    專欄文章的文章編號。
     * @param modelMap 用於添加視圖所需屬性的 ModelMap 物件。
     * @return 顯示單個專欄文章的視圖名稱。
     */
    @GetMapping("/oneColumnArticle")
    public String oneColumnArticle(@RequestParam("artNo") Integer artNo,
                                   ModelMap modelMap,
                                   HttpSession session) {

        System.out.println(artNo);

        // 獲取上架中的文章列表
        List<ColumnArticle> publishedArticles = columnArticleService.getPublishedArticles();

        if (!publishedArticles.isEmpty()) {
            ColumnArticle firstArticle = publishedArticles.get(0);
            // 現在您可以使用 firstArticle 來訪問第一個文章的屬性和方法
            // 例如：firstArticle.getTitle()，firstArticle.getContent()，等等
            session.setAttribute("onePublishedArticles", firstArticle.getArtNo());
        }

        // 將文章列表添加到 ModelMap 中
        modelMap.addAttribute("publishedArticles", publishedArticles);

        // 找到當前文章在列表中的索引
        int currentIndex = publishedArticles.indexOf(columnArticleService.findColumnArticleByArtNo(artNo));
        System.out.println("我是第幾個索引: " + currentIndex);

        // 如果當前文章在列表中找到
        if (currentIndex != -1) {
            // 獲取上一篇文章
            ColumnArticle previousArticle = null;
            if (currentIndex > 0) {
                previousArticle = publishedArticles.get(currentIndex - 1);
            }

            // 獲取下一篇文章
            ColumnArticle nextArticle = null;
            if (currentIndex < publishedArticles.size() - 1) {
                nextArticle = publishedArticles.get(currentIndex + 1);
            }

            // 添加上一篇文章和下一篇文章到 ModelMap 中
            modelMap.addAttribute("previousArticle", previousArticle);
            modelMap.addAttribute("nextArticle", nextArticle);
        }


        // 從 columnArticleService 中獲取單個專欄文章
        ColumnArticle columnArticle = columnArticleService.getOneColumnArticle(artNo);

        // 從 columnReplyService 中獲取單個文章中的所有的文章回復
        List<ColumnReply> columnReplies = columnReplyService.getRepliesByArticleId(artNo);

        // 獲取文章的回應數量
        int responseCount = columnArticleService.getResponseCount(artNo);

        // 獲取單篇文章的按讚數量
        int likeCount = clickLikeService.getLikeCountByArtNo(artNo);

        // 將專欄文章放入模型中，以便在視圖中使用
        modelMap.addAttribute("columnArtice", columnArticle);
        modelMap.addAttribute("responseCount", responseCount + " Responses"); // 單篇文章的留言數量
        modelMap.addAttribute("columnReplies", columnReplies); // 單篇文章的所有文章回覆
        modelMap.addAttribute("likeCount", likeCount); // 單篇文章的點讚數量

        Member member = (Member) session.getAttribute("loginsuccess");

        if (member != null) {
            List<ClickLike> clickLikeList = clickLikeService.getLikedArticlesByMember(member.getMemNo());
            List<ArticleCollection> articleCollectionList = articleCollectionService.getArticleCollectionByMember(member.getMemNo());

            // 創建一個包含點讚文章編號的列表
            List<Integer> likedArtNoList = new ArrayList<>();
            for (ClickLike clickLike : clickLikeList) {
                likedArtNoList.add(clickLike.getCompositeClickLike().getArtNo());
            }

            // 創建一個包含收藏文章編號的列表
            List<Integer> articleCollections = new ArrayList<>();
            for (ArticleCollection articleCollection : articleCollectionList) {
                articleCollections.add(articleCollection.getCompositeArticleCollection().getArtNo());
            }

            // 將 clickLikeList 和 likedArtNoList 存入 session
            session.setAttribute("clickLikeList", clickLikeList);
            session.setAttribute("likedArtNoList", likedArtNoList);

            // 將 articleCollections 和 articleCollection 存入 session
            session.setAttribute("articleCollectionList", articleCollectionList);
            session.setAttribute("articleCollections", articleCollections);
        }

        // 將 columnReplies 存入 session
        session.setAttribute("columnReplies", columnReplies);

        // 返回單個專欄文章的頁面
        return "/frontend/columnarticle/singleColumnArticle";
    }

    @GetMapping("/{artNo}/responseCount")
    public ResponseEntity<Integer> getResponseCount(@PathVariable Integer artNo) {
        int responseCount = columnArticleService.getResponseCount(artNo);
        return new ResponseEntity<>(responseCount, HttpStatus.OK);
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
     * @param memNo 會員編號。
     * @param artNo 專欄文章編號。
     * @return ResponseEntity<String>，如果操作成功則返回200 OK，並返回成功消息；如果用戶尚未登入則返回401 Unauthorized錯誤；如果操作失敗則返回500 Internal Server Error錯誤。
     */
    @PostMapping("/like/{memNo}/{artNo}")
    @ResponseBody
    public ResponseEntity<String> likeColumnArticle(@PathVariable Integer memNo,
                                                    @PathVariable Integer artNo,
                                                    HttpSession session) {
        // 檢查用戶是否已經登入
        if (session.getAttribute("loginsuccess") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未登入，請登入後即可收藏文章");
        }

        // 登入成功後進行點讚的邏輯處理
        boolean success = clickLikeService.likeColumnArticle(memNo, artNo);


        if (success) {
            // 將會員資料存入會話
            session.setAttribute("loginsuccess", memberService.findByNo(memNo));

            // 新增點讚的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent("你成功為專欄文章的第" + String.valueOf(artNo) + "篇:" + columnArticleService.getOneColumnArticle(artNo).getArtTitle() + " 點讚，感謝妳的支持。");
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

    /**
     * POST 請求端點，用於取消對專欄文章的點讚。
     *
     * @param memNo   會員編號。
     * @param artNo   專欄文章編號。
     * @param request HttpServletRequest 對象，用於獲取 HTTP 請求相關信息。
     * @return ResponseEntity<String>，如果操作成功則返回 200 OK，並返回成功消息；如果用戶尚未登入則返回 401 Unauthorized 錯誤；
     * 如果操作失敗則返回 500 Internal Server Error 錯誤。
     */
    @PostMapping("/unlike/{memNo}/{artNo}")
    @ResponseBody
    public ResponseEntity<String> unlikeColumnArticle(@PathVariable Integer memNo,
                                                      @PathVariable Integer artNo,
                                                      HttpServletRequest request,
                                                      HttpSession session) {
        // 檢查用戶是否已經登入
        if (session.getAttribute("loginsuccess") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未登入，請登入後再試");
        }

        // 登入成功後進行取消點讚的邏輯處理
        boolean success = clickLikeService.unlikeArticle(memNo, artNo);

        if (success) {
            // 將會員資料存入會話
            session.setAttribute("loginsuccess", memberService.findByNo(memNo));

            // 新增點讚的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent("你取消了專欄文章的第" + String.valueOf(artNo) + "篇:" + columnArticleService.getOneColumnArticle(artNo).getArtTitle() + " 的點讚。");
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);
            noticeService.addNotice(newNotice);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(memberService.findByNo(memNo));
            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(memberService.findByNo(memNo).getMemNo());

            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            return ResponseEntity.ok("已取消點讚");

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("取消點讚失敗");
        }
    }


    /**
     * POST請求端點，用於對專欄文章進行收藏。
     *
     * @param memNo 會員編號。
     * @param artNo 專欄文章編號。
     * @return ResponseEntity<String>，如果操作成功則返回 200 OK，並返回成功消息；如果會員尚未登入則返回 401 Unauthorized錯誤；如果操作失敗則返回 500 Internal Server Error錯誤。
     */
    @PostMapping("/columnArticleCollection/{memNo}/{artNo}")
    @ResponseBody
    public ResponseEntity<String> columnArticleCollection(@PathVariable Integer memNo,
                                                          @PathVariable Integer artNo,
                                                          HttpSession session) {

        // 檢查用戶是否已經登入
        if (session.getAttribute("loginsuccess") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未登入，請登入後即可收藏文章");
        }

        // 登入成功後進行點讚的邏輯處理
        boolean success = columnArticleService.columnArticleCollection(memNo, artNo);

        if (success) {
            // 將會員資料存入會話
            session.setAttribute("loginsuccess", memberService.findByNo(memNo));

            // 新增點讚的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent("你成功收藏專欄文章的第" + artNo + "篇:" + columnArticleService.getOneColumnArticle(artNo).getArtTitle() + " 收藏 ，感謝妳的支持。");
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);
            noticeService.addNotice(newNotice);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(memberService.findByNo(memNo));
            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(memNo);

            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            return ResponseEntity.ok("收藏成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("收藏失敗");
        }
    }

    /**
     * POST請求端點，用於對專欄文章進行取消收藏。
     *
     * @param memNo 會員編號。
     * @param artNo 專欄文章編號。
     * @return ResponseEntity<String>，如果操作成功則返回 200 OK，並返回成功消息；如果會員尚未登入則返回 401 Unauthorized錯誤；如果操作失敗則返回 500 Internal Server Error錯誤。
     */
    @PostMapping("/unColumnArticleCollection/{memNo}/{artNo}")
    @ResponseBody
    public ResponseEntity<String> unColumnArticleCollection(@PathVariable Integer memNo,
                                                            @PathVariable Integer artNo,
                                                            HttpSession session) {

        // 檢查用戶是否已經登入
        if (session.getAttribute("loginsuccess") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未登入，請登入後即可收藏文章");
        }

        // 登入成功後進行取消的邏輯處理
        boolean success = columnArticleService.unColumnArticleCollection(memNo, artNo);

        if (success) {
            // 將會員資料存入會話
            session.setAttribute("loginsuccess", memberService.findByNo(memNo));

            // 新增點讚的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent("你取消對專欄文章第" + artNo + "篇:" + columnArticleService.getOneColumnArticle(artNo).getArtTitle() + " 的收藏。");
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);
            noticeService.addNotice(newNotice);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(memberService.findByNo(memNo));
            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(memNo);

            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            return ResponseEntity.ok("該文章已經取消收藏");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("取消收藏失敗");
        }
    }

    @PostMapping("/submitCommit")
    @ResponseBody
    public ResponseEntity<String> submitCommit(@RequestParam("memNo") Integer memNo,
                                               @RequestParam("artNo") Integer artNo,
                                               @RequestParam("comContent") String comContent,
                                               HttpSession session) {
        try {
            // 將會員資料存入會話
            session.setAttribute("loginsuccess", memberService.findByNo(memNo));

            // 新增留言的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent("你成功留言了專欄文章的第" + artNo + "篇：" + columnArticleService.getOneColumnArticle(artNo).getArtTitle() + "，感謝你的支持，你的留言為:" + comContent);
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);
            noticeService.addNotice(newNotice);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(memberService.findByNo(memNo));
            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(memNo);

            // 將通知消息列表和未讀通知數量存入會話
            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            columnReplyService.submitComment(memNo, artNo, comContent);
            return ResponseEntity.ok("留言提交成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("留言提交失敗");
        }
    }

    /**
     * 根據關鍵字搜尋狀態為 "上架中" 的專欄文章，並將結果顯示在搜尋結果頁面。
     * 支持分頁顯示結果。
     *
     * @param keyword 搜尋關鍵字。
     * @param page    當前頁碼，默認為 0。
     * @param size    每頁顯示的文章數量，默認為 8。
     * @param modelMap 用於傳遞搜尋結果到視圖的模型對象。
     * @param session 當前 HTTP 會話。
     * @return 搜尋結果的視圖名稱。如果沒有找到任何符合條件的文章，返回空列表並顯示提醒訊息。
     */
    @GetMapping("/searchArticles")
    public String searchArticles(@RequestParam("keyword") String keyword,
                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "8") Integer size,
                                 ModelMap modelMap,
                                 HttpSession session) {
        if (session.getAttribute("loginsuccess") != null) {
            // 如果使用者已登入，取得使用者相關資料
            Member member = (Member) session.getAttribute("loginsuccess");
            List<ClickLike> clickLikeList = clickLikeService.getLikedArticlesByMember(member.getMemNo());
            List<ArticleCollection> articleCollectionList = articleCollectionService.getArticleCollectionByMember(member.getMemNo());

            // 創建一個包含點讚文章編號的列表
            List<Integer> likedArtNoList = new ArrayList<>();
            for (ClickLike clickLike : clickLikeList) {
                likedArtNoList.add(clickLike.getCompositeClickLike().getArtNo());
            }

            // 創建一個包含收藏文章編號的列表
            List<Integer> articleCollections = new ArrayList<>();
            for (ArticleCollection articleCollection : articleCollectionList) {
                articleCollections.add(articleCollection.getCompositeArticleCollection().getArtNo());
            }

            // 將 clickLikeList 和 likedArtNoList 存入 session
            session.setAttribute("clickLikeList", clickLikeList);
            session.setAttribute("likedArtNoList", likedArtNoList);

            // 將 articleCollections 和 articleCollection 存入 session
            session.setAttribute("articleCollectionList", articleCollectionList);
            session.setAttribute("articleCollections", articleCollections);
        }

        // 確保 page 不是負數
        page = Math.max(page, 0);
        Pageable pageable = PageRequest.of(page, size);
        Page<ColumnArticle> searchResults = columnArticleService.searchArticles(keyword, pageable);

        System.out.println("Search Keyword: " + keyword);
        System.out.println("Search Results: " + searchResults.getContent());

        if (searchResults.isEmpty()) {
            // 添加提醒訊息
            modelMap.addAttribute("message", "未找到任何符合條件的文章。");
            System.out.println("我有進來");
        }

        modelMap.addAttribute("columnArticlePage", searchResults);

        // 將文章的點讚數添加到模型中
        Map<Integer, Integer> likeCounts = new HashMap<>();
        for (ColumnArticle columnArticle : searchResults) {
            int artNo = columnArticle.getArtNo();
            int likeCount = clickLikeService.getLikeCountByArtNo(artNo);
            likeCounts.put(artNo, likeCount);
        }

        modelMap.addAttribute("likeCounts", likeCounts);

        // 返回搜尋結果的視圖
        return "frontend/columnarticle/listAllColumnArticle";
    }


}
