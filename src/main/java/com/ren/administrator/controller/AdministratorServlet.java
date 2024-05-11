//package com.ren.administrator.controller;
//
//import com.Entity.ServicePicture;
//import com.ren.administrator.service.Impl.AdministratorServiceImpl;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//
//@MultipartConfig
//@WebServlet("/administrator/administrator.do")
//public class AdministratorServlet extends HttpServlet {
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
//        if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//            // 展示Optional寫法
//            String str = Optional.ofNullable(req.getParameter("admNo"))
//                    .orElse("");
//            if (str.isEmpty()) {
//                errorMsgs.add("請輸入管理員編號");
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/select_administrator.jsp");
//                failureView.forward(req, res);
//                return;// 程式中斷
//            }
//
//            Integer admNo = null;
//            try {
//                admNo = Integer.valueOf(str);
//            } catch (Exception e) {
//                errorMsgs.add("管理員編號格式不正確");
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/select_administrator.jsp");
//                failureView.forward(req, res);
//                return;// 程式中斷
//            }
//
//            /*************************** 2.開始查詢資料 *****************************************/
//            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//            // 執行Service的getOnProduct，該方法執行DAO的findByPrimaryKey，將資料庫內的資料以VO的形式傳回
//            Administrator administrator = administratorSvc.getOneAdministrator(admNo);
//            String photoBase64 = administratorSvc.photoSticker(admNo);
//            // 引用類型的屬性在未附值時預設為null
//            if (administrator == null) {
//                errorMsgs.add("查無資料");
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/select_administrator.jsp");
//                failureView.forward(req, res);
//                return;// 程式中斷
//            }
//
//            /*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
//            req.setAttribute("administrator", administrator); // 資料庫取出的administrator物件,存入req
//            req.setAttribute("photoBase64", photoBase64);
//            String url = "/administrator/listOneAdministrator.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneAdministrator.jsp
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
//            Integer admNo = Integer.valueOf(req.getParameter("admNo"));
//
//            /*************************** 2.開始查詢資料 ****************************************/
//            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//            Administrator administrator = administratorSvc.getOneAdministrator(admNo);
//
//            /*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
//            req.setAttribute("administrator", administrator); // 資料庫取出的administrator物件,存入req
//            String url = "/administrator/update_administrator_input.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_administrator_input.jsp
//            successView.forward(req, res);
//        }
//
//        if ("update".equals(action)) { // 來自update_administrator_input.jsp的請求
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//            Integer admNo = Integer.valueOf(req.getParameter("admNo").trim());
//
//            String admPwd = req.getParameter("admPwd");
//            String admPwdReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//            if (admPwd == null || admPwd.trim().length() == 0) {
//                errorMsgs.add("商品名稱: 請勿空白");
//            } else if (!admPwd.trim().matches(admPwdReg)) { // 以下練習正則(規)表示式(regular-expression)
//                errorMsgs.add("商品名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//            }
//
//            String admName = req.getParameter("admName").trim();
//            if (admName == null || admName.trim().length() == 0) {
//                errorMsgs.add("商品資訊請勿空白");
//            }
//
//            Byte admStat = null;
//            try {
//                admStat = Byte.valueOf(req.getParameter("admStat").trim());
//            } catch (NumberFormatException e) {
//                admStat = Byte.valueOf("0");
//                errorMsgs.add("管理員狀態請填數字.");
//            }
//
//            String admEmail = req.getParameter("admEmail").trim();
//            if (admEmail == null || admEmail.trim().length() == 0) {
//                errorMsgs.add("email請勿空白");
//            }
//
//            Integer titleNo = Integer.valueOf(req.getParameter("titleNo").trim());
//
//            java.sql.Date admHireDate = null;
//            try {
//                admHireDate = java.sql.Date.valueOf(req.getParameter("admHireDate").trim());
//            } catch (IllegalArgumentException e) {
//                errorMsgs.add("請輸入日期");
//            }
//
//            byte[] admPhoto = null;
//            Part photoPart = req.getPart("admPhoto");
//            if (photoPart != null) {
//                try (InputStream inputStream = photoPart.getInputStream()) {
//                    admPhoto = inputStream.readAllBytes();
//                } catch (IOException e) {
//                    errorMsgs.add("讀取 admPhoto 時發生錯誤: " + e.getMessage());
//                }
//            } else {
//                errorMsgs.add("未找到照片");
//            }
//
//            Administrator administrator = new Administrator();
//            administrator.setAdmNo(admNo);
//            administrator.setAdmPwd(admPwd);
//            administrator.setAdmName(admName);
//            administrator.setAdmStat(admStat);
//            administrator.setAdmEmail(admEmail);
//            administrator.setTitleNo(titleNo);
//            administrator.setAdmHireDate(admHireDate);
//            administrator.setAdmPhoto(admPhoto);
//
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                req.setAttribute("administrator", administrator); // 含有輸入格式錯誤的administrator物件,也存入req
//                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/update_administrator_input.jsp");
//                failureView.forward(req, res);
//                return; // 程式中斷
//            }
//
//            /*************************** 2.開始修改資料 *****************************************/
//            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//            administrator = administratorSvc.updateAdministrator(admNo, admPwd, admName, admStat, admEmail, titleNo, admHireDate, admPhoto);
//
//            /*************************** 3.修改完成,準備轉交(Send the Success view) *************/
//            req.setAttribute("administrator", administrator); // 資料庫update成功後,正確的的administrator物件,存入req
//            String url = "/administrator/listOneAdministrator.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);
//            successView.forward(req, res);
//        }
//
//        if ("insert".equals(action)) { // 來自addAdministrator.jsp的請求
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
//
//            String admPwd = req.getParameter("admPwd");
//            String admPwdReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//            if (admPwd == null || admPwd.trim().length() == 0) {
//                errorMsgs.add("商品名稱: 請勿空白");
//            } else if (!admPwd.trim().matches(admPwdReg)) { // 以下練習正則(規)表示式(regular-expression)
//                errorMsgs.add("商品名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//            }
//
//            String admName = req.getParameter("admName").trim();
//            if (admName == null || admName.trim().length() == 0) {
//                errorMsgs.add("商品資訊請勿空白");
//            }
//
//            Byte admStat = null;
//            try {
//                admStat = Byte.valueOf(req.getParameter("admStat").trim());
//            } catch (NumberFormatException e) {
//                admStat = Byte.valueOf("0");
//                errorMsgs.add("管理員狀態請填數字.");
//            }
//
//            String admEmail = req.getParameter("admEmail").trim();
//            if (admEmail == null || admEmail.trim().length() == 0) {
//                errorMsgs.add("email請勿空白");
//            }
//
//            Integer titleNo = Integer.valueOf(req.getParameter("titleNo").trim());
//
//            java.sql.Date admHireDate = null;
//            try {
//                admHireDate = java.sql.Date.valueOf(req.getParameter("admHireDate").trim());
//            } catch (IllegalArgumentException e) {
//                errorMsgs.add("請輸入日期");
//            }
//
//            byte[] admPhoto = null;
//            try {
//                Part photoPart = req.getPart("admPhoto");
//                if (photoPart != null) {
//                    InputStream inputStream = photoPart.getInputStream();
//                    admPhoto = inputStream.readAllBytes();
//                } else {
//                    errorMsgs.add("未找到 照片 部分。");
//                }
//            } catch (IOException e) {
//                errorMsgs.add("讀取 admPhoto 時發生錯誤: " + e.getMessage());
//            }
//
//            Administrator administrator = new Administrator();
//            administrator.setAdmPwd(admPwd);
//            administrator.setAdmName(admName);
//            administrator.setAdmStat(admStat);
//            administrator.setAdmEmail(admEmail);
//            administrator.setTitleNo(titleNo);
//            administrator.setAdmHireDate(admHireDate);
//            administrator.setAdmPhoto(admPhoto);
//
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                req.setAttribute("administrator", administrator); // 含有輸入格式錯誤的administrator物件,也存入req
//                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/addAdministrator.jsp");
//                failureView.forward(req, res);
//                return;
//            }
//
//            /*************************** 2.開始新增資料 ***************************************/
//            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//
//            /*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
//            String url = "/administrator/listAllAdministrator.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllProduct.jsp
//            successView.forward(req, res);
//        }
//
//        if ("delete".equals(action)) {
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*************************** 1.接收請求參數 ***************************************/
//            Integer admNo = Integer.valueOf(req.getParameter("admNo"));
//
//            /*************************** 2.開始刪除資料 ***************************************/
//            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//            administratorSvc.deleteAdministrator(admNo);
//
//            /*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
//            String url = "/administrator/listAllAdministrator.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
//            successView.forward(req, res);
//        }
//
//        if ("upload".equals(action)) { // 來自addAdministrator.jsp的請求
//
//            List<String> errorMsgs = new LinkedList<>();
//            // Store this set in the request scope, in case we need to
//            // send the ErrorPage view.
//            req.setAttribute("errorMsgs", errorMsgs);
//
//            /*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
//            Integer admNo = Integer.valueOf(req.getParameter("admNo").trim());
//
//            byte[] admPhoto = null;
//            try {
//                // 取得Part物件
//                Part photoPart = req.getPart("uploadImg");
//                if (photoPart != null) {
//
//                    InputStream inputStream = photoPart.getInputStream();
//                    admPhoto = inputStream.readAllBytes();
//
//                } else {
//                    errorMsgs.add("未找到照片部分。");
//                }
//            } catch (IOException e) {
//                errorMsgs.add("讀取admPhoto時發生錯誤: " + e.getMessage());
//            }
//            // Send the use back to the form, if there were errors
//            if (!errorMsgs.isEmpty()) {
//                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/upload.jsp");
//                failureView.forward(req, res);
//                return;
//            }
//
//            /*************************** 2.開始新增資料 ***************************************/
//            // 取得Service物件執行上傳方法
//            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//            administratorSvc.uploadPhoto(admNo, admPhoto);
//            // 根據傳入編號取得VO，後續傳入req給forward後的頁面使用
//            Administrator administrator = administratorSvc.getOneAdministrator(admNo);
//            String photoBase64 = administratorSvc.photoSticker(admNo);
//
//            /*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
//            req.setAttribute("administrator", administrator); // 資料庫取出的administrator物件,存入req
//            req.setAttribute("photoBase64", photoBase64);
//            String url = "/administrator/listOneAdministrator.jsp";
//            RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneAdministrator.jsp
//            successView.forward(req, res);
//        }
//
////        if ("register".equals(action)) {
////
////            List<String> errorMsgs = new LinkedList<>();
////            // Store this set in the request scope, in case we need to
////            // send the ErrorPage view.
////            req.setAttribute("errorMsgs", errorMsgs);
////
////            /*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
////            String admName = req.getParameter("admName").trim();
////            if (admName == null || admName.trim().length() == 0) {
////                errorMsgs.add("管理員名稱請勿空白");
////            }
////
////            String admPwd = req.getParameter("admPwd");
////            String admPwdReg = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,12}$";
////            if (admPwd == null || admPwd.trim().length() == 0) {
////                errorMsgs.add("管理員密碼: 請勿空白!");
////            } else if (!admPwd.trim().matches(admPwdReg)) { // 以下練習正則(規)表示式(regular-expression)
////                errorMsgs.add("管理員密碼: 只能是中、英文字母、數字和_ , 且長度必需在6到12之間");
////            }
////
////            String admPwd2 = req.getParameter("admPwd2");
////            if (admPwd2 == null || admPwd2.trim().length() == 0) {
////                errorMsgs.add("確認密碼請勿空白!");
////            } else if (!admPwd2.trim().matches(admPwd)) { // 以下練習正則(規)表示式(regular-expression)
////                errorMsgs.add("˙啊呦!這就玄了哦!密碼與確認密碼不相同!");
////            }
////
////            String admEmail = req.getParameter("admEmail");
////            String admEmailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
////
////            if (admEmail == null || admEmail.trim().length() == 0) {
////                errorMsgs.add("請輸入電子郵件地址");
////            } else if (!admEmail.matches(admEmailRegex)) {
////                errorMsgs.add("電子郵件地址格式不正確");
////            }
////             密碼加密及產生鹽值
////            var salt = ThreadLocalRandom.current().nextInt();
////            var encrypt = String.valueOf(salt + password.hashCode());
////
////            AdministratorVO administrator = new AdministratorVO();
////            administrator.setAdmPwd(admName);
////            administrator.setAdmPwd(admPwd);
////            administrator.setAdmPwd(admEmail);
////            AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
////            // 執行Service的register()方法，並返回驗證後的字串集合，加入errorMsgs
////            errorMsgs.addAll(administratorSvc.register(administrator));
////            // Send the use back to the form, if there were errors
////            if (!errorMsgs.isEmpty()) {
////                req.setAttribute("administrator", administrator); // 含有輸入格式錯誤的administrator物件,也存入req
////                RequestDispatcher failureView = req.getRequestDispatcher("/administrator/register.jsp");
////                failureView.forward(req, res);
////                return;
////            }
////
////            /*************************** 2.開始新增資料 ***************************************/
////            // 確認無重複，執行註冊
////            Byte admStat = 1;
////            Integer titleNo = 10;
////            LocalDate today = LocalDate.now();
////            // 將 LocalDate 轉換為 java.sql.Date
////            Date admHireDate = Date.valueOf(today);
////            // 將VO放入dao定義的方法內，使其執行資料庫操作
////            administratorSvc.addAdministrator(admPwd, admName, admStat, admEmail, titleNo, admHireDate);
////
////            /*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
////            String url = "/administrator/listAllAdministrator.jsp";
////            RequestDispatcher successView = req.getRequestDispatcher(url);
////            successView.forward(req, res);
////        }
//    }
//
//}