package com.zhenyuye.webapp.utils;

import com.zhenyuye.webapp.exceptions.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

public class ValidationUtil {
    public static void verifyInput(BindingResult result) {
        if (result.hasErrors()) {
            if (result.hasErrors()) {
                String msg = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(" "));
                throw new ValidationException(msg);
            }
        }
    }
}
