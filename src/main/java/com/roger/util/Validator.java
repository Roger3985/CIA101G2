package com.roger.util;

import static com.roger.util.Regex.*;

public class Validator {

    /**
     * 驗證是否為信箱格式。
     *
     * @param email 在登入頁面輸入 Email 時傳入方法內判斷格式。
     * @return 返回布林值，如果符合 Email 格式則返回 true，不符合則返回 false。
     */
    public static boolean validateEmail(String email) {
        return emailRegex.matcher(email).find();
    }

    /**
     * 驗證是否為帳號格式
     *
     * @param account 在登入頁面輸入 帳號 時傳入方法內判斷格式。
     * @return 返回布林值，如果符合 帳號 格式則返回 true，不符合則返回 false。
     */
    public static boolean validateAccout(String account) {
        return accountRegex.matcher(account).find();
    }


    /**
     * 驗證請求的 url 是否為靜態資源或為登入、註冊、忘記密碼等網頁
     *
     * @param url 發出請求的網址 request url
     * @return True為靜態資源或登入相關網頁(無須過濾)，False為需過濾頁面
     */
    public static boolean validateURL(String url) {
        return resourcesRegex.matcher(url).find();
    }

}
