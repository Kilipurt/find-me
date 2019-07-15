package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.Message;
import com.findme.models.User;
import com.findme.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MessageControllerStatus {

    private MessageService messageService;

    private Logger logger = Logger.getLogger(MessageControllerStatus.class);

    @Autowired
    public MessageControllerStatus(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/delete-messages", method = RequestMethod.PUT)
    public ResponseEntity<String> deleteMessages(HttpSession session, @RequestParam String userId,
                                                 @RequestBody List<Message> messages) throws Exception {
        long id = Long.parseLong(userId);
        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(id, loggedInUser.getId());

        messageService.updateDateDeletedForList(messages);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-chat/", method = RequestMethod.PUT)
    public ResponseEntity<String> deleteChat(HttpSession session, @RequestParam String loggedInUserId,
                                             @RequestParam String userId) throws Exception {
        long id = Long.parseLong(loggedInUserId);
        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(id, loggedInUser.getId());

        messageService.deleteChat(id, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-chats/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getChats(HttpSession session, Model model, @PathVariable String id) throws Exception {
        User loggedInUser = (User) session.getAttribute("user");
        long userId = Long.parseLong(id);

        validateUserId(userId, loggedInUser.getId());

        model.addAttribute("users", messageService.getChatsByUserId(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-messages", method = RequestMethod.GET)
    public ResponseEntity<String> getMessages(@RequestParam String loggedInUserId, @RequestParam String userId,
                                              @RequestParam String offset, HttpSession session, Model model)
            throws Exception {
        User loggedInUser = (User) session.getAttribute("user");
        long id = Long.parseLong(loggedInUserId);

        validateUserId(id, loggedInUser.getId());

        long userIdLong = Long.parseLong(userId);
        long offsetLong = Long.parseLong(offset);

        model.addAttribute("messages", messageService.getMessagesByUsersId(id, userIdLong, offsetLong));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/send-message", method = RequestMethod.POST)
    public ResponseEntity<String> send(HttpSession session, @RequestBody Message message) throws Exception {
        logger.info("MessageControllerStatus. send method. Send message.");
        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/read-message", method = RequestMethod.PUT)
    public ResponseEntity<String> read(HttpSession session, @RequestBody Message message) throws Exception {
        logger.info("MessageControllerStatus. read method. Read message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.read(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/edit-message", method = RequestMethod.PUT)
    public ResponseEntity<String> edit(HttpSession session, @RequestBody Message message) throws Exception {
        logger.info("MessageControllerStatus. edit method. edit message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.edit(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-message", method = RequestMethod.PUT)
    public ResponseEntity<String> updateDateDeleted(HttpSession session, @RequestBody Message message)
            throws Exception {
        logger.info("MessageControllerStatus. updateDateDeleted method. Update date deleted of message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.updateDateDeleted(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateUserId(long loggedInUserId, long userFromId) throws BadRequestException {
        if (loggedInUserId != userFromId) {
            logger.error("MessageControllerStatus. User " + loggedInUserId + " does not have enough rights");
            throw new BadRequestException("User " + loggedInUserId + " does not have enough rights");
        }
    }
}
