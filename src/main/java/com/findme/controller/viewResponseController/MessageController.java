package com.findme.controller.viewResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.User;
import com.findme.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/chats/{id}", method = RequestMethod.GET)
    public String getChats(Model model, HttpSession session, @PathVariable(name = "id") String id) throws Exception {
        User loggedInUser = (User) session.getAttribute("user");
        long loggedInUserId = Long.parseLong(id);

        if (loggedInUserId != loggedInUser.getId()) {
            throw new BadRequestException("User " + loggedInUser.getId() + " does not have enough rights");
        }

        model.addAttribute("users", messageService.getChatsByUserId(loggedInUserId));

        return "chats";
    }

    @RequestMapping(path = "/open-chat/{id}", method = RequestMethod.GET)
    public String openChat(Model model, HttpSession session, @PathVariable(name = "id") String id) throws Exception {
        User loggedInUser = (User) session.getAttribute("user");
        long userId = Long.parseLong(id);

        model.addAttribute("messages", messageService.getMessagesByUsersId(loggedInUser.getId(), userId, 0));

        return "messages";
    }
}