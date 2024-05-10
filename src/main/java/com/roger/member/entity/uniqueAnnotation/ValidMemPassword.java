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

@Constraint(validatedBy = ValidMemPassword.ValidMemPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMemPassword {

    // 驗證失敗時顯示的錯誤處理
    String message() default "會員密碼只能是數字和英文字母（包括大小寫）";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidMemPasswordValidator implements ConstraintValidator<ValidMemPassword, String> {

        // 會員密碼驗證的正則表達式
        private static final Pattern MEMBER_PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,20}$");


        @Override
        public void initialize(ValidMemPassword constraintAnnotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 寫法完成
            // 如果值為 null，則視為有效
            if (value.trim().isEmpty()) {
                return true;
            }

            // 使用正則表達式檢查值是否符合會員密碼的格式
            return MEMBER_PASSWORD_PATTERN.matcher(value).matches();
        }
    }
}
