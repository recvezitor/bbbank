package com.dimas.bbbank.exception;

public class InsufficientFundsException extends ApiBaseException {
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
