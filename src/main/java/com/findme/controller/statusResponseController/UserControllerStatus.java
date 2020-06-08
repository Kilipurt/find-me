package com.findme.controller.statusResponseController;

import com.findme.models.User;
import com.findme.service.UserService;
import com.findme.util.JsonUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@Log4j
public class UserControllerStatus {

    private UserService userService;
    private JsonUtil jsonUtil;

    @Autowired
    public UserControllerStatus(UserService userService, JsonUtil jsonUtil) {
        this.userService = userService;
        this.jsonUtil = jsonUtil;
    }

    @RequestMapping(path = "/get-logged-user", method = RequestMethod.GET)
    public ResponseEntity<User> getLoggedInUser(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) throws Exception {
        log.info("UserController registerUser method. Register new user");

        userService.save(user);
        return new ResponseEntity<>("Registration is success", HttpStatus.OK);
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login(HttpSession session, @RequestParam String phone, @RequestParam String password)
            throws Exception {
        log.info("UserController login method.");

        User user = userService.login(phone, password);
        session.setAttribute("user", user);

        return new ResponseEntity<>(user.getId().toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpSession session) throws Exception {
        log.info("UserController logout method.");

        User user = (User) session.getAttribute("user");
        user.setDateLastActive(new Date());
        userService.update(user);
        session.invalidate();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(path = "/outcome-requests", method = RequestMethod.GET)
    public ResponseEntity<String> getOutcomeRequests(HttpSession session) throws Exception {
        log.info("UserController getOutcomeRequests method. Selecting outcome requests");

        User loggedInUser = (User) session.getAttribute("user");
        List<User> sendRequestsUsersTo = userService.getOutcomeRequests(loggedInUser.getId());
        return new ResponseEntity<>(jsonUtil.toJson(sendRequestsUsersTo), HttpStatus.OK);
    }

    @RequestMapping(path = "/income-requests", method = RequestMethod.GET)
    public ResponseEntity<String> getIncomeRequests(HttpSession session) throws Exception {
        log.info("UserController getIncomeRequests method. Selecting income requests");

        User loggedInUser = (User) session.getAttribute("user");
        List<User> receivedRequestsUsersFrom = userService.getIncomeRequests(loggedInUser.getId());
        return new ResponseEntity<>(jsonUtil.toJson(receivedRequestsUsersFrom), HttpStatus.OK);
    }

//    @RequestMapping(path = "get-friends", method = RequestMethod.GET)
//    public ResponseEntity<String> getFriends(@RequestParam String userId, HttpSession session, Model model)
//            throws Exception {
//        log.info("UserController getFriends method. Selecting friends of user");
//
//        User loggedInUser = (User) session.getAttribute("user");
//        long id = Long.parseLong(userId);
//
//        if (id != loggedInUser.getId()) {
//            log.error("UserController getFriends method. User has not enough rights");
//            throw new BadRequestException("User has not enough rights");
//        }
//
//        model.addAttribute("friends", userService.getFriends(id));
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}