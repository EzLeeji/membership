package com.kakao.pay.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(String errorMessage, int status) {
        this.message = errorMessage;
        this.status = status;
    }
}
