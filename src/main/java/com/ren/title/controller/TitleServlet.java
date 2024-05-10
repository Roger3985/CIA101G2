//package com.ren.title.controller;
//
//import com.Entity.ServicePicture;
//import com.ren.title.service.TitleServiceImpl;
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
//@WebServlet("/title/title.do")
//public class TitleServlet extends HttpServlet {
//
//	@Override
//	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		doPost(req, res);
//	}
//
//	@Override
//	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//
//		req.setCharacterEncoding("UTF-8");
//		String action = req.getParameter("action");
//
//		if ("getOne_For_Display".equals(action)) {
//
//			List<String> errorMsgs = new LinkedList<>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//
//			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//			String str = req.getParameter("titleNo");
//			if (str == null || (str.trim()).length() == 0) {
//				errorMsgs.add("請輸入職位編號");
//			}
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/title/select_title.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷
//			}
//
//			Integer titleNo = null;
//			try {
//				titleNo = Integer.valueOf(str);
//			} catch (Exception e) {
//				errorMsgs.add("編號格式不正確");
//			}
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/title/select_title.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷
//			}
//
//			/*************************** 2.開始查詢資料 *****************************************/
//			TitleServiceImpl titleSvc = new TitleServiceImpl();
//			Title title = titleSvc.getOneTitle(titleNo);
//			// 引用類型的屬性在未附值時預設為null
//			if (title == null) {
//				errorMsgs.add("查無資料");
//			}
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/title/select_title.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷
//			}
//
//			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
//			req.setAttribute("title", title);
//			String url = "/title/listOneTitle.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);
//			successView.forward(req, res);
//		}
//
//		if ("getOne_For_Update".equals(action)) {
//
//			List<String> errorMsgs = new LinkedList<>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//
//			/*************************** 1.接收請求參數 ****************************************/
//			Integer titleNo = Integer.valueOf(req.getParameter("titleNo"));
//
//			/*************************** 2.開始查詢資料 ****************************************/
//			TitleServiceImpl titleSvc = new TitleServiceImpl();
//			Title title = titleSvc.getOneTitle(titleNo);
//
//			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
//			req.setAttribute("title", title);
//			String url = "/title/update_title_input.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);
//			successView.forward(req, res);
//		}
//
//		if ("update".equals(action)) {
//
//			List<String> errorMsgs = new LinkedList<>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//
//			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//			Integer titleNo = Integer.valueOf(req.getParameter("titleNo").trim());
//
//			String titleName = req.getParameter("titleName");
//			String titleNameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//			if (titleName == null || titleName.trim().length() == 0) {
//				errorMsgs.add("職位名稱: 請勿空白");
//			} else if (!titleName.trim().matches(titleNameReg)) { // 以下練習正則(規)表示式(regular-expression)
//				errorMsgs.add("職位名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//			}
//
//			Title title = new Title();
//			title.setTitleNo(titleNo);
//			title.setTitleName(titleName);
//
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				req.setAttribute("title", title);
//				RequestDispatcher failureView = req.getRequestDispatcher("/title/update_title_input.jsp");
//				failureView.forward(req, res);
//				return; // 程式中斷
//			}
//
//			/*************************** 2.開始修改資料 *****************************************/
//			TitleServiceImpl titleSvc = new TitleServiceImpl();
//			// 執行update後返回以titleNo查詢更新後的VO
//			title = titleSvc.updateTitle(titleNo, titleName);
//
//			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
//			req.setAttribute("title", title);
//			String url = "/title/listOneTitle.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);
//			successView.forward(req, res);
//		}
//
//		if ("insert".equals(action)) {
//
//			List<String> errorMsgs = new LinkedList<>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//
//			/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
//			String titleName = req.getParameter("titleName");
//			String titleNameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//			if (titleName == null || titleName.trim().length() == 0) {
//				errorMsgs.add("商品名稱: 請勿空白");
//			} else if (!titleName.trim().matches(titleNameReg)) { // 以下練習正則(規)表示式(regular-expression)
//				errorMsgs.add("商品名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//			}
//
//			Title title = new Title();
//			title.setTitleName(titleName);
//
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				req.setAttribute("title", title);
//				RequestDispatcher failureView = req.getRequestDispatcher("/title/addTitle.jsp");
//				failureView.forward(req, res);
//				return;
//			}
//
//			/*************************** 2.開始新增資料 ***************************************/
//			TitleServiceImpl titleSvc = new TitleServiceImpl();
//			titleSvc.addTitle(titleName);
//
//			/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
//			String url = "/title/listAllTitle.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);
//			successView.forward(req, res);
//		}
//
//		if ("delete".equals(action)) {
//
//			List<String> errorMsgs = new LinkedList<>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//
//			/*************************** 1.接收請求參數 ***************************************/
//			Integer titleNo = Integer.valueOf(req.getParameter("titleNo"));
//
//			/*************************** 2.開始刪除資料 ***************************************/
//			TitleServiceImpl titleSvc = new TitleServiceImpl();
//			titleSvc.deleteTitle(titleNo);
//
//			/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
//			String url = "/title/listAllTitle.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
//			successView.forward(req, res);
//		}
//	}
//
//}