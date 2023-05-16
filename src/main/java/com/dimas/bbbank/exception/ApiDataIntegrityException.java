package com.dimas.bbbank.exception;

public class ApiDataIntegrityException extends ApiBaseException {
    public ApiDataIntegrityException(String message) {
        super(message);
    }

    public ApiDataIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }
}
