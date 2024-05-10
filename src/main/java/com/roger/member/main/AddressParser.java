package com.roger.member.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AddressParser 是一個用於解析給定地址的類。
 * 它會將地址拆分為三個部分：省份/市、區/縣和詳細地址。
 */
public class AddressParser {

    /**
     * 程序的入口點。
     * 給定一個範例地址，通過調用 parseAddress 函數進行拆分，
     * 並輸出拆分後的省份/市、區/縣和詳細地址。
     *
     * @param args 程序命令列參數（未使用）。
     */
    public static void main(String[] args) {
        // 給定的地址
        String address = "基隆市 仁愛區 忠孝東路四段100號";

        // 拆分地址
        String[] parsedAddress = parseAddress(address);

        // 輸出拆分後的地址部分
        System.out.println("縣/市：" + parsedAddress[0]);
        System.out.println("區：" + parsedAddress[1]);
        System.out.println("詳細地址：" + parsedAddress[2]);
    }

    /**
     * 拆分地址函數。
     * 此函數使用正則表達式將給定的地址拆分為三個部分：
     * 縣/市、區/縣/市和詳細地址。
     *
     * @param address 要拆分的地址字串。
     * @return 一個字串數組，其中包含省份/市、區/縣和詳細地址。
     *         如果地址無法匹配正則表達式，則返回一個空數組。
     */
    public static String[] parseAddress(String address) {
        // 定義正則表達式
        Pattern pattern = Pattern.compile("^(.+市|.+縣)(.+區|.+縣|.+市)(.+)$");
        Matcher matcher = pattern.matcher(address);

        // 匹配成功後拆分地址
        if (matcher.matches()) {
            String country = matcher.group(1);
            String district = matcher.group(2);
            String oldData = matcher.group(3);

            return new String[] {country, district, oldData};
        } else {
            // 如果匹配失敗，返回空數組
            return new String[0];
        }
    }
}
