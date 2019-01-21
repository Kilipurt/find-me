package com.findme.exception;

public class InternalServerError extends Exception {
    public InternalServerError(String message) {
        super("500: " + message);
    }
}
