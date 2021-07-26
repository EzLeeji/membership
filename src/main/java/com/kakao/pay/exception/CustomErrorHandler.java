package com.kakao.pay.exception;

import com.kakao.pay.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomErrorHandler{

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response> handleCustomException(CustomException ex){
        Response response = new Response(ex.getMessage(),ex.getCode());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(CustomException ex){
        Response response = new Response(ex.getMessage(),ex.getCode());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}