package com.simblog.api.exception;

public class AlreadyExistsEmailException extends SimblogException{

    private static final String MESSAGE = "すでに登録したメールです";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
