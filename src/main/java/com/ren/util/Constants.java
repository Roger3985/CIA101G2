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
    byte LOGIN_STATE_LOGIN = 1;
    byte LOGIN_STATE_LOGOUT = 0;
    byte LOGOUT_STATE_LOGIN = 0;
    byte LOGOUT_STATE_LOGOUT = 1;
    byte OnShelf = 1;
    byte OffShelf = 0;
    byte ACTIVE = 1;
    byte INACTIVE = 0;
    String TITLE_EMPLOYEE = "1";
    String TITLE_FULLTIME = "2";
    String TITLE_MANAGER = "3";
    String TITLE_BOSS = "4";


    /**
     * 首頁路徑
     */
    String backendIndex = "/backend/index";

    /**
     * 登入頁面路徑
     */
    String loginPage = "/backend/login";

    /**
     * 登入頁面路徑
     */
    String errorRedirect = "/backend/login?error=";

    /**
     * 註冊頁面路徑
     */
    String registerPage = "/backend/register";

    /**
     * 忘記密碼頁面路徑
     */
    String forgotPasswordPage = "/backend/forgotPassword";

    String emailSubject = "Fall衣Love股份有限公司";
    String forgotPwdContent = "親愛的Fall衣Love員工您好，因您的帳戶密碼遺失，系統將您的密碼修改為預設，若您需要修改，請麻煩登入後於管理員系統修改，以下為預設密碼: ";

}
