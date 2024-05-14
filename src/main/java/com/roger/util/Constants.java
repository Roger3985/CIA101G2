package com.roger.util;

/**
 * 用於 interface 內宣告常數、類別會自動加入 public static final
 */
public interface Constants {

    int PAGE_MAX_RESULT = 10;
    int FIRST = 0;
    int SUCCESS = 1;
    int FAILURE = 0;
    byte YES = 1;
    byte NO = 0;
    int FIRST_ORDER = 1;
    int SECOND_ORDER = 2;

    /**
     * 首頁路徑
     */
    String frontendIndex = "/index";

    /**
     * 登入前台首頁路徑
     */
    String loginPage = "/frontend/member/loginMember";
    String loginPage2 = "/frontend/member/loginMemberByAccount";
    String loginPage3 = "/frontend/member/loginPage";

    /**
     * 註冊前台會員路徑
     */
    String registerPage = "/frontend/member/addMemberData";

    /**
     * 忘記密碼
     */
    String forgotPasswordPage = "/frontend/member/forgetPassword";


}
