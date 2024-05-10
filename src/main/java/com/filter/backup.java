//package com.ren.filter;
//
//import com.Entity.Administrator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ren.administrator.dto.LoginState;
//import com.ren.administrator.service.AdministratorServiceImpl;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import redis.clients.jedis.Jedis;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.*;
//import java.io.IOException;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static com.ren.util.Constants.loginPage;
//import static com.ren.util.Validator.validateURL;
//
///**
// * 用於確認後台員工登入狀態的過濾器，
// * 主要有二功能:
// * 1.確認登入狀態，如果未登入則導向登入
// * 2.確認自動登入功能
// */
//@WebFilter(urlPatterns = "/backend/*")
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class backup extends HttpFilter {
//
//    @Override
//    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        String requestURI = req.getRequestURI();
//        // 跳過靜態資源與登入相關網頁
//        if (!validateURL(requestURI)) {
//            System.out.println("被AutoLoginFilter過濾的" + requestURI);
//
//            // 獲得session物件，如果沒有則創建一個(使用false會有已造訪網頁時session物件仍未建立的情況)
//            HttpSession session = req.getSession();
//            LoginState loginState = null;
//
//            System.out.println(session);
//            // 搜尋使用者cookie確認是否有管理員相關資訊
//            Optional<Cookie> userCookie = Optional.ofNullable(req.getCookies())
//                    .flatMap(this::userCookie);
//
//            /**
//             * ((有無造訪網頁(有無session) || 有無登入(有無administrator)) || 有無設定自動登入(有無autoLogin Cookie))
//             * 因考慮使用者可能會關閉Cookie與不使用自動登入功能，且優先判斷cookie有較高的安全性問題，將autoLogin的確認放在 or 判斷式的最後面，
//             * 優先確認使用者登入狀態
//             * 如果都沒有，導向登入頁面
//             */
//            if ((loginState = (LoginState) session.getAttribute("loginState")) == null && !userCookie.isPresent()) {
//                System.out.println("來看看是誰被過濾, session:" + session + ", loginState:" + loginState + ", cookie:" + userCookie);
//                System.out.println("還沒登入哦!");
//                res.sendRedirect(loginPage);
//                return;
//            }
//            // 第一次登入(自動登入)
//            if (loginState == null){
//                // 因為filter不能加Component，使用Jedis來對Redis資料庫做操作
//                Jedis jedis = null;
//                String cookieValue = userCookie.get().getValue();
//                String redisValue = ""; // 避免空值，設定為空字串
//                try {
//                    // 取得Redis資料庫連線
//                    jedis = new Jedis("localhost", 6379);
//                    // 獲取該Cookie的使用者編號
//                    redisValue = jedis.get(cookieValue).replaceAll("\"", "");
//                    AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//                    // 透過使用者編號找到管理員
//                    Administrator administrator = administratorSvc.getOneAdministrator(Integer.valueOf(redisValue));
//                    // 使用Service的登入方法，獲得登入狀態DTO
//                    loginState = administratorSvc.login(administrator, session);
//                    // 將登入狀態存入Session
//                    session.setAttribute("loginState", loginState);
//                    // 將登入狀態存入Redis資料庫，之後可於Session做核對(於LoginStateFilter核對)
//                    ObjectMapper jsonTransform = new ObjectMapper();
//                    jedis.set(administrator.getAdmNo().toString(), jsonTransform.writeValueAsString(loginState));
//
//                } finally {
//                    if (jedis != null) {
//                        jedis.close();
//                    }
//                }
//            }
//        }
//        chain.doFilter(req, res);
//    }
//
//    /**
//     * 搜尋使用者是否已有登入的記錄
//     *
//     * @param cookies 傳入使用者的cookies，並於後續使用Stream的filter方法，
//     *                於方法內呼叫自定義check()方法將含使用者登入資訊的cookies過濾，
//     *                並使用findFirst()方法找出第一個符合條件的Cookie
//     * @return 返回Optional物件，如果沒有找到符合條件的cookie，則返回含null的Optional物件
//     */
//    private Optional<Cookie> userCookie(Cookie[] cookies) {
//        return Stream.of(cookies)
//                .filter(cookie -> check(cookie))
//                .findFirst();
//    }
//
//    /**
//     * 判斷是否有登入資訊
//     * 1.從cookie裡面先尋找是否有名字為"autoLogin"的cookie
//     * 2.其值為在登入時自動生成的亂數，分別存入Cookie("autoLogin", random)、Redis資料庫(random, admNo.toString())
//     * 3.透過Redis的搜尋方法找到相對應的key
//     *
//     * @param cookie 傳入Cookie物件，並呼叫Cookie的getter方法確認使否有符合登入資訊的Cookie
//     * @return 如果兩者都符合則返回true，沒有則返回false
//     */
//    public boolean check(Cookie cookie) {
//        Jedis jedis = null;
//        String cookieValue = cookie.getValue();
//        String redisValue = ""; // 避免空值，設定為空字串
//        try {
//            jedis = new Jedis("localhost", 6379);
//            redisValue = jedis.get(cookieValue);
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//
//        return "autoLogin".equals(cookie.getName()) && !redisValue.equals("");
//    }
//
//}