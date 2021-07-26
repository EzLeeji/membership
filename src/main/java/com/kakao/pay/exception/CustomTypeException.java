package com.kakao.pay.exception;

import lombok.Getter;

@Getter
public enum CustomTypeException {

    NO_USER_SIGN_MEMBERSHIP(-1001,"멤버십에 가입한 사용자가 아닙니다."),
    ALREADY_USER_SIGN_MEMBERSHIP(-1002,"멤버십에 이미 가입한 사용자 입니다."),
    ALREADY_USER_DELETE_MEMBERSHIP(-1003,"삭제할 수 없는 사용자 입니다."),
    NO_MEMBERSHIP_COMPANY(-2001,"존재하지 않는 멤버십 입니다."),
    NOT_FOUND(404,"찾을 수 없음"),
    INTERNAL_SERVER_ERR(500,"내부 서버 오류"),
    BAD_REQUEST(400,"잘못된 요청");

    int statusCode;
    String message;

    CustomTypeException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
