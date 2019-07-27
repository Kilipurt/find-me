package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import com.findme.service.UserService;
import com.findme.util.RequestJsonUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@Log4j
public class UserControllerStatus {

    private UserService userService;
    private RequestJsonUtil requestJsonUtil;

    @Autowired
    public UserControllerStatus(UserService userService, RequestJsonUtil requestJsonUtil) {
        this.userService = userService;
        this.requestJsonUtil = requestJsonUtil;
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) throws Exception {
        log.info("UserController registerUser method. Register new user");

        userService.save(user);
        return new ResponseEntity<>("Registration is success", HttpStatus.OK);
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login(Model model, HttpSession session, @RequestParam(name = "phone") String phone,
                                        @RequestParam(name = "password") String password) throws Exception {
        log.info("UserController login method.");

        User user = userService.login(phone, password);
        session.setAttribute("user", user);
        model.addAttribute("user", user);

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

    @RequestMapping(path = "/outcome-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getOutcomeRequests(HttpSession session, @PathVariable String userId)
            throws Exception {
        log.info("UserController getOutcomeRequests method. Selecting outcome requests");

        User loggedInUser = (User) session.getAttribute("user");
        long id = Long.parseLong(userId);

        if (id != loggedInUser.getId()) {
            log.error("UserController getOutcomeRequests method. User has not enough rights");
            throw new BadRequestException("User has not enough rights");
        }

        List<User> sentRequestsUsers = userService.getOutcomeRequests(id);
        return new ResponseEntity<>(requestJsonUtil.getJsonFromList(sentRequestsUsers), HttpStatus.OK);
    }

    @RequestMapping(path = "/income-requests/{userId}", method = RequestMethod.GET)
    public ResponseEntity<String> getIncomeRequests(HttpSession session, @PathVariable String userId) throws Exception {
        log.info("UserController getIncomeRequests method. Selecting income requests");

        User loggedInUser = (User) session.getAttribute("user");
        long id = Long.parseLong(userId);

        if (id != loggedInUser.getId()) {
            log.error("UserController getIncomeRequests method. User has not enough rights");
            throw new BadRequestException("User has not enough rights");
        }

        List<User> receivedRequestsUsers = userService.getIncomeRequests(id);
        return new ResponseEntity<>(requestJsonUtil.getJsonFromList(receivedRequestsUsers), HttpStatus.OK);
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