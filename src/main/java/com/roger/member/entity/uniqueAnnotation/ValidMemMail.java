package com.roger.member.entity.uniqueAnnotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Constraint(validatedBy = ValidMemMail.ValidMemMailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMemMail {

    // 驗證失敗時顯示的錯誤處理
    String message() default "信箱格式輸入錯誤！，請輸入xxx@gmail.com or xxx@yahoo.com...";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidMemMailValidator implements ConstraintValidator<ValidMemMail, String> {

        // 會員信箱驗證的正則表達式
        private static final Pattern MEMBER_MAIL_PATTERN = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");


        @Override
        public void initialize(ValidMemMail constraintAnnotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 寫法完成
            // 如果值為 null，則視為有效
            if (value.trim().isEmpty()) {
                return true;
            }

            // 使用正則表達式檢查值是否符合會員信箱的格式
            return MEMBER_MAIL_PATTERN.matcher(value).matches();

        }
    }

}
