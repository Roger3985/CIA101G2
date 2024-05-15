package com.ren.util;

import java.util.regex.Pattern;

public interface Regex {

    /**
     * 靜態資源的正則表達式(含登入、註冊、忘記密碼)
     */
    Pattern resourcesRegex =
            Pattern.compile(".*\\.(css|js|png|jpg|jpeg|gif|svg|woff|ttf|woff2)$|/register|/login|/forgotPassword|/sendEmail");

    /**
     * Email格式的正則表達式
     */
    Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * 新增的url格式的正則表達式
     */
    Pattern addUrlRegex = Pattern.compile("^/backend/[^/]+/add.*$");

    /**
     * 修改的url格式的正則表達式
     */
    Pattern updateUrlRegex = Pattern.compile("^/backend/[^/]+/update.*$");

    /**
     * 刪除的url格式的正則表達式
     */
    Pattern deleteUrlRegex = Pattern.compile("^/backend/[^/]+/delete.*$");


}
