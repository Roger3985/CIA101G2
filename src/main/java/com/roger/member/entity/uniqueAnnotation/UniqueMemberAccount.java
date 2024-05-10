package com.roger.member.entity.uniqueAnnotation;

import com.roger.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定義會員帳號是否存在
@Constraint(validatedBy = UniqueMemberAccount.UniqueMemberAccountValidator.class)
@Target(ElementType.FIELD) // 可設置在屬性上
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueMemberAccount {

    String message() default "帳號已被使用!";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};

    class UniqueMemberAccountValidator implements ConstraintValidator<UniqueMemberAccount, String> {

        @Autowired
        private MemberRepository memberRepository;

        @Override
        public boolean isValid(String memAcc, ConstraintValidatorContext context) {
            return !memberRepository.existsByMemAcc(memAcc);
        }
    }

}
