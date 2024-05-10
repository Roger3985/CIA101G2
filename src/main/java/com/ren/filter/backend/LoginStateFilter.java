package com.ren.filter.backend;

import com.ren.administrator.dto.LoginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.ren.util.Constants.SECOND_ORDER;
import static com.ren.util.Constants.loginPage;
import static com.ren.util.Validator.validateURL;

/**
 * 用Session與Redis實現單點登入系統
 * 檢視帳號登入狀態，以SessionID比對，如果SessionID與Redis資料庫內的不一樣，強制登出
 */
@Component
@Order(SECOND_ORDER)
public class LoginStateFilter extends HttpFilter {

    @Autowired
    @Qualifier("integerLoginState")
    private RedisTemplate<Integer, LoginState> itlRedisTemplate;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        LoginState loginState = null;
        // 非靜態資源與登入相關網頁 && 已登入
        if (!validateURL(requestURI) && (loginState = (LoginState) session.getAttribute("loginState")) != null) {
            System.out.println("被LoginStateFilter過濾的" + requestURI);
            // 如果當前SessionID與Redis資料庫內的SessionID不同，則代表為不同裝置登入，強制登出
            // 從當前session內的登入狀態獲得管理員編號，使用管理員編號查詢Redis資料庫登入狀態的SessionID
            if (!session.getId().equals(itlRedisTemplate.opsForValue().get(loginState.getAdmNo()).getJsessionid())) {
                session.invalidate();
                System.out.println("被強制登出囉");
                res.sendRedirect(loginPage);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}