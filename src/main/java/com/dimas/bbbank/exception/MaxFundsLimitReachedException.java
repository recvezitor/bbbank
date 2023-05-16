package com.dimas.bbbank.exception;

public class MaxFundsLimitReachedException extends ApiBaseException {
    public MaxFundsLimitReachedException(String message) {
        super(message);
    }

    public MaxFundsLimitReachedException(String message, Throwable cause) {
        super(message, cause);
    }
}
