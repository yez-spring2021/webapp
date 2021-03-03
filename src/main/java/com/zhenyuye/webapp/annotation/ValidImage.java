package com.zhenyuye.webapp.annotation;

import com.zhenyuye.webapp.validators.ImageTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageTypeValidator.class)

@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE,ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
    String message() default "The image type is not supported";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
