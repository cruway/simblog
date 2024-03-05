package com.simblog.api.exception;

public class PostNotFound extends SimblogException {

    private static final String MESSAGE = "存在しないコンテンツです。";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
