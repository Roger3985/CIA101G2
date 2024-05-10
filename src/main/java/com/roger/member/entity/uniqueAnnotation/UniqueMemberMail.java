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

//自定義會員信箱是否存在
@Constraint(validatedBy = UniqueMemberMail.UniqueMemberMailValidator.class)
@Target(ElementType.FIELD) // 可設置在屬性上
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueMemberMail {

    String message() default "信箱已被使用!";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};

    class UniqueMemberMailValidator implements ConstraintValidator<UniqueMemberMail, String> {

        @Autowired
        private MemberRepository memberRepository;

        @Override
        public boolean isValid(String memMail, ConstraintValidatorContext context) {
            return !memberRepository.existsByMemMail(memMail);
        }
    }
}
