package com.urbanflow.common.exception;

/**
 * Thrown when a business rule is violated.
 */
public class BusinessException extends UrbanFlowException {

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }
}
