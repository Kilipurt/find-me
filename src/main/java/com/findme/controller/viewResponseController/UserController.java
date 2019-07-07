package com.findme.controller.viewResponseController;

import com.findme.exception.NotFoundException;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private UserService userService;
    private RelationshipService relationshipService;

    private Logger logger = Logger.getLogger(UserController.class);

    private static User loginUser;

    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    public static User getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(User loginUser) {
        UserController.loginUser = loginUser;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable() String userId) throws Exception {
        logger.info("UserController profile method. Moving to user page");

        Long id = Long.parseLong(userId);
        User user = userService.findById(id);

        if (user == null) {
            logger.error("UserController profile method. User with id " + id + " was not found");
            throw new NotFoundException("User with id " + id + " was not found");
        }

        model.addAttribute("user", user);

        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("relationshipStatus", relationshipService.getRelationshipStatus(loginUser.getId(), id));
        }

        return "profile";
    }
}
