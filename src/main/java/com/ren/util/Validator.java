package com.ren.util;


import static com.ren.util.Regex.*;

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

    /**
     * 確認是否為已壓縮的檔案格式
     *
     * @param contentType 前端傳送參數標頭
     * @return 如果為jpeg、png等已壓縮格式傳回true、非壓縮檔案格式傳回false
     */
    public static boolean validateFileType(String contentType) {
        return compressFileRegex.matcher(contentType).find();
    }

    public static boolean validateInteger(String userId) {
        return integerRegex.matcher(userId).find();
    }

}
