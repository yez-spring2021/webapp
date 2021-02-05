package com.zhenyuye.webapp.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ValidationException{

    public UnAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
