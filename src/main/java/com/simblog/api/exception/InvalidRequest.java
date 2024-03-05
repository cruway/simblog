package com.simblog.api.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends SimblogException {

    private static final String MESSAGE = "否定なリクエストです。";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
