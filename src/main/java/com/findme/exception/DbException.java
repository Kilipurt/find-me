package com.findme.exception;

public class DbException extends InternalServerError {

    public DbException(String message) {
        super(message);
    }
}
