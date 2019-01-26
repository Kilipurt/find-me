package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String userId) {

        try {
            Long id = Long.parseLong(userId);
            User user = userService.findById(id);

            if (user == null) {
                throw new BadRequestException("User with id " + id + " was not found");
            }

            model.addAttribute("user", user);
            return "profile";
        } catch (NumberFormatException | BadRequestException e) {
            model.addAttribute("exception", e);
            return "badRequestException";
        }
    }
}
