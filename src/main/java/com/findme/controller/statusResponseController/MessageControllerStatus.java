package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.Message;
import com.findme.models.User;
import com.findme.service.MessageService;
import com.findme.util.JsonUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Log4j
public class MessageControllerStatus {

    private MessageService messageService;
    private JsonUtil jsonUtil;

    @Autowired
    public MessageControllerStatus(MessageService messageService, JsonUtil jsonUtil) {
        this.messageService = messageService;
        this.jsonUtil = jsonUtil;
    }

    @RequestMapping(path = "/delete-messages", method = RequestMethod.PUT)
    public ResponseEntity<String> deleteMessages(@RequestBody List<Long> messagesId) throws Exception {
        messageService.deleteMessages(messagesId);
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

    @RequestMapping(path = "/get-chats", method = RequestMethod.GET)
    public ResponseEntity<String> getChats(HttpSession session) throws Exception {
        User loggedInUser = (User) session.getAttribute("user");
        List<User> chats = messageService.getChatsByUserId(loggedInUser.getId());
        return new ResponseEntity<>(jsonUtil.toJson(chats), HttpStatus.OK);
    }

    @RequestMapping(path = "/get-messages", method = RequestMethod.GET)
    public ResponseEntity<String> getMessages(@RequestParam String userId, @RequestParam String offset,
                                              HttpSession session) throws Exception {
        User loggedInUser = (User) session.getAttribute("user");

        long userIdLong = Long.parseLong(userId);
        long offsetLong = Long.parseLong(offset);

        List<Message> messages = messageService.getMessagesByUsersId(loggedInUser.getId(), userIdLong, offsetLong);
        return new ResponseEntity<>(jsonUtil.toJson(messages), HttpStatus.OK);
    }

    @RequestMapping(path = "/send-message", method = RequestMethod.POST)
    public ResponseEntity<String> send(HttpSession session, @RequestBody Message message) throws Exception {
        log.info("MessageControllerStatus. send method. Send message.");
        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/read-message", method = RequestMethod.PUT)
    public ResponseEntity<String> read(HttpSession session, @RequestBody Message message) throws Exception {
        log.info("MessageControllerStatus. read method. Read message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.read(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/edit-message", method = RequestMethod.PUT)
    public ResponseEntity<String> edit(HttpSession session, @RequestBody Message message) throws Exception {
        log.info("MessageControllerStatus. edit method. edit message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.edit(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-message", method = RequestMethod.PUT)
    public ResponseEntity<String> deleteSingleMessage(HttpSession session, @RequestBody Message message)
            throws Exception {
        log.info("MessageControllerStatus. deleteSingleMessage method. Update date deleted of message.");

        User loggedInUser = (User) session.getAttribute("user");

        validateUserId(loggedInUser.getId(), message.getUserFrom().getId());

        messageService.deleteSingleMessage(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateUserId(long loggedInUserId, long userFromId) throws BadRequestException {
        if (loggedInUserId != userFromId) {
            log.error("MessageControllerStatus. User " + loggedInUserId + " does not have enough rights");
            throw new BadRequestException("User " + loggedInUserId + " does not have enough rights");
        }
    }
}