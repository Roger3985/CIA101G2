package com.filter.backend;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;

import static com.ren.util.Constants.*;
import static com.ren.util.Validator.validateURL;

/**
 * 用於確認後台員工登入狀態的過濾器，
 * 主要有二功能:
 * 1.確認登入狀態，如果未登入則導向登入
 * 2.如果使用者有選擇自動登入功能，則執行自動登入
 */
@Component
@Order(FIRST_ORDER)
public class AutoLoginFilter extends HttpFilter {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    // String To Integer
    @Autowired
    @Qualifier("cookieStrInt")
    private RedisTemplate<String, Integer> stiRedisTemplate;

    // <String, String>
    @Autowired
    @Qualifier("cookieStrStr")
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String requestURI = req.getRequestURI();
        // 跳過靜態資源與登入相關網頁
        if (!validateURL(requestURI)) {
            System.out.println("被AutoLoginFilter過濾的" + requestURI);

            // 獲得session物件，如果沒有則創建一個(使用false會有已造訪網頁時session物件仍未建立的情況)
            HttpSession session = req.getSession();
            LoginState loginState = null;

            System.out.println(session);
            // 搜尋使用者cookie確認是否有登入相關資訊的cookie
            Optional<Cookie> userCookie = Optional.ofNullable(req.getCookies())
                    .flatMap(this::userCookie);

            // (有無登入(有無LoginState) && 有無設定自動登入(有無autoLogin Cookie))
            // 因考慮使用者可能會關閉Cookie與不使用自動登入功能，且優先判斷cookie有較高的安全性問題，將autoLogin的確認放在 or 判斷式的最後面，
            // 優先確認使用者登入狀態
            // 如果都沒有，導向登入頁面
            if ((loginState = (LoginState) session.getAttribute("loginState")) == null && !userCookie.isPresent()) {
//                System.out.println("來看看是誰被過濾, session:" + session + ", loginState:" + loginState + ", cookie:" + userCookie);
                System.out.println("還沒登入哦!");
                String encodedMessage = URLEncoder.encode("您還沒登入哦! 麻煩請先回到首頁登入。", StandardCharsets.UTF_8.toString());
                res.sendRedirect(errorRedirect + encodedMessage);
                return;
            }
            // 第一次登入(自動登入)
            if (loginState == null) {
                // 獲取Cookie值
                String cookieValue = userCookie.get().getValue();
                // 透過Cookie值獲得AdmNo
                Integer admNo = stiRedisTemplate.opsForValue().get(cookieValue);
                // 透過使用者編號找到管理員
                Administrator administrator = administratorSvc.getOneAdministrator(admNo);
                // 使用Service的登入方法，獲得登入狀態DTO
                administratorSvc.login(administrator, session);
            }
        }
        chain.doFilter(req, res);
    }

    /**
     * 搜尋使用者是否已有登入的記錄
     *
     * @param cookies 傳入使用者的cookies，並於後續使用Stream的filter方法，
     *                於方法內呼叫自定義check()方法將含使用者登入資訊的cookies過濾，
     *                並使用findFirst()方法找出第一個符合條件的Cookie
     * @return 返回Optional物件，如果沒有找到符合條件的cookie，則返回含null的Optional物件
     */
    private Optional<Cookie> userCookie(Cookie[] cookies) {
        return Stream.of(cookies)
                .filter(cookie -> check(cookie))
                .findFirst();
    }

    /**
     * 判斷是否有登入資訊
     * 1.從cookie裡面先尋找是否有名字為"autoLogin"的cookie
     * 2.其值為在登入時自動生成的亂數，分別存入Cookie("autoLogin", random)、Redis資料庫(random, admNo)
     * 3.透過Redis的搜尋方法找到相對應的key
     *
     * @param cookie 傳入Cookie物件，並呼叫Cookie的getter方法確認使否有符合登入資訊的Cookie
     * @return 如果兩者都符合則返回true，沒有則返回false
     */
    private boolean check(Cookie cookie) {
        return "autoLogin".equals(cookie.getName()) // 找尋名為"autoLogin"的Cookie
                && stringRedisTemplate.opsForValue() // 以Cookie的Value確認Redis資料庫內有無存入此登入相關資料
                .get(cookie.getValue()) != null;
    }

}