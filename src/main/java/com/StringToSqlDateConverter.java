package com;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class StringToSqlDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (source == null || source.isEmpty()) {
//            System.out.println("有進來轉換器喔");
            TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
            TypeDescriptor targetType = TypeDescriptor.valueOf(Date.class);
            throw new ConversionFailedException(sourceType, targetType, source, new IllegalArgumentException("請填入日期!"));
        }
        try {
            return Date.valueOf(source);
        } catch (IllegalArgumentException e) {
            TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
            TypeDescriptor targetType = TypeDescriptor.valueOf(Date.class);
            throw new ConversionFailedException(sourceType, targetType, source, e);
        }
    }

}
