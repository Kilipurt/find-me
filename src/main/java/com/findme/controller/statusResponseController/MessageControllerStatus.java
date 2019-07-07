package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.Message;
import com.findme.models.User;
import com.findme.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class MessageControllerStatus {

    private MessageService messageService;

    private Logger logger = Logger.getLogger(MessageControllerStatus.class);

    @Autowired
    public MessageControllerStatus(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(path = "/send-message", method = RequestMethod.POST)
    public ResponseEntity<String> send(HttpSession session, @RequestBody Message message) throws Exception {
        logger.info("MessageControllerStatus. send method. Send message.");
        User loggedInUser = (User) session.getAttribute("user");

        validateUserFrom(loggedInUser, message.getUserFrom());

        messageService.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/read-message", method = RequestMethod.PUT)
    public ResponseEntity<String> read(HttpSession session, @RequestBody Message message) throws Exception {
        logger.info("MessageControllerStatus. read method. Read message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserFrom(loggedInUser, message.getUserFrom());

        messageService.read(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/edit-message", method = RequestMethod.PUT)
    public ResponseEntity<String> edit(HttpSession session, @RequestBody Message message) throws Exception {
        logger.info("MessageControllerStatus. edit method. edit message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserFrom(loggedInUser, message.getUserFrom());

        messageService.edit(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-message", method = RequestMethod.PUT)
    public ResponseEntity<String> updateDateDeleted(HttpSession session, @RequestBody Message message)
            throws Exception {
        logger.info("MessageControllerStatus. updateDateDeleted method. Update date deleted of message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserFrom(loggedInUser, message.getUserFrom());

        messageService.updateDateDeleted(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateUserFrom(User loggedInUser, User userFrom) throws BadRequestException {
        if (!loggedInUser.getId().equals(userFrom.getId())) {
            logger.error("MessageControllerStatus. User " + loggedInUser.getId() + " does not have enough rights");
            throw new BadRequestException("User " + loggedInUser.getId() + " does not have enough rights");
        }
    }
}
