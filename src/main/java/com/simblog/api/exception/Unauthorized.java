package com.simblog.api.exception;

/**
 * status -> 401
 */
public class Unauthorized extends SimblogException {

    private static final String MESSAGE = "認証が必要です。";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
