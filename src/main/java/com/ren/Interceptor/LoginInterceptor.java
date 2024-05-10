//package com.ren.Interceptor;
//
//import com.Entity.Administrator;
//import com.ren.administrator.service.AdministratorServiceImpl;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
//import static com.ren.util.Constants.YES;
//
///**
// * 用於確認後台員工登入狀態的攔截器，
// * 主要有三功能:
// * 1.確認Session，防止重複登入
// * 2.自動登入功能
// * 3.權限驗證
// */
//public class LoginInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestURI = request.getRequestURI();
//        AdministratorServiceImpl administratorSvc = new AdministratorServiceImpl();
//        if (checkURL(requestURI)) {
//            System.out.println("走小路");
//        } else {
//            Administrator administrator = (Administrator) request.getSession().getAttribute("administrator");
//            if (administrator == null) {
//                response.sendRedirect(loginPage);
//                return false;
//            }
//
//            Optional<Cookie> userCookie = Optional.ofNullable(request.getCookies()).flatMap(this::userCookie);
//
//            if (!userCookie.isPresent()) {
//                response.sendRedirect(loginPage);
//                return false;
//            }
//
//            String userId = request.getParameter("userId");
//            if (validateEmail(userId)) {
//                administrator = administratorSvc.getOneAdministrator(userId);
//            } else {
//                administrator = administratorSvc.getOneAdministrator(Integer.valueOf(userId));
//            }
//
//            Cookie cookie = null;
//            String autoLogin = request.getParameter("autoLogin");
//            if (autoLogin.equals(YES)) {
//                String cookieName = administrator.getAdmNo().toString();
//                String cookieValue = administrator.getTitle().getTitleNo().toString();
//                cookie = new Cookie(cookieName, cookieValue);
//                cookie.setMaxAge(864000);
//                response.addCookie(cookie);
//            }
//        }
//
//        // 如果已經登入，則允許訪問
//        if (requestURI.endsWith(".css") || requestURI.endsWith(".js")) {
//            return true;
//        }
//        System.out.println("成功抵達");
//        return true;
//    }
//
//    /**
//     * 首頁路徑
//     */
//    private String backendIndex = "/backend/index";
//
//    /**
//     * 登入頁面路徑
//     */
//    private String loginPage = "/backend/login";
//
//    /**
//     * 註冊頁面路徑
//     */
//    private String registerPage = "/backend/register";
//
//    /**
//     * 忘記密碼頁面路徑
//     */
//    private String forgotPasswordPage = "/backend/forgotPassword";
//
//    private Optional<Cookie> userCookie(Cookie[] cookies) {
//        return java.util.Arrays.stream(cookies).filter(this::check).findFirst();
//    }
//
//    private boolean check(Cookie cookie) {
//        return "admNo".equals(cookie.getName()) && "titleNo".equals(cookie.getValue());
//    }
//
//    private final Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
//
//    private Boolean validateEmail(String email) {
//        return emailRegex.matcher(email).find();
//    }
//
//    private Boolean checkURL(String url) {
//        return url.equals(loginPage) || url.equals(registerPage) || url.equals(forgotPasswordPage);
//    }
//}