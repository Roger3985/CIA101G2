//package com.ren.filter.backend;
//
//import com.Entity.Administrator;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@WebFilter("/backend/product/updateProduct")
//public class DemoFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) req;
//        HttpServletResponse httpResponse = (HttpServletResponse) res;
//
//        HttpSession session = ((HttpServletRequest) req).getSession();
//        Administrator administrator = (Administrator) session.getAttribute("administrator");
//        Integer titleNo = administrator.getTitle().getTitleNo();
//        if (titleNo > 2) {
//            // 未授權
//            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            res.getWriter().write("你沒有此權限!");
//        }
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
