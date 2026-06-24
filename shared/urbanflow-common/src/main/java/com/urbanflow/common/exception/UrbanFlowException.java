package com.urbanflow.common.exception;

/**
 * Base runtime exception for all UrbanFlow domain and application errors.
 */
public class UrbanFlowException extends RuntimeException {

    private final String errorCode;

    public UrbanFlowException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public UrbanFlowException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
