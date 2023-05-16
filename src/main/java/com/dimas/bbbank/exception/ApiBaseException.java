package com.dimas.bbbank.exception;

public class ApiBaseException extends RuntimeException {

    public ApiBaseException(String message) { super(message); }

    public ApiBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
