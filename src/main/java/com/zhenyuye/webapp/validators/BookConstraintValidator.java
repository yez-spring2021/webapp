package com.zhenyuye.webapp.validators;

import com.zhenyuye.webapp.annotation.UniqueISBN;
import com.zhenyuye.webapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class BookConstraintValidator  implements ConstraintValidator<UniqueISBN, String> {
    @Autowired
    private BookService bookService;
    @Override
    public void initialize(UniqueISBN constraintAnnotation) {

    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return !bookService.isbnCheck(isbn);
    }
}
