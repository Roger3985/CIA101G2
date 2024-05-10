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

// 自定義會員手機號碼是否已存在
@Constraint(validatedBy = UniqueMemberMobile.UniqueMemberMobileValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueMemberMobile {

    String message() default "手機號碼已存在";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};

    class UniqueMemberMobileValidator implements ConstraintValidator<UniqueMemberMobile, String> {

        @Autowired
        private MemberRepository memberRepository;

        @Override
        public boolean isValid(String memMob, ConstraintValidatorContext context) {
            return !memberRepository.existsByMemMob(memMob);
        }

    }
}
