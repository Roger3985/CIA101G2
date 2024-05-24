package com.roger.columnarticle.entity.uniqueAnnotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Constraint(validatedBy = ValidArtContent.ValidArtContentValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidArtContent {

    // 驗證失敗時顯示的錯誤處理
    String message() default "專欄文章內容: 只能是中、英文字母、數字和_，, 且長度必需在1到500之間";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidArtContentValidator implements ConstraintValidator<ValidArtContent, String> {

        // 專欄文章內容的正則表達式
        private static final Pattern ARTICLE_CONTENT_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5,，]{1,500}$");

        @Override
        public void initialize(ValidArtContent constraintAnnotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 寫法完成
            // 如果值為 null，則視為有效
            if (value.trim().isEmpty()) {
                return true;
            }

            // 使用正則表達式檢查值是否符合專欄文章內容的格式
            return ARTICLE_CONTENT_PATTERN.matcher(value).matches();
        }
    }
}
