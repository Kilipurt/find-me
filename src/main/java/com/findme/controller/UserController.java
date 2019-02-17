package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.models.User;
import com.findme.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable() String userId) {

        try {
            Long id = Long.parseLong(userId);
            User user = userService.findById(id);

            if (user == null) {
                throw new NotFoundException("User with id " + id + " was not found");
            }

            model.addAttribute("user", user);
            return "profile";
        } catch (NumberFormatException | BadRequestException e) {
            model.addAttribute("exception", e);
            return "badRequestException";
        } catch (NotFoundException e) {
            model.addAttribute("exception", e);
            return "notFoundException";
        } catch (InternalServerError e) {
            model.addAttribute("error", e);
            return "internalServerError";
        }
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        try {
            userService.save(user);
            return new ResponseEntity<>("Registration is success", HttpStatus.OK);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login(
            HttpSession session,
            @RequestParam(value = "phoneField") String phone,
            @RequestParam(value = "passwordField") String password
    ) {
        try {
            User user = userService.login(phone, password);

            if (session != null) {
                session.setAttribute("user", user);
            }

            return new ResponseEntity<>("Login is success", HttpStatus.OK);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpSession session) {
        if (session != null) {
            User user = (User) session.getAttribute("user");
            user.setDateLastActive(new Date());

            try {
                userService.update(user);
            } catch (InternalServerError e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (BadRequestException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            session.invalidate();
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
