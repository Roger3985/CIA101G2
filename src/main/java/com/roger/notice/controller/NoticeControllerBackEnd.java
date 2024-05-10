package com.roger.notice.controller;

import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.entity.uniqueAnnotation.Create;
import com.roger.notice.service.impl.NoticeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/backend/notice")
public class NoticeControllerBackEnd {

    @Autowired
    public NoticeServiceImpl noticeService;

    @Autowired
    public MemberService memberService;

    /**
     * 前往查詢單個會員通知的頁面。
     */
    @GetMapping("/getNoticeOneData")
    public String getNoticeOneData(@ModelAttribute("notice") Notice notice,
                                   ModelMap modelMap) {
        // 返回視圖名稱
        return "backend/notice/selectNotice";
    }

    /**
     * 通過會員編號查詢會員的所有通知，並將通知列表添加到模型中。
     * 返回視圖名稱，用於渲染查詢結果的頁面。
     *
     * @param memNo 會員編號，用於查詢該會員的所有通知。
     * @param modelMap 用於在頁面中存儲和傳遞數據。
     * @return 視圖名稱 "backend/notice/selectOnePage" 用於渲染查詢結果的頁面。
     */
    @GetMapping("/getOnAny")
    public String getOnAny(@RequestParam(value = "memNo", required = false) Integer memNo,
                           @RequestParam(value = "notContent", required = false) String notContent,
                           @RequestParam(value = "notTime",required = false) Timestamp notTime,
                           @RequestParam(value = "notStat", required = false) Byte notStat,
                           ModelMap modelMap) {

        Map<String, Object> map = new HashMap<>();

        if (memNo != null) {
            map.put("memNo", memNo);
        } else if (notContent != null) {
            map.put("notContent", notContent);
        } else if (notTime != null) {
            map.put("notTime", notTime);
        } else if (notStat != null) {
            map.put("notStat", notStat);
        }

        // 根據會員編號 memNo 獲取通知列表
        List<Notice> noticeList = noticeService.getByAttributes(map);

        // 將通知列表添加到模型中
        modelMap.addAttribute("noticeList", noticeList);
        modelMap.addAttribute("getOnAny", "true");

        return "backend/notice/selectNotice";

    }

//    /**
//     * 這個方法根據給定的會員編號 `memNo` 查詢該會員的所有通知，並以 JSON 格式返回通知列表。
//     *
//     * @param memNo 會員編號，用於查詢該會員的所有通知。
//     * @return 一個包含通知列表的 `ResponseEntity` 對象，如果查詢成功，狀態碼為 200。
//     *         如果發生錯誤，狀態碼將設置為 500。
//     * @throws Exception 當在查詢過程中發生錯誤時拋出。
//     */
//    @GetMapping("/getNoticeByMemNo")
//    @ResponseBody
//    public ResponseEntity<List<Notice>> getNoticeByMemNo(@RequestParam("memNo") Integer memNo) {
//        System.out.println(memNo);
//        try {
//            // 獲取會員的所有通知
//            List<Notice> notices = noticeService.findNoticesByMemberMemNo(memNo);
//
//            // 返回通知列表，狀態碼為 200
//            return ResponseEntity.ok(notices);
//        } catch (Exception e) {
//            // 處理錯誤
//            // 你可以返回一個錯誤響應
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }



    /**
     * 前往新增頁面。
     *
     * @param modelMap 視圖模型，用於在頁面中儲存和傳遞資料。
     * @return 返回 "backend/notice/addNotice" 視圖名稱，用於渲染新增通知頁面。
     */
    @GetMapping("/addNoticeData")
    public String addNoticeData(ModelMap modelMap) {
        modelMap.addAttribute("notice", new Notice());
        return "backend/notice/addNotice";
    }

    /**
     * 處理新增通知的請求。
     *
     * @param notice 接收新增通知的資料物件。
     * @param result 用於驗證輸入資料的結果物件。
     * @param modelMap 視圖模型，用於在頁面儲存和傳遞資料。
     * @return 如果存在錯誤，返回 "backend/notice/addNotice" 視圖名稱繼續顯示新增通知頁面；如果成功添加通知，則重定向到通知列表頁面。
     * @throws IOException 如果在處理過程中發生 I/O 錯誤。
     */
    @PostMapping("/addNotice")
    public String addNotice(@Validated(Create.class) Notice notice,
                            BindingResult result,
                            ModelMap modelMap) throws IOException {


        if (result.hasErrors()) {
            return "backend/notice/addNotice";
        }

        noticeService.addNotice(notice);
        return "redirect:/backend/notice/noticelist";
    }

    // 全部json到前端
    @GetMapping("/getAllNotice")
    public ResponseEntity<List<Notice>> getAllNotices() {
        Notice notice = new Notice();
        List<Notice> notices = noticeService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(notices);
    }

    /**
     * 顯示所有通知的列表視圖頁面。
     *
     * @param modelMap 視圖模型，用於在頁面中存儲和傳遞數據。
     * @return 返回 "backend/notice/listAllNotice" 視圖名稱，用於渲染通知列表頁面。
     */
    @GetMapping("/noticelist")
    public String showlist(ModelMap modelMap) {
        return "backend/notice/listAllNotice";
    }

    /**
     * 前往修改頁面。
     * 這個方法接受 `ModelMap` 和 `memNo` 參數作為輸入。通過 `noticeService.getOneNotice()` 查找對應於
     * `memNo` 的 `Notice` 實例，然後將該實例添加到模型中。
     *
     * @param modelMap 視圖模型，用於在頁面中儲存和傳遞資料。
     * @param motNo    會員通知編號，作為查詢的參數。
     * @return 返回 "backend/notice/updateNotice" 視圖名稱，用於渲染修改通知頁面。
     */
    @GetMapping("updateNoticeData")
    public String updateNoticeData(@ModelAttribute("motNo") Integer motNo,
                                   // @Validated(Create.class) Notice notice,
                                    ModelMap modelMap
                                   // BindingResult result
    ) {
        System.out.println(motNo);
        Notice oldData = noticeService.getOneNotice(motNo);
        // System.out.println("test" + oldData);
        // modelMap.addAttribute("notice", notice);
        modelMap.addAttribute("data", oldData);
        return "backend/notice/updateNotice";
    }

    /**
     * 修改現有通知。
     * 接受 `Notice` 實例 `notice`，如果該實例包含任何驗證錯誤，則返回修改通知頁面。
     * 如果驗證成功，則通過 `noticeService.updateNotice()` 方法更新通知。
     * 更新後將新通知數據添加到 `ModelMap`，並返回通知列表頁面。
     *
     * @param notice    要更新的 `Notice` 實例，包含要更新的通知的相關信息。
     * @param result    用於驗證輸入資料的結果對象。
     * @param modelMap  視圖模型，用於在頁面中儲存和傳遞資料。
     * @return 如果存在驗證錯誤，返回 "backend/notice/updateNotice" 視圖名稱；如果成功更新通知，則返回通知列表頁面。
     * @throws IOException 如果在過程中發生 I/O 錯誤。
     */
    @PostMapping("/updateNotice")
    public String updateNotice(@ModelAttribute("data") @Validated(Create.class) Notice notice,
                               BindingResult result,
                               ModelMap modelMap) throws IOException {

        System.out.println(notice);
        System.out.println(result.getErrorCount());
        System.out.println(result.getFieldErrorCount());

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "backend/notice/updateNotice";
        }

        Notice newData = noticeService.updateNotice(notice);
        modelMap.addAttribute("successData", newData);
        System.out.println(newData);
        return "backend/notice/listAllNotice";
    }

