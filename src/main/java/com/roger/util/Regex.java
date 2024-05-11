package com.roger.util;

import java.util.regex.Pattern;

public interface Regex {

    /**
     * 靜態資源的正則表達式(含登入、註冊、忘記密碼)
     */
    Pattern resourcesRegex = Pattern.compile(".*\\.(css|js|png|jpg|jpeg|gif|svg|woff|ttf|woff2)$|/addMember|/loginMember|/loginMemberByAccount|/forgotPassword");

    /**
     * Email 格式的正則表達式
     */
    Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * 會員帳號格式的正則表達式
     */
    Pattern accountRegex = Pattern.compile("^[a-zA-Z0-9]{4,10}$");
}
