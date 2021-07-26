package com.kakao.pay.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private int code;
    private String message;

    public CustomException(CustomTypeException ce){
        this.code = ce.getStatusCode();
        this.message = ce.getMessage();
    }
}
