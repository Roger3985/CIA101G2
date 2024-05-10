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

@Constraint(validatedBy = ValidMemName.ValidMemNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMemName {

    // 驗證失敗時顯示的錯誤處理
    String message() default "會員姓名: 只能是中、英文字母、數字和_，且只能在2~10位";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidMemNameValidator implements ConstraintValidator<ValidMemName, String> {

        // 會員姓名驗證的正則表達式
        private static final Pattern MEMBER_NAME_PATTERN = Pattern.compile("^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$");

        @Override
        public void initialize(ValidMemName constraintAnnotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 寫法完成
            // 如果值為 null，則視為有效
            if (value.trim().isEmpty()) {
                return true;
            }

            // 使用正則表達式檢查值是否符合會員姓名的格式
            return MEMBER_NAME_PATTERN.matcher(value).matches();

        }
    }
}
