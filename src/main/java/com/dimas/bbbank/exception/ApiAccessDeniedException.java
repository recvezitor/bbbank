package com.dimas.bbbank.exception;

public class ApiAccessDeniedException extends ApiBaseException {
    public ApiAccessDeniedException(String message) {
        super(message);
    }

    public ApiAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
