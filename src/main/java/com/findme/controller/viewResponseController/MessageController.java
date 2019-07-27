package com.findme.controller.viewResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class MessageController {

    @RequestMapping(path = "/chats/{id}", method = RequestMethod.GET)
    public String getChats(HttpSession session, @PathVariable(name = "id") String id) throws Exception {
        User loggedInUser = (User) session.getAttribute("user");
        long loggedInUserId = Long.parseLong(id);

        if (loggedInUserId != loggedInUser.getId()) {
            throw new BadRequestException("User " + loggedInUser.getId() + " does not have enough rights");
        }

        return "chats";
    }
}