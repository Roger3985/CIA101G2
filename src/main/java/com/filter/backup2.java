//package com.ren.filter;
//
//import com.Entity.Administrator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ren.administrator.dto.LoginStateMember;
//import org.springframework.data.redis.core.RedisTemplate;
//import redis.clients.jedis.Jedis;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//import static com.ren.util.Constants.loginPage;
//import static com.ren.util.Validator.validateURL;
//
///**
// * 檢視帳號登入狀態，以SessionID比對，如果SessionID與Redis資料庫內的不一樣，強制登出
// */
//@WebFilter("/backend/*")
//public class backup2 extends HttpFilter {
//
//    @Override
//    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        String requestURI = req.getRequestURI();
//        HttpSession session = req.getSession();
//        LoginStateMember loginState = null;
//        // 非靜態資源與登入相關網頁 && 已登入
//        System.out.println(session);
//        if (!validateURL(requestURI) && (loginState = (LoginStateMember) session.getAttribute("loginState")) != null) {
//            System.out.println("被LoginStateFilter過濾的" + requestURI);
//            // 如果當前SessionID與Redis資料庫內的SessionID不同，則代表為不同裝置登入，強制登出
//            Jedis jedis = null;
//            try {
//                jedis = new Jedis("localhost", 6379);
//                String json = jedis.get(loginState.getAdmNo().toString());
//                ObjectMapper jsonTransform = new ObjectMapper();
//                if (session.getId() != jsonTransform.readValue(json, LoginStateMember.class)
//                        .getJsessionid()){
//                    session.invalidate();
//                    System.out.println("被強制登出囉");
//                    res.sendRedirect(loginPage);
//                    return;
//                }
//            } finally {
//                if (jedis != null) {
//                    jedis.close();
//                }
//            }
//
//        }
//        chain.doFilter(req, res);
//    }
//}