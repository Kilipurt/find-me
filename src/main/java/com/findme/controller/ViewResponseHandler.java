package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(assignableTypes = {PostControllerViewResponse.class, UserControllerViewResponse.class})
public class ViewResponseHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView badRequestExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView modelAndView = new ModelAndView("badRequestException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ModelAndView numberFormatExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView modelAndView = new ModelAndView("badRequestException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView unauthorizedExceptionHandler(HttpServletRequest req, Exception e)
            throws Exception {
        ModelAndView modelAndView = new ModelAndView("unauthorizedException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ModelAndView internalServerErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView modelAndView = new ModelAndView("internalServerError");
        modelAndView.addObject("error", e);
        return modelAndView;
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView notFoundExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView modelAndView = new ModelAndView("notFoundException");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }
}
