package com.bungeeinc.bungeeapp.api.exception;

public class NoSuchProfileException extends RuntimeException {
    public NoSuchProfileException() {
    }

    public NoSuchProfileException(String message) {
        super(message);
    }
}
