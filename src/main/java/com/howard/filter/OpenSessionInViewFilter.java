//package com.howard.filter;
//
//import com.howard.util.HibernateUtil;
//import org.hibernate.SessionFactory;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@WebFilter(urlPatterns = { "/*" })
//public class OpenSessionInViewFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        SessionFactory factory = HibernateUtil.getSessionFactory();
//        try {
//            System.out.println("Opening transaction in filter");
//            factory.getCurrentSession().beginTransaction();
//            chain.doFilter(req, res);
//            factory.getCurrentSession().getTransaction().commit();
//        } catch (Exception e) {
//            if (!res.isCommitted()) {
//                factory.getCurrentSession().getTransaction().rollback();
//                e.printStackTrace();
//                res.setContentType("text/html");
//                res.setCharacterEncoding("UTF-8");
//                PrintWriter out = res.getWriter();
//                out.println("<html><body><h1>An internal error occurred, please contact the system administrator.</h1></body></html>");
//                out.close(); // Ensure the stream is closed after writing
//            } else {
//                System.out.println("Response already committed, unable to send error page.");
//            }
//        }
//    }
//
//
//
//}
//
//
//
//
