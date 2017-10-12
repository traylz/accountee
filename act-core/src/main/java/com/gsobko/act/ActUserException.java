package com.gsobko.act;

/**
 * This exceptions should have message that can be passed to user
 */
public class ActUserException extends RuntimeException {
    public ActUserException(String message) {
        super(message);
    }

    public ActUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
