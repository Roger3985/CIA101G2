package com.roger.converters;

import com.roger.notice.entity.Notice;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * StringToTimestampConverter 是一個將指定格式的字串轉換為 java.sql.Timestamp 的轉換器。
 * 使用指定的日期時間格式進行轉換。
 * @Author Roger
 */
public class StringToTimestampConverter implements Converter<String, Timestamp> {

    // 定義日期時間格式：yyyy-MM-dd HH:mm:ss
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 將指定格式的字串轉換為 java.sql.Timestamp。
     *
     * @param source 要轉換的字串，格式應為 yyyy-MM-dd HH:mm:ss。
     * @return 轉換後的 java.sql.Timestamp，如果轉換失敗則返回 null。
     */
    @Override
    public Timestamp convert(String source) {
        try {
            // 將字串解析為 java.util.Date
            java.util.Date utilDate = dateTimeFormat.parse(source);
            // 將 java.util.Date 轉換為 java.sql.Timestamp
            return new Timestamp(utilDate.getTime());
        } catch (ParseException e) {
            // 如果解析失敗，可以打印錯誤消息或進行其他的處理
            e.printStackTrace();
            System.out.println("轉換失敗");
            return null;
        }
    }
}
