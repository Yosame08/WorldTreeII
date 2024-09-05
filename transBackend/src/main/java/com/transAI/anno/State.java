package com.transAI.anno;

import com.transAI.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { StateValidation.class })
public @interface State {
    String message() default "state只能是已发布或者草稿";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
