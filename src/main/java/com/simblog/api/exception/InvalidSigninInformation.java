package com.simblog.api.exception;

public class InvalidSigninInformation extends SimblogException {

    private static final String MESSAGE = "ID/PASSWORDが正しくありません。";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
