package com.findme.controller;

import com.findme.exception.UnauthorizedException;
import com.findme.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Log4j
public class Interceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null || session.isNew() || request.getRequestURI().equals("/")
                || request.getRequestURI().equals("/login") || request.getRequestURI().equals("/user-registration")) {
            return true;
        }

        log.error("User is not authorized");
        throw new UnauthorizedException("User is not authorized");
    }
}
