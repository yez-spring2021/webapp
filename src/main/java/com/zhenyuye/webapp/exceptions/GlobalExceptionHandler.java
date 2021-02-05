package com.zhenyuye.webapp.exceptions;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> handleException(RuntimeException e) {
        JSONObject jsonObject = buildMessage(e);
        return new ResponseEntity<>(jsonObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e) {
        JSONObject jsonObject = buildMessage(e);
        return new ResponseEntity<>(jsonObject, e.getHttpStatus());
    }

    private JSONObject buildMessage(RuntimeException e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "Network time out, please try again later.";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        return jsonObject;
    }

}
