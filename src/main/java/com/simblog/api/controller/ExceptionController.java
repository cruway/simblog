package com.simblog.api.controller;

import com.simblog.api.exception.SimblogException;
import com.simblog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message("リクエスト失敗")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ResponseBody
    @ExceptionHandler(SimblogException.class)
    public ResponseEntity<ErrorResponse> simblogException(SimblogException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("例外発生", e);

        ErrorResponse body = ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(body);

        return response;
    }
}
