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

@Constraint(validatedBy = ValidMemMobile.ValidMemMobileValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMemMobile {

    // 驗證失敗時顯示的錯誤訊息
    String message() default "手機格式錯誤，請輸入以下的格式:09XXXXXXXX";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidMemMobileValidator implements ConstraintValidator<ValidMemMobile, String> {

        // 會員信箱驗證的正則表達式
        private static final Pattern MEMBER_MOBILE_PATTERN = Pattern.compile("^[0][9]\\d{8}$");

        @Override
        public void initialize(ValidMemMobile constraintAnnotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 寫法完成
            // 如果值為 null，則視為有效
            if (value.trim().isEmpty()) {
                return true;
            }

            // 使用正則表達式檢查值是否符合會員電話的格式
            return MEMBER_MOBILE_PATTERN.matcher(value).matches();
        }
    }
}
