package com.findme.controller.viewResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {
    private Logger logger = Logger.getLogger(PostController.class);

    @RequestMapping(path = "/feed/{loggedInUserId}", method = RequestMethod.GET)
    public String getFeed(HttpSession session, @PathVariable() String loggedInUserId) throws Exception {
        logger.info("PostController getFeed method. Moving to feed page");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser.getId() != Long.parseLong(loggedInUserId)) {
            logger.error("PostController getFeed method. User does not have enough rights");
            throw new BadRequestException("User does not have enough rights");
        }

        return "feed";
    }
}
