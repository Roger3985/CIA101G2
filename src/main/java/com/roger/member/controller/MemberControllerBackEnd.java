package com.roger.member.controller;

import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/backend/member")
public class MemberControllerBackEnd {

    @Autowired
    private MemberService memberService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 前往後台管理首頁
    @GetMapping("/frontendEndIndex")
    public String frontendEndIndex() {
        return "frontend/index";
    }

    /**
     * 顯示所有會員列表的頁面。
     * 此方法處理 HTTP GET 請求到 '/backend/member/memberlist' URL 路徑，
     * 返回呈現所有會員列表的視圖名稱。
     *
     * @param modelMap 包含模型屬性的 'ModelMap'。
     * @return 要呈現的視圖名稱 "listAllMember4.html"。
     */
    @GetMapping("/memberlist")
    public String showList(ModelMap modelMap) {
        return "backend/member/listAllMember";
    }

    /**
     * 提供所有會員資料列表供視圖渲染使用。
     * 此方法使用`@ModelAttribute`註解，確保在處理請求時可用於視圖中的`memListData`屬性。
     * 該屬性是一個包含所有會員的列表，由`memberService.findAll()`方法獲取。
     *
     * @return 包含所有會員的列表。
     */
    @ModelAttribute("memListData")
    protected List<Member> referenceListData() {
        List<Member> list = memberService.findAll();
        return list;
    }

    /**
     * 停權指定的會員帳號並立即登出該會員。
     * 此方法接受會員編號 (`memNo`) 作為參數，並使用 `memberService.banMem()` 方法進行會員帳號停權處理。
     * 停權成功後，該方法將會員編號存儲在 Redis 中，標記為 "noFun"。
     * 此外，該方法會立即終止該會員的會話，從而強制登出該會員。
     *
     * @param memNo 要停權的會員編號。
     * @param session HTTP 會話 (`HttpSession`) 用於終止該會員的會話。
     * @return 重定向到會員列表頁面。
     */
    @PostMapping("/banMember")
    public String banMember(@ModelAttribute("memNo") String memNo, HttpSession session) {
        // 停權會員
        memberService.banMem(Integer.valueOf(memNo));
        redisTemplate.opsForValue().set("noFun:members" + memNo, memNo);

        // 終止該會員的會話
        session.invalidate();

        // 重定向到會員列表頁面
        return "redirect:/backend/member/memberlist";
    }

    /**
     * 復權指定會員帳號並恢復會員的會話。
     * 此方法通過接受會員編號(`memNo`)作為參數，使用`memberService.findByNo()`方法查找會員對象，
     * 然後將該會員的帳號狀態設置為已驗證(狀態值為1)，並使用`memberService.edit()`方法更新會員信息。
     *
     * @param memNo 要復權的會員編號。
     * @param session 要恢復的 HTTP 會話。
     * @return 重定向到會員列表頁面。
     */
    @PostMapping("/reMember")
    public String reMember(@ModelAttribute("memNo") String memNo, HttpSession session) {
        // 復權會員
        Member member = memberService.findByNo(Integer.valueOf(memNo));
        member.setMemStat(Byte.valueOf("1"));
        memberService.edit(member);

        // 檢查 Redis 中的鍵是否存在，如果存在則刪除
        String key = "noFun:members" + memNo;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }

        // 恢復會員會話
        session.setAttribute("loginsuccess", member);

        // 重定向到會員列表頁面
        return "redirect:/backend/member/memberlist";
    }

    /**
     * 圖片處理方法。
     * 根據請求參數中的會員編號（memno）查找會員的圖片資料，並將圖片作為響應輸出。
     *
     * @param memNo 會員編號（請求參數）。
     * @param req 請求物件。
     * @param res 響應物件。
     * @throws IOException 當輸出流出現異常時拋出。
     */
    @GetMapping("/picture")
    public void dBGifReader(@RequestParam("memno") String memNo, HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        // 設定響應內容類型為圖片
        res.setContentType("image/jpg");
        ServletOutputStream out = res.getOutputStream();

        try {
            // 從會員服務中查找會員的圖片資料並輸出
            out.write(memberService.findByNo(Integer.valueOf(memNo)).getMemPic());
        } catch (Exception e) {
            // 如果發生異常，輸出預設的佈景圖片
            byte[] buf = java.util.Base64.getDecoder().decode(
                    "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");
            out.write(buf);
        }
    }

}
