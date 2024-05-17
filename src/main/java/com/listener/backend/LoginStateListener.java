package com.listener.backend;

import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.Impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class LoginStateListener implements HttpSessionListener {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session好像建立囉!");
    }

    /**
     * 將閒置的帳號登出:
     * 在Session關閉時，確認資料庫內的登入狀態是否正確，
     * 如果當前會話的SessionID與Redis資料庫內的SessionID相同，
     * 執行Service的登出方法。
     *
     * @param se SessionListener預設傳入SessionEvent物件
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session結束");
        // 從HttpSessionEvent中獲得Session物件
        HttpSession session = se.getSession();
        // 從Session裡取出登入狀態物件，確認登入與登出狀態
        Object objLoginState = session.getAttribute("loginState");
        // 確認是否有無登入
        if (objLoginState != null) {
            LoginState loginState = (LoginState) objLoginState;
            // 確認Redis資料庫有無登入狀態資料，如果沒有，代表非閒置狀態，已執行過登出
            if (administratorSvc.getFromRedis(loginState.getAdmNo()) != null) {
                // 如果當前SessionID與LoginState的SessionID相同，執行登出方法
                if (session.getId().equals(loginState.getJsessionid())) {
                    administratorSvc.logout(loginState);
                }
            }
        }

        // 最後將登入狀態移除Session，觸發AttributeListener的方法
        session.removeAttribute("loginState");
    }
}