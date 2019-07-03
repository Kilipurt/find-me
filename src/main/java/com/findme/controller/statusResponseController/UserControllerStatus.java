package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import com.findme.service.UserService;
import com.findme.util.RequestJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class UserControllerStatus {

    private UserService userService;
    private RequestJsonUtil requestJsonUtil;
    private Logger logger = Logger.getLogger(UserControllerStatus.class);

    @Autowired
    public UserControllerStatus(UserService userService, RequestJsonUtil requestJsonUtil) {
        this.userService = userService;
        this.requestJsonUtil = requestJsonUtil;
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) throws Exception {
        logger.info("UserController registerUser method. Register new user");

        userService.save(user);
        return new ResponseEntity<>("Registration is success", HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login(HttpSession session, @RequestParam(value = "phone") String phone,
            @RequestParam(value = "password") String password) throws Exception {
        logger.info("UserController login method.");

        User user = userService.login(phone, password);
        session.setAttribute("user", user);
        com.findme.controller.viewResponseController.UserController.setLoginUserId(user.getId());
        return new ResponseEntity<>(user.getId().toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpSession session) throws Exception {
        logger.info("UserController logout method.");

        User user = (User) session.getAttribute("user");
        user.setDateLastActive(new Date());
        userService.update(user);
        session.invalidate();
        com.findme.controller.viewResponseController.UserController.setLoginUserId(null);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(path = "/outcome-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getOutcomeRequests(HttpSession session, @PathVariable String userId)
            throws Exception {
        logger.info("UserController getOutcomeRequests method. Selecting outcome requests");

        User loggedInUser = (User) session.getAttribute("user");

        if (Long.parseLong(userId) != loggedInUser.getId()) {
            logger.error("UserController getOutcomeRequests method. User has not enough rights");
            throw new BadRequestException("User has not enough rights");
        }

        List<User> sentRequestsUsers = userService.getOutcomeRequests(Long.parseLong(userId));
        return new ResponseEntity<>(requestJsonUtil.getJsonFromList(sentRequestsUsers), HttpStatus.OK);
    }

    @RequestMapping(path = "/income-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getIncomeRequests(HttpSession session, @PathVariable String userId) throws Exception {
        logger.info("UserController getIncomeRequests method. Selecting income requests");

        User loggedInUser = (User) session.getAttribute("user");

        if (Long.parseLong(userId) != loggedInUser.getId()) {
            logger.error("UserController getIncomeRequests method. User has not enough rights");
            throw new BadRequestException("User has not enough rights");
        }

        List<User> receivedRequestsUsers = userService.getIncomeRequests(Long.parseLong(userId));
        return new ResponseEntity<>(requestJsonUtil.getJsonFromList(receivedRequestsUsers), HttpStatus.OK);
    }
}