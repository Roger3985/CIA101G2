package com.roger.converters;

import org.springframework.core.convert.converter.Converter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * StringToDateConverter 是一個將 String 類型的日期格式轉換為 java.sql.Date 類型的轉換器。
 * 使用指定的日期格式進行轉換。
 * @Author Roger
 */
public class StringToDateConverter implements Converter<String, Date> {

    // 設置日期格式：yyyy-MM-dd
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 將指定的日期格式的字符串轉換為 java.sql.Date 類型。
     *
     * @param source 要轉換的字串，格式應為 yyyy-MM-dd。
     * @return 轉換後的 java.sql.Date 對象，如果轉換失敗，則返回 null。
     */
    @Override
    public Date convert(String source) {
        try {
            // 將 String 轉換為 java.util.Date
            java.util.Date utilDate = dateFormat.parse(source);
            // 將 java.util.Date 轉換為 java.sql.Date
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            // 如果轉換失敗，可以選擇打印錯誤或返回 null
            e.printStackTrace();
            System.out.println("轉換失敗");
            return null;
        }
    }

}
