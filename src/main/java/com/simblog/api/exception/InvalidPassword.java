package com.simblog.api.exception;

public class InvalidPassword extends SimblogException {

    private static final String MESSAGE = "パスワードが正しくありません。";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
