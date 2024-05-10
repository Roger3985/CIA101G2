//package com.ren.filter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.annotation.WebInitParam;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(
//        urlPatterns = {"/*"},
//        initParams = { @WebInitParam(name = "ENCODING", value = "UTF-8") }
//)
//public class CharacterEncoding extends HttpFilter {
//    @Override
//    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        String encoding = getInitParameter("ENCODING");
//        req.setCharacterEncoding(encoding);
//        res.setContentType("text/html; charset=UTF-8");
//    }
//}
