package com.findme.controller.responseHandler;

import com.findme.controller.viewResponseController.PostController;
import com.findme.controller.viewResponseController.UserController;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(assignableTypes = {PostController.class, UserController.class})
public class ViewResponseHandler {

    @ExceptionHandler(value = {BadRequestException.class, NumberFormatException.class})
    public ModelAndView badRequestExceptionHandler(Exception e) {
        ModelAndView modelAndView = new ModelAndView("badRequestException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView unauthorizedExceptionHandler(Exception e) {
        ModelAndView modelAndView = new ModelAndView("unauthorizedException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ModelAndView internalServerErrorHandler(Exception e) {
        ModelAndView modelAndView = new ModelAndView("internalServerError");
        modelAndView.addObject("error", e);
        return modelAndView;
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView notFoundExceptionHandler(Exception e) {
        ModelAndView modelAndView = new ModelAndView("notFoundException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }
}