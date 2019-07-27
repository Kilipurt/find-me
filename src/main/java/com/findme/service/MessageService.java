package com.findme.service;

import com.findme.dao.MessageDAO;
import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.Message;
import com.findme.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j
public class MessageService {

    private MessageDAO messageDAO;
    private RelationshipDAO relationshipDAO;

    @Autowired
    public MessageService(MessageDAO messageDAO, RelationshipDAO relationshipDAO) {
        this.messageDAO = messageDAO;
        this.relationshipDAO = relationshipDAO;
    }

    public void deleteChat(long loggedInUserId, long userId) throws BadRequestException, InternalServerError {
        if (userId <= 0) {
            throw new BadRequestException("Wrong id " + userId);
        }

        messageDAO.deleteChat(loggedInUserId, userId);
    }

    public void deleteMessages(List<Long> messagesId) throws BadRequestException, InternalServerError {
        if (messagesId.size() > 10) {
            throw new BadRequestException("User can delete only 10 messages by step");
        }

        messageDAO.deleteMessages(messagesId);
    }

    public List<User> getChatsByUserId(long loggedInUserId) throws InternalServerError {
        return messageDAO.getChatsByUserId(loggedInUserId);
    }

    public List<Message> getMessagesByUsersId(long firstUserId, long secondUserId, long offset)
            throws InternalServerError, BadRequestException {
        if (firstUserId <= 0) {
            throw new BadRequestException("Wrong id " + firstUserId);
        }

        if (secondUserId <= 0) {
            throw new BadRequestException("Wrong id " + secondUserId);
        }

        if (offset < 0) {
            throw new BadRequestException("Wrong offset " + offset);
        }

        return messageDAO.getMessagesByUsersId(firstUserId, secondUserId, offset);
    }

    public Message send(Message message) throws BadRequestException, InternalServerError {
        log.info("MessageService. send method. Send message from user " + message.getUserFrom().getId() + " to user "
                + message.getUserTo().getId());

        if (message.getText().length() > 140) {
            log.error("MessageService. send method. Message " + message.getId() + " too long");
            throw new BadRequestException("Message " + message.getId() + " too long");
        }

        long userFromId = message.getUserFrom().getId();
        long userToId = message.getUserTo().getId();

        if (relationshipDAO.getFriendRelationshipByUsersId(userFromId, userToId) == null) {
            log.error("MessageService. send method. You can send message only to friends");
            throw new BadRequestException("You can send message only to friends");
        }

        message.setDateSent(new Date());
        return messageDAO.save(message);
    }

    public Message read(Message message) throws InternalServerError, BadRequestException {
        log.info("MessageService. read method. Read message " + message.getId());

        if (message.getDateRead() != null) {
            log.error("MessageService. read method. Message " + message.getId() + " already read");
            throw new BadRequestException("Message " + message.getId() + " already read");
        }

        message.setDateRead(new Date());
        return messageDAO.update(message);
    }

    public Message edit(Message message) throws BadRequestException, InternalServerError {
        log.info("MessageService. edit method. Edit message " + message.getId());

        if (message.getDateRead() != null) {
            log.error("MessageService. edit method. Message " + message.getId() + " was read");
            throw new BadRequestException("Message " + message.getId() + " was read");
        }

        if (message.getText().length() > 140) {
            log.error("MessageService. edit method. Message " + message.getId() + " too long");
            throw new BadRequestException("Message " + message.getId() + " too long");
        }

        message.setDateEdited(new Date());
        return messageDAO.update(message);
    }

    public void deleteSingleMessage(Message message) throws BadRequestException, InternalServerError {
        log.info("MessageService. deleteSingleMessage method. Delete message " + message.getId());

        if (message.getDateRead() != null) {
            log.error("MessageService. deleteSingleMessage method. Message " + message.getId() + " was read");
            throw new BadRequestException("Message " + message.getId() + " was read");
        }

        if (message.getDateDeleted() != null) {
            log.error("MessageService. deleteSingleMessage method. Message " + message.getId() + " already deleted");
            throw new BadRequestException("Message " + message.getId() + " already deleted");
        }

        message.setDateDeleted(new Date());
        messageDAO.update(message);
    }
}
