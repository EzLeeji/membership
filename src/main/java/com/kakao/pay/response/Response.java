package com.kakao.pay.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {
    private boolean success ;
    private T response;
    private ErrorResponse error;

    public Response(T response){
        this.success = true;
        this.response = response;
    }

    public Response(String message, int status){
        error = new ErrorResponse(message,status);
    }
}
