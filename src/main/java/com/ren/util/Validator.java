package com.ren.util;


import static com.ren.util.Regex.emailRegex;
import static com.ren.util.Regex.resourcesRegex;

public class Validator {

    /**
     * 驗證是否為信箱格式
     *
     * @param email 在登入頁面輸入用戶名或Email時傳入方法內判斷格式
     * @return 返回布林值，如果符合Email格式則返回true，不符合則返回false
     */
    public static boolean validateEmail(String email) {
        return emailRegex.matcher(email).find();
    }

    /**
     * 驗證請求的url是否為靜態資源或為登入、註冊、忘記密碼等網頁
     *
     * @param url 發出請求的網址 request url
     * @return True為靜態資源或登入相關網頁(無須過濾)，False為需過濾頁面
     */
    public static boolean validateURL(String url) {
        return resourcesRegex.matcher(url).find();
    }

}
