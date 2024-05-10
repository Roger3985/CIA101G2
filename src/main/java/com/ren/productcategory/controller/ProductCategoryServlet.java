//package com.ren.productcategory.controller;
//
//import com.Entity.ServicePicture;
//import com.ren.productcategory.service.ProductCategoryServiceImpl;
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
//@WebServlet("/productcategory/productCategory.do")
//public class ProductCategoryServlet extends HttpServlet {
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
//			String str = req.getParameter("pCatNo");
//			if (str == null || (str.trim()).length() == 0) {
//				errorMsgs.add("請輸入商品類別編號");
//			}
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/productcategory/select_productCategory.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷
//			}
//
//			Integer pCatNo = null;
//			try {
//				pCatNo = Integer.valueOf(str);
//			} catch (Exception e) {
//				errorMsgs.add("商品類別編號格式不正確");
//			}
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/productcategory/select_productCategory.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷
//			}
//
//			/*************************** 2.開始查詢資料 *****************************************/
//			ProductCategoryServiceImpl productCategorySvc = new ProductCategoryServiceImpl();
//			ProductCategory productCategory = productCategorySvc.getOneProductCatagory(pCatNo);
//			// 引用類型的屬性在未附值時預設為null
//			if (productCategory == null) {
//				errorMsgs.add("查無資料");
//			}
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/productcategory/select_productCategory.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷
//			}
//
//			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
//			req.setAttribute("productCategory", productCategory);
//			String url = "/productcategory/listOneProductCategory.jsp";
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
//			Integer pCatNo = Integer.valueOf(req.getParameter("pCatNo").trim());
//
//			/*************************** 2.開始查詢資料 ****************************************/
//			ProductCategoryServiceImpl productCategorySvc = new ProductCategoryServiceImpl();
//			ProductCategory productCategory = productCategorySvc.getOneProductCatagory(pCatNo);
//
//			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
//			req.setAttribute("productCategory", productCategory);
//			String url = "/productcategory/update_productCategory_input.jsp";
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
//			Integer pCatNo = Integer.valueOf(req.getParameter("pCatNo").trim());
//
//			String pCatName = req.getParameter("pCatName");
//			String pCatNameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//			if (pCatName == null || pCatName.trim().length() == 0) {
//				errorMsgs.add("商品名稱: 請勿空白");
//			} else if (!pCatName.trim().matches(pCatNameReg)) { // 以下練習正則(規)表示式(regular-expression)
//				errorMsgs.add("商品名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//			}
//
//			ProductCategory productCategory = new ProductCategory();
//			productCategory.setpCatNo(pCatNo);
//			productCategory.setpCatName(pCatName);
//
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				req.setAttribute("productCategory", productCategory);
//				RequestDispatcher failureView = req.getRequestDispatcher("/productcategory/update_productCategory_input.jsp");
//				failureView.forward(req, res);
//				return; // 程式中斷
//			}
//
//			/*************************** 2.開始修改資料 *****************************************/
//			ProductCategoryServiceImpl productCategorySvc = new ProductCategoryServiceImpl();
//			productCategory = productCategorySvc.updateProductCategory(pCatNo, pCatName);
//
//			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
//			req.setAttribute("productCategory", productCategory);
//			String url = "/productcategory/listOneProductCategory.jsp";
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
//			Integer pCatNo = Integer.valueOf(req.getParameter("pCatNo").trim());
//
//			String pCatName = req.getParameter("pCatName");
//			String pCatNameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//			if (pCatName == null || pCatName.trim().length() == 0) {
//				errorMsgs.add("商品名稱: 請勿空白");
//			} else if (!pCatName.trim().matches(pCatNameReg)) {
//				errorMsgs.add("商品名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//			}
//
//			ProductCategory productCategory = new ProductCategory();
//			productCategory.setpCatName(pCatName);
//
//			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				req.setAttribute("productCategory", productCategory);
//				RequestDispatcher failureView = req.getRequestDispatcher("/productcategory/addProductCategory.jsp");
//				failureView.forward(req, res);
//				return;
//			}
//
//			/*************************** 2.開始新增資料 ***************************************/
//			ProductCategoryServiceImpl productCategorySvc = new ProductCategoryServiceImpl();
//			productCategory = productCategorySvc.addProductCategory(pCatName);
//
//			/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
//			String url = "/productcategory/listAllProductCategory.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);
//			successView.forward(req, res);
//		}
//
//		if ("delete".equals(action)) { // 來自listAllProductCategory.jsp
//
//			List<String> errorMsgs = new LinkedList<>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//
//			/*************************** 1.接收請求參數 ***************************************/
//			Integer pCatNo = Integer.valueOf(req.getParameter("pCatNo"));
//
//			/*************************** 2.開始刪除資料 ***************************************/
//			ProductCategoryServiceImpl productCategorySvc = new ProductCategoryServiceImpl();
//			productCategorySvc.deleteProductCategory(pCatNo);
//
//			/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
//			String url = "/productcategory/listAllProductCategory.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
//			successView.forward(req, res);
//		}
//	}
//
//}