//    /**
//     * 刪除指定的會員通知
//     *
//     */
//    @PostMapping("/delete")
//    public String deleteNotice(@RequestParam("motNo") String motNo) {
//        boolean deleted = noticeService.deleteNotice(motNo);
//        if (deleted) {
//
//        }
//    }


    /**
     * 設置查詢全部通知列表的模型屬性。
     *
     * @return 返回查詢全部通知列表的模型屬性。
     */
    @ModelAttribute("noticeListData")
    protected List<Notice> referenceListData() {
        List<Notice> list = noticeService.getAll();
        return list;
    }

    @ModelAttribute("memberListData")
    protected List<Member> referenceListData2() {
        List<Member> list = memberService.findAll();
        return list;
    }

//    /**
//     * 使用 @ModelAttribute 創建一個方法來收集重複的通知內容
//     */
//    @ModelAttribute("duplicateNotices")
//    public List<String> findDuplicateNotices(@ModelAttribute("notices") List<Notice> notices) {
//        // 使用 Set 集合來追蹤已經出現過的內容
//        Set<String> seen = new HashSet<>();
//        // 使用 List 集合來收集重複的通知內容
//        List<String> duplicates = new ArrayList<>();
//
//        // 遍歷通知列表
//        for (Notice notice : notices) {
//            String content = notice.getNotContent();
//            // 如果在 seen 集合中已經存在，則表示重複內容
//            if (seen.contains(content)) {
//                duplicates.add(content);
//            } else {
//                // 如果在 seen 集合中不存在，則將其添加進去
//                seen.add(content);
//            }
//        }

        // 返回收集到的重複內容
//        return duplicates;
//    }

}
