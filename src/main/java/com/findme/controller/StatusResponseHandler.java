package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice(assignableTypes = {PostControllerStatusResponse.class, UserControllerStatusResponse.class, RelationshipControllerStatusResponse.class})
public class StatusResponseHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<String> badRequestExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<String> numberFormatExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedExceptionHandler(HttpServletRequest req, Exception e)
            throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ResponseEntity<String> internalServerErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> ioExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
