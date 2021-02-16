package com.zhenyuye.webapp.annotation;


import com.zhenyuye.webapp.validators.BookConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookConstraintValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueISBN {
    String message() default "ISBN has been used";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
