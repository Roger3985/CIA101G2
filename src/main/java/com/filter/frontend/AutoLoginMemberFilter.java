package com.filter.frontend;

import com.roger.member.dto.LoginStateMember;
import com.roger.member.entity.Member;
import com.roger.member.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static com.roger.util.Constants.FIRST_ORDER;
import static com.roger.util.Constants.loginPage;
import static com.roger.util.Validator.validateURL;

/**
 * 用於確認前台會員登入狀態的過濾器
 * 主要有兩大功能:
 * 1. 確認登入狀態
 * 2. 確認自動登入的功能
 */
@Component
@Order(FIRST_ORDER)
public class AutoLoginMemberFilter extends HttpFilter {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    @Qualifier("stringInteger")
    private RedisTemplate<String, Integer> stringIntegerRedisTemplate;

    @Autowired
    @Qualifier("integerLoginStateMember")
    private RedisTemplate<Integer, LoginStateMember> integerLoginStateRedisTemplateMember;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String requestURI = req.getRequestURI();

        // 跳過靜態資源與登入相關的網頁
        if (!validateURL(requestURI)) {
            System.out.println("被 AutoLoginMemberFilter 過濾的: " + requestURI);

            // 獲得 session 物件，如果沒有則創建一個 (使用 false 會有已造訪網站時的 session 物件仍未建立的情況)
            HttpSession session = req.getSession();
            LoginStateMember loginStateMember = null;

            System.out.println(session);

            // 搜尋使用者 cookie 確認是否有會員相關資訊
            Optional<Cookie> userCookie = Optional.ofNullable(req.getCookies())
                    .flatMap(this::userCookie);

            /**
             * ((有無造訪網頁(有無 session) || 有無登入(有無 successlogin)) || 有無設定自動登入(有無 autoLoginMember Cookie))
             * 因考慮會員可能會關閉 Cookie 與不使用自動登入功能，且優先判斷 cookie 有較高的安全性問題，將 autoLoginMember 的確認放在 or 判斷式的最後面，
             * 優先確認會員登入狀態
             * 如果都沒有，導向登入頁面
             */
            if ((loginStateMember = (LoginStateMember)  session.getAttribute("loginsuccess")) == null && !userCookie.isPresent()) {
                System.out.println("你還沒有登入喔");
                res.sendRedirect(loginPage);
                return;
            }

            // 第一次登入(自動登入)
            if (loginStateMember == null) {

                // 獲取 Cookie 值
                String cookieValue = userCookie.get().getValue();

                // 透過 Cookie 值獲得 memNo;
                Integer memNo = stringIntegerRedisTemplate.opsForValue().get(cookieValue);

                // 透過會員編號找到會員
                Member member = memberService.getMemberByMemNo(memNo);

                // 使用 Service 的登入方法，獲取登入狀態 DTO
                loginStateMember = memberService.loginState(member, session);

                // 將登入狀態存入 Session
                session.setAttribute("loginsuccess", loginStateMember);

                integerLoginStateRedisTemplateMember.opsForValue().set(memNo, loginStateMember);
            }
        }
        chain.doFilter(req, res);
    }

    /**
     * 搜尋會員是否已有登入的記錄。
     *
     * @param cookies 傳入會員的 cookies，並於後續使用 Stream 的 filter 方法，
     *                於方法內呼叫自定義 check() 方法將含使用者登入資訊的 cookies 過濾，
     *                並使用 findFirst() 方法找出第一個符合條件的 Cookie。
     * @return 返回 Optional 物件，如果沒有找到符合條件的 cookie，則返回含 null 的 Optional 物件。
     */
    private Optional<Cookie> userCookie(Cookie[] cookies) {
        return Stream.of(cookies)
                .filter(cookie -> check(cookie))
                .findFirst();
    }

    /**
     * 判斷是否有登入資訊。
     * 1.從 cookie 裡面先尋找是否有名字為 "autoLoginMember" 的 cookie。
     * 2.其值為在登入時自動生成的亂數，分別存入 Cookie ("autoLoginMember", random)、Redis資料庫(random, memNo.toString())。
     * 3.透過 Redis 的搜尋方法找到相對應的 key。
     *
     * @param cookie 傳入 Cookie 物件，並呼叫 Cookie 的 getter 方法確認使否有符合登入資訊的 Cookie。
     * @return 如果兩者都符合則返回 true，沒有則返回 false。
     */
    public boolean check(Cookie cookie) {
        // 檢查給定的 Cookie 是否為自動登錄會員 Cookie，並且該值在 Redis 中存在
        return "autoLoginMember".equals(cookie.getName())
                && stringRedisTemplate.opsForValue()
                .get(cookie.getValue()) != null;
    }
}
