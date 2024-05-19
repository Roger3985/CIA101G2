package com.filter.backend;

import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
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
 * 用Session與Redis實現單點登入系統
 * 檢視帳號登入狀態，以SessionID比對，如果SessionID與Redis資料庫內的不一樣，強制登出
 * 強制登出執行方式:
 * 1.將使用者的自動登入cookie移除，使其無法透過自動登入的方式直接登入
 * 2.註銷Session
 * 之後將使用者導至登入畫面
 * 不使用Service的登出方法，因為會更改到其他登入用戶的登入狀態，只針對該使用者裝置作處置
 */
@Component
@Order(SECOND_ORDER)
public class LoginStateFilter extends HttpFilter {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    @Autowired
    @Qualifier("cookieStrStr")
    private StringRedisTemplate cookieRedisTemplate;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        LoginState loginState = null;
        // 非靜態資源與登入相關網頁 && 已登入
        if (!validateURL(requestURI) && (loginState = (LoginState) session.getAttribute("loginState")) != null) {
            System.out.println("被LoginStateFilter過濾的" + requestURI);
            // 有時會發生Redis資料庫異常的問題，在這邊處理
            if ((loginState = administratorSvc.getFromRedis(loginState.getAdmNo())) == null) {
                System.out.println("發生異常，請麻煩重新登入");
                session.invalidate();
                // 將錯誤訊息傳到前端
//                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                res.setContentType("application/json");
//                res.getWriter().write("{\"error\": \"發生異常，請重新登入。\"}");
//                res.getWriter().flush();
                String encodedMessage = URLEncoder.encode("發生異常，麻煩請您重新登入。", StandardCharsets.UTF_8.toString());
                res.sendRedirect(errorRedirect + encodedMessage);
                return;
            }
            // 如果當前SessionID與Redis資料庫內的SessionID不同，則代表為不同裝置登入，強制登出
            // 從當前session內的登入狀態獲得管理員編號，使用管理員編號查詢Redis資料庫登入狀態的SessionID
            if (!session.getId().equals(loginState.getJsessionid())) {

                // 搜尋使用者cookie確認是否有登入相關資訊的cookie
                Optional<Cookie> userCookie = Optional.ofNullable(req.getCookies())
                        .flatMap(this::userCookie);

                if (userCookie.isPresent()) {
                    // 從Optional中獲取Cookie物件
                    Cookie deleteCookie = userCookie.get();
                    // 將Cookie期限設為0,並新增到BackendIndexController新增時的路徑，即可註銷Cookie
                    deleteCookie.setMaxAge(0);
                    deleteCookie.setPath(req.getContextPath() + "/backend");
                    res.addCookie(deleteCookie);
                    // 刪除Redis資料庫內的cookie資料
                    cookieRedisTemplate.delete(deleteCookie.getName());
                }

                // session.removeAttribute("loginState"); // 因Session被註銷，所以不用移除Session內的值
                session.invalidate();
                System.out.println("被強制登出囉");
                // 將錯誤訊息傳到前端響應
//                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                res.setContentType("application/json");
//                res.getWriter().write("{\"error\": \"偵測到您已在其他裝置登入，請重新登入。\"}");
//                res.getWriter().flush();
                // 重導回首頁
                String encodedMessage = URLEncoder.encode("偵測到您已在其他裝置登入，麻煩請您重新登入。", StandardCharsets.UTF_8.toString());
                res.sendRedirect(errorRedirect + encodedMessage);
                return;
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
     * 3.在LoginState過濾器中，主要用途為找到"autoLogin"的Cookie，並把他註銷，讓使用者重新登入
     *
     * @param cookie 傳入Cookie物件，並呼叫Cookie的getter方法確認使否有符合登入資訊的Cookie
     * @return 如果兩者都符合則返回true，沒有則返回false
     */
    private boolean check(Cookie cookie) {
        return "autoLogin".equals(cookie.getName());
    }

}