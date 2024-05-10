package com.listener;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.service.AdministratorServiceImpl;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class LoginStateListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session好像建立囉!");
    }

    /**
     * 在Session關閉時，確認資料庫內的登入狀態是否正確
     *
     * @param se SessionListener預設傳入SessionEvent物件
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session結束");
//        // 從HttpSessionEvent中獲得Session物件
//        HttpSession session = se.getSession();
//        // 從Session裡取出administrator物件，確認登入與登出狀態
//        Administrator administrator = (Administrator) session.getAttribute("administrator");
//        AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//        Byte loginState = administrator.getAdmLogin();
//        Byte logoutState = administrator.getAdmLogout();
//        // 如果登入狀態為已登入 or 登出狀態為未登出
//        if (loginState != 0 || logoutState != 1) {
//            administrator.setAdmLogin(Byte.valueOf("0"));
//            administrator.setAdmLogout(Byte.valueOf("1"));
//            administratorSvc.updateAdministrator(administrator);
//        }
    }
}