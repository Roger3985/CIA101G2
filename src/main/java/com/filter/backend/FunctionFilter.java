package com.filter.backend;

import com.ren.administrator.dto.LoginState;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.ren.util.Constants.*;
import static com.ren.util.Regex.*;
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

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        LoginState loginState = null;

        // 非靜態資源與登入相關網頁 && 已登入
        if (!validateURL(requestURI) && (loginState = (LoginState) session.getAttribute("loginState")) != null) {
            System.out.println("被FunctionFilter過濾的" + requestURI);
            // 獲取職位編號，後續用來校對職位
            // 職位 1.打工仔 2.正職員工 3.經理 4.老闆
            // 1.老闆可以做到CRUD
            // 2.經理可以做到CRU
            // 3.正職員工可以做到CR
            // 4.只能查詢，打工仔的悲歌
            Integer titleNo = loginState.getTitleNo();
            // 先確認來源url是屬於哪一種type的
            String url = "";
            if (addUrlRegex.matcher(requestURI).find()) {
                url = "ADD";
            } else if (updateUrlRegex.matcher(requestURI).find()) {
                url = "UPDATE";
            } else if (deleteUrlRegex.matcher(requestURI).find()) {
                url = "DELETE";
            }
            
            switch (url) {
                case "ADD":
                    // 職位沒有比打工仔還大
                    if (!(titleNo > PARTJOB)) {
                        reject(req, res);
                    }
                    break;
                case "UPDATE":
                    // 職位沒有比全職員工大
                    if (!(titleNo > FULLTIME)) {
                        reject(req, res);
                    }
                    break;
                case "DELETE":
                    // 職位沒有比經理大
                    if (!(titleNo > MANAGER)) {
                        reject(req, res);
                    }
                    break;    
            }
        }

        chain.doFilter(req, res);
    }

    /**
     * 將使用者Forward到原網頁
     *
     * @param req 用於獲得RequestUrl與Dispatcher，後續用來forward
     * @param res 後續用於forward
     * @throws ServletException forward時可能會拋出的異常
     * @throws IOException forward時可能會拋出的異常
     */
    private void reject(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(req.getRequestURI());
        dispatcher.forward(req, res);
        System.out.println("您還沒有權限!!!");
    }
}
