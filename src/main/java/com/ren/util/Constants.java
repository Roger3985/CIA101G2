package com.ren.util;

import java.util.regex.Pattern;

/**
 * 於interface內宣告常數、類別會自動加入public static final
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
    int THIRD_ORDER = 3;
    int PARTJOB = 1;
    int FULLTIME = 2;
    int MANAGER = 3;
    int BOSS = 4;

    /**
     * 首頁路徑
     */
    String backendIndex = "/backend/index";

    /**
     * 登入頁面路徑
     */
    String loginPage = "/backend/login";

    /**
     * 註冊頁面路徑
     */
    String registerPage = "/backend/register";

    /**
     * 忘記密碼頁面路徑
     */
    String forgotPasswordPage = "/backend/forgotPassword";

}
