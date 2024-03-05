package com.simblog.api.exception;

public class PostNotFound extends RuntimeException {

    private static final String MESSAGE = "存在しないコンテンツです。";

    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }
}
