package com.findme.exception;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super("400: " + message);
    }
}
