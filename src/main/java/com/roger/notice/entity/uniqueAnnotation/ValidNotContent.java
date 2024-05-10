package com.roger.notice.entity.uniqueAnnotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Constraint(validatedBy = ValidNotContent.ValidNotContentValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNotContent {

    // 驗證失敗時顯示的錯誤處理
    String message() default "最新會員通知內容: 只能是中、英文字母、數字和_，, 且長度必需在1到30之間";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidNotContentValidator implements ConstraintValidator<ValidNotContent, String> {

        // 會員通知內容的正則表達式
        private static final Pattern NOTICE_CONTENT_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5,，]{1,30}$");

        @Override
        public void initialize(ValidNotContent constraintAnnotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 寫法完成
            // 如果值為 null，則視為有效
            if (value.trim().isEmpty()) {
                return true;
            }

            // 使用正則表達式檢查值是否符合會員通知訊息內容的格式
            return NOTICE_CONTENT_PATTERN.matcher(value).matches();
        }
    }
}
