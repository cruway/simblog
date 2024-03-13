package com.simblog.api.exception;

public class UserNotFound extends SimblogException {

    private static final String MESSAGE = "存在しないユーザです。";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
