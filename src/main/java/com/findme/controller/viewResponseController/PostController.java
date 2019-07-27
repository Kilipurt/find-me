package com.findme.controller.viewResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@Log4j
public class PostController {

    @RequestMapping(path = "/feed/{loggedInUserId}", method = RequestMethod.GET)
    public String getFeed(HttpSession session, @PathVariable String loggedInUserId) throws Exception {
        log.info("PostController getFeed method. Moving to feed page");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser.getId() != Long.parseLong(loggedInUserId)) {
            log.error("PostController getFeed method. User does not have enough rights");
            throw new BadRequestException("User does not have enough rights");
        }

        return "feed";
    }
}
