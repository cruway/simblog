package com.simblog.api.response;

import lombok.Builder;
import lombok.Getter;

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
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
