package com.roger.member.main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MemberPasswordHash {

    public static void main(String[] args) {
        // 將 memPwd 加上前綴 "Fall" 和後綴 "Love" 進行簡易密碼加密
        String memPwd = "123456";
        memPwd = "Fall" + memPwd + "Love";

        try {
            // 根據 MD5 演算法生成 MessageDigest 物件
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            // 將字符串 memPwd 轉換為字節陣列
            byte[] srcBytes = memPwd.getBytes();
            // 使用 srcBytes 更新摘要
            md5.update(srcBytes);
            // 完成哈希演算法計算，得到 result
            byte[] resultBytes = md5.digest();

            // 將字節陣列轉換為十六進制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : resultBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 輸出十六進制字符串
            System.out.println("MD5 哈希結果: " + hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            // 異常處理
            e.printStackTrace();
        }
    }
}
