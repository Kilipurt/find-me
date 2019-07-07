package com.findme.controller;

import com.findme.exception.UnauthorizedException;
import com.findme.models.User;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Interceptor extends HandlerInterceptorAdapter {

    private Logger logger = Logger.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null || session.isNew() || request.getRequestURI().equals("/")
                || request.getRequestURI().equals("/login") || request.getRequestURI().equals("/user-registration")) {
            return true;
        }

        logger.error("User is not authorized");
        throw new UnauthorizedException("User is not authorized");
    }
}
