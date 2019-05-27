package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import com.findme.util.RequestJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class UserController {

    private UserService userService;
    private RelationshipService relationshipService;
    private RequestJsonUtil requestJsonUtil;
    private Logger logger = Logger.getLogger(UserController.class);

    private Long loginUserId;

    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService, RequestJsonUtil requestJsonUtil) {
        this.userService = userService;
        this.relationshipService = relationshipService;
        this.requestJsonUtil = requestJsonUtil;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable() String userId) {
        logger.info("UserController profile method. Moving to user page");

        try {
            Long id = Long.parseLong(userId);
            User user = userService.findById(id);

            if (user == null) {
                logger.error("UserController profile method. User with id " + id + " was not found");
                throw new NotFoundException("User with id " + id + " was not found");
            }

            model.addAttribute("user", user);
            
            if (loginUserId != null) {
                model.addAttribute("loginUserId", loginUserId);
            }

            if (loginUserId != null) {
                model.addAttribute("relationshipStatus", relationshipService.getRelationshipStatus(loginUserId, id));
            }

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
        logger.info("UserController registerUser method. Register new user");

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
        logger.info("UserController login method.");

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
        logger.info("UserController logout method.");

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
        logger.info("UserController getOutcomeRequests method. Selecting outcome requests");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("UserController getOutcomeRequests method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            if (Long.parseLong(userId) != loggedInUser.getId()) {
                logger.error("UserController getOutcomeRequests method. User has not enough rights");
                throw new BadRequestException("User has not enough rights");
            }

            List<User> sentRequestsUsers = userService.getOutcomeRequests(Long.parseLong(userId));

            return new ResponseEntity<>(requestJsonUtil.getJsonFromList(sentRequestsUsers), HttpStatus.OK);
        } catch (IllegalStateException | BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/income-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getIncomeRequests(HttpSession session, @PathVariable String userId) {
        logger.info("UserController getIncomeRequests method. Selecting income requests");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("UserController getIncomeRequests method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            if (Long.parseLong(userId) != loggedInUser.getId()) {
                logger.error("UserController getIncomeRequests method. User has not enough rights");
                throw new BadRequestException("User has not enough rights");
            }

            List<User> receivedRequestsUsers = userService.getIncomeRequests(Long.parseLong(userId));

            return new ResponseEntity<>(requestJsonUtil.getJsonFromList(receivedRequestsUsers), HttpStatus.OK);
        } catch (IllegalStateException | BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}