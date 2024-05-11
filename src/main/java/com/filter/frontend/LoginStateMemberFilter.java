package com.filter.frontend;

import com.roger.member.dto.LoginStateMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static com.roger.util.Constants.*;
import static com.roger.util.Validator.validateURL;

/**
 * 用 Session 與 Redis 實現單點登入系統
 * 檢視會員登入狀態，以 SessionID 比對，如果 SessionID 與 Redis 資料庫內的不一樣，強制登出
 */
@Component
@Order(SECOND_ORDER)
public class LoginStateMemberFilter extends HttpFilter {

    @Autowired
    @Qualifier("integerLoginStateMember")
    private RedisTemplate<Integer, LoginStateMember> integerLoginStateRedisTemplateMember;


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();

        LoginStateMember loginStateMember = null;

        // 非靜態資源與登入相關的網頁 && 已登入
        if (!validateURL(requestURI) && (loginStateMember = (LoginStateMember) session.getAttribute("loginsuccess"))  != null) {
            System.out.println("被 LoginStateMemberFilter 過濾的: " + requestURI);
            // 如果當前 SessionID 與 Redis 資料庫內的 SessionID 不同，則代表為不同裝置登入，強制登出
            // 從當前 session 內的登入狀態獲得會員編號，使用會員編號查詢 Redis 資料庫登入狀態的 SessionID
            if (!session.getId().equals(integerLoginStateRedisTemplateMember.opsForValue().get(loginStateMember.getMemNo()).getJessionid())) {
                session.invalidate();
                System.out.println("被強制登出囉!!!!!!!");
                res.sendRedirect(loginPage);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
