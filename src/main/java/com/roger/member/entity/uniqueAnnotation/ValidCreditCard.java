package com.roger.member.entity.uniqueAnnotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Optional;
import java.util.regex.Pattern;

@Constraint(validatedBy = ValidCreditCard.ValidCreditCardValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreditCard {

    // 驗證失敗時顯示的錯誤訊息
    String message() default "信用卡卡號應只包含 15 到 19 位數字";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};

    class ValidCreditCardValidator implements ConstraintValidator<ValidCreditCard, String> {

        // 信用卡號碼的正則表達式
        private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("^[0-9]{15,19}$");

        @Override
        public void initialize(ValidCreditCard annotation) {
            // 初始化驗證器（如果需要，可以在這裡進行初始化）
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            // DONE 第一種寫法
            // 使用 Optional 包裝 value，並使用 map 方法進行正則表達式匹配
            return Optional.ofNullable(value)
                    // 如果值為 null 或空字串，則視為有效
                    .filter(val -> !val.trim().isEmpty())
                    // 使用正則表達式檢查值是否符合信用卡號碼的格式
                    .map(val -> CREDIT_CARD_PATTERN.matcher(val).matches())
                    // 如果 Optional 為空，則表示 value 為 null 或空字符串，視為有效
                    .orElse(true);

//            // DONE 第二種寫法
//            // 如果值為 null，則視為有效
//            if (value.trim().isEmpty()) {
//                return true;
//            }
//
//            // 使用正則表達式檢查值是否符合信用卡號碼的格式
//            return CREDIT_CARD_PATTERN.matcher(value).matches();
        }
    }
}