package com.zhenyuye.webapp.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException{
    private final HttpStatus httpStatus;
    public ValidationException() {
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ValidationException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ValidationException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
