package com.simblog.api.exception;

public class CommentNotFound extends SimblogException {

    private static final String MESSAGE = "存在しないコメントです。";

    public CommentNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
