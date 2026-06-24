package com.urbanflow.common.exception;

/**
 * Thrown when a requested resource does not exist.
 */
public class ResourceNotFoundException extends UrbanFlowException {

    public ResourceNotFoundException(String resource, Object identifier) {
        super("RESOURCE_NOT_FOUND", "%s not found: %s".formatted(resource, identifier));
    }
}
