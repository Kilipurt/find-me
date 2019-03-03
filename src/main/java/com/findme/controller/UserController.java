package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private RelationshipService relationshipService;

    private Long loginUserId;

    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
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
            model.addAttribute("loginUserId", loginUserId);
            model.addAttribute("relationshipStatus", relationshipService.getRelationshipStatus(loginUserId, id));

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
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "password") String password
    ) {
        try {
            User user = userService.login(phone, password);
            session.setAttribute("user", user);
            loginUserId = user.getId();

            return new ResponseEntity<>(user.getId().toString(), HttpStatus.OK);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        user.setDateLastActive(new Date());

        try {
            userService.update(user);
            session.invalidate();
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/outcome-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getOutcomeRequests(HttpSession session, @PathVariable String userId) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null || Long.parseLong(userId) != loggedInUser.getId()) {
                throw new BadRequestException("User is not authorized");
            }

            List<User> sentRequestsUsers = userService.getOutcomeRequests(Long.parseLong(userId));

            StringBuilder usersJSON = new StringBuilder();
            usersJSON.append("\"users\":[");

            for(User user : sentRequestsUsers) {
                usersJSON.append("{\"id\":\"")
                        .append(user.getId())
                        .append("\",\"firstName\":\"")
                        .append(user.getFirstName()).append("\",\"lastName\":\"")
                        .append(user.getLastName())
                        .append("\"},");
            }

            usersJSON.deleteCharAt(usersJSON.length() - 1);
            usersJSON.append("]");

            return new ResponseEntity<>(usersJSON.toString(), HttpStatus.OK);
        } catch (IllegalStateException | BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/income-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getIncomeRequests(HttpSession session, @PathVariable String userId) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null || Long.parseLong(userId) != loggedInUser.getId()) {
                throw new BadRequestException("User is not authorized");
            }

            List<User> receivedRequestsUsers = userService.getIncomeRequests(Long.parseLong(userId));

            StringBuilder usersJSON = new StringBuilder();
            usersJSON.append("\"users\" : [");

            for(User user : receivedRequestsUsers) {
                usersJSON.append("{ \"id\" : \"")
                        .append(user.getId())
                        .append("\", \"firstName\" : \"")
                        .append(user.getFirstName()).append("\", \"lastName\" : \"")
                        .append(user.getLastName())
                .append("\"},");
            }

            usersJSON.deleteCharAt(usersJSON.length() - 1);
            usersJSON.append("]");

            return new ResponseEntity<>(usersJSON.toString(), HttpStatus.OK);
        } catch (IllegalStateException | BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
