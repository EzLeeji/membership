package com.kakao.pay.exception;

import com.kakao.pay.response.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ServerExceptionController implements ErrorController {

    @RequestMapping(value = "/error")
    public ResponseEntity<Response> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                Response response = new Response(CustomTypeException.NOT_FOUND.getMessage(), CustomTypeException.NOT_FOUND.getStatusCode());
                return new ResponseEntity(response, HttpStatus.NOT_FOUND);
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value()){
                Response response = new Response(CustomTypeException.BAD_REQUEST.getMessage(), CustomTypeException.BAD_REQUEST.getStatusCode());
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
        }
        Response response = new Response(CustomTypeException.INTERNAL_SERVER_ERR.getMessage(), CustomTypeException.INTERNAL_SERVER_ERR.getStatusCode());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
