package com.simblog.api.exception;

public abstract class SimblogException extends RuntimeException {

    public SimblogException(String message) {
        super(message);
    }

    public SimblogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
