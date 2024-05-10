//package com.ren.admauthority.controller;
//
//import com.ren.admauthority.service.AdmAuthorityServiceImpl;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//
//public class AdmAuthorityServlet extends HttpServlet {
//
//    @Override
//    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        doPost(req, res);
//    }
//
//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//
//        req.setCharacterEncoding("UTF-8");
//        String action = req.getParameter("action");
//
//        if ("getOne_For_Display".equals(action)) {
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//            String str = req.getParameter("titleNo");
//            if (str == null || (str.trim()).length() == 0) {
//                errorMsgs.add("請輸入商品類別編號");
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/admauthority/select_admAuthority.jsp");
//                failureView.forward(req, res);
//                return;// 程式中斷
//            }
//
//            Integer titleNo = null;
//            try {
//                titleNo = Integer.valueOf(str);
//            } catch (Exception e) {
//                errorMsgs.add("商品類別編號格式不正確");
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/admauthority/select_admAuthority.jsp");
//                failureView.forward(req, res);
//                return;// 程式中斷
//            }
//
//            /*************************** 2.開始查詢資料 *****************************************/
//            AdmAuthorityServiceImpl admAuthoritySvc = new AdmAuthorityServiceImpl();
//
//            AdmAuthority admAuthority = admAuthoritySvc.getOneAdmAuthority(titleNo);
//            // 引用類型的屬性在未附值時預設為null
//            if (admAuthority == null) {
//                errorMsgs.add("查無資料");
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/admauthority/select_admAuthority.jsp");
//                failureView.forward(req, res);
//                return;// 程式中斷
//            }
//
//            /*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
//            req.setAttribute("admAuthority", admAuthority);
//            String url = "/admauthority/listOneAdmAuthority.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);
//            successView.forward(req, res);
//        }
//
//        if ("getOne_For_Update".equals(action)) {
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 ****************************************/
//            Integer titleNo = Integer.valueOf(req.getParameter("titleNo"));
//
//            /*************************** 2.開始查詢資料 ****************************************/
//            AdmAuthorityServiceImpl admAuthoritySvc = new AdmAuthorityServiceImpl();
//            AdmAuthority admAuthority = admAuthoritySvc.getOneAdmAuthority(titleNo);
//
//            /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
//            req.setAttribute("admAuthority", admAuthority);
//            String url = "/admauthority/update_admAuthority_input.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);
//            successView.forward(req, res);
//        }
//
//        if ("update".equals(action)) {
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//            Integer titleNo = Integer.valueOf(req.getParameter("titleNo").trim());
//
//            Integer authFuncNo = Integer.valueOf(req.getParameter("authFuncNo").trim());
//
//            AdmAuthority admAuthority = new AdmAuthority();
//            admAuthority.setTitleNo(titleNo);
//            admAuthority.setAuthFuncNo(authFuncNo);
//
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                req.setAttribute("admAuthority", admAuthority);
//                RequestDispatcher failureView = req.getRequestDispatcher("/admauthority/update_admAuthority_input.jsp");
//                failureView.forward(req, res);
//                return; // 程式中斷
//            }
//
//            /*************************** 2.開始修改資料 *****************************************/
//
//            AdmAuthorityServiceImpl admAuthoritySvc = new AdmAuthorityServiceImpl();
//            admAuthority = admAuthoritySvc.updateAdmAuthority(titleNo, authFuncNo);
//
//            /*************************** 3.修改完成,準備轉交(Send the Success view) *************/
//            req.setAttribute("admAuthority", admAuthority);
//            String url = "/admauthority/listOneAdmAuthority.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);
//            successView.forward(req, res);
//        }
//
//        if ("insert".equals(action)) {
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
//            Integer titleNo = Integer.valueOf(req.getParameter("titleNo").trim());
//
//            Integer authFuncNo = Integer.valueOf(req.getParameter("authFuncNo").trim());
//
//            AdmAuthority admAuthority = new AdmAuthority();
//            admAuthority.setAuthFuncNo(authFuncNo);
//
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                req.setAttribute("admAuthority", admAuthority);
//                RequestDispatcher failureView = req.getRequestDispatcher("/admauthority/addAdmAuthority.jsp");
//                failureView.forward(req, res);
//                return;
//            }
//
//            /*************************** 2.開始新增資料 ***************************************/
//            AdmAuthorityServiceImpl admAuthoritySvc = new AdmAuthorityServiceImpl();
//            admAuthority = admAuthoritySvc.addAdmAuthority(titleNo, authFuncNo);
//
//            /*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
//            String url = "/admauthority/listAllAdmAuthority.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);
//            successView.forward(req, res);
//        }
//
//        if ("delete".equals(action)) { // 來自listAllAdmAuthorityCategory.jsp
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 ***************************************/
//            Integer titleNo = Integer.valueOf(req.getParameter("titleNo"));
//
//            /*************************** 2.開始刪除資料 ***************************************/
//            AdmAuthorityServiceImpl admAuthoritySvc = new AdmAuthorityServiceImpl();
//            admAuthoritySvc.deleteAdmAuthority(titleNo);
//
//            /*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
//            String url = "/admauthoritycategory/listAllAdmAuthority.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
//            successView.forward(req, res);
//        }
//    }
//
//}
