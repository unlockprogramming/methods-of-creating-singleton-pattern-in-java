package com.unlock_programming.exception;

public class SingletonViolationException extends RuntimeException {

    public SingletonViolationException(String message) {
        super(message);
    }
}
