package com.findme.controller.responseHandler;

import com.findme.controller.statusResponseController.MessageControllerStatus;
import com.findme.controller.statusResponseController.PostControllerStatus;
import com.findme.controller.statusResponseController.RelationshipControllerStatus;
import com.findme.controller.statusResponseController.UserControllerStatus;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice(assignableTypes = {PostControllerStatus.class, UserControllerStatus.class,
        RelationshipControllerStatus.class, MessageControllerStatus.class})
public class StatusResponseHandler {

    @ExceptionHandler({BadRequestException.class, NumberFormatException.class})
    public ResponseEntity<String> badRequestExceptionHandler(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedExceptionHandler(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ResponseEntity<String> internalServerErrorHandler(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> ioExceptionHandler(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}