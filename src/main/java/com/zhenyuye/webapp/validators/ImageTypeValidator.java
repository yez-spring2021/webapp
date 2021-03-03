package com.zhenyuye.webapp.validators;

import com.zhenyuye.webapp.annotation.ValidImage;
import com.zhenyuye.webapp.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
@Component
public class ImageTypeValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    @Autowired
    private FileService fileService;
    @Override
    public void initialize(ValidImage constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {


        if (multipartFile==null || multipartFile.getContentType()==null|| !isSupportedContentType(multipartFile.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only PNG/JPG/JPEG images are allowed.")
                    .addConstraintViolation();

            return false;
        }
        if(fileService.hasFile(multipartFile.getOriginalFilename())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Filename existed")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}
