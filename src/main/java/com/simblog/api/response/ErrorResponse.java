package com.simblog.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code": "400",
 *     "message": "リクエスト失敗",
 *     "validation": {
 *         "title": "値を入力してください"
 *     }
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String fieldName,String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
