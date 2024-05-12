package com.filter.backend;

import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.Impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.ren.util.Constants.*;
import static com.ren.util.Validator.validateURL;

/**
 * 此為權限過濾器，目前設計為將新增、修改、刪除等資料庫操作的權限控管，
 * 過濾使用者階級使其不能訪問，view則是針對導覽設定Thymeleaf標籤控制畫面
 * 過濾的url Pattern如下:
 * 1.backend/xxx/addYyy
 * 2.update/xxx/updateYyy
 * 3.delete/xxx/deleteYyy
 */
@Component
@Order(THIRD_ORDER)
public class FunctionFilter extends HttpFilter {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

//        String requestURI = req.getRequestURI();
//        HttpSession session = req.getSession();
//        LoginState loginState = null;
//
//        // 非靜態資源與登入相關網頁 && 已登入
//        if (!validateURL(requestURI) && (loginState = (LoginState) session.getAttribute("loginState")) != null) {
//            System.out.println("被FunctionFilter過濾的" + requestURI);
//            // 獲取職位編號，後續用來校對職位
//            // 職位 1.老闆 2.經理 3.正職員工 4.打工仔
//            Integer titleNo = loginState.getTitleNo();
//            // 1.老闆可以做到CRUD
//            // 2.經理可以做到CRU
//            // 3.正職員工可以做到CR
//            // 4.只能查詢，打工仔的悲歌
//            switch (titleNo) {
//                case PARTJOB -> {
//                    req.getRequestDispatcher(requestURI).forward(req, res);
//                case FULLTIME -> ;
//                case MANAGER -> ;
//                case BOSS -> ;
//            }
//
//
//        }

        chain.doFilter(req, res);
    }
}
