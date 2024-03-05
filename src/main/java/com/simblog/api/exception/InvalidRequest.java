package com.simblog.api.exception;

public class InvalidRequest extends SimblogException {

    private static final String MESSAGE = "否定なリクエストです。";

    public InvalidRequest() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
