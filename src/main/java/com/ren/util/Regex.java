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

    /**
     * 壓縮過的檔案類型
     */
    Pattern compressFileRegex = Pattern.compile("^(image/jpeg|image/png|image/gif|application/zip|application/x-rar-compressed|application/gzip|application/x-bzip2|application/x-xz|application/x-tar|application/x-7z-compressed|audio/mpeg|video/mp4|video/x-matroska|video/quicktime|video/mpeg|video/x-flv|audio/ogg|video/webm|application/pdf|application/msword|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/vnd.ms-excel|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet|application/vnd.ms-powerpoint|application/vnd.openxmlformats-officedocument.presentationml.presentation|image/vnd.adobe.photoshop|application/x-iso9660-image|application/x-apple-diskimage)$");


}
