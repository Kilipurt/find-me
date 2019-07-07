package com.findme.service;

import com.findme.dao.MessageDAO;
import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.util.Date;

@Service
public class MessageService {

    private MessageDAO messageDAO;
    private RelationshipDAO relationshipDAO;

    private Logger logger = Logger.getLogger(MessageService.class);

    @Autowired
    public MessageService(MessageDAO messageDAO, RelationshipDAO relationshipDAO) {
        this.messageDAO = messageDAO;
        this.relationshipDAO = relationshipDAO;
    }

    public Message send(Message message) throws BadRequestException, InternalServerError {
        logger.info("MessageService. send method. Send message from user " + message.getUserFrom().getId() + " to user "
                + message.getUserTo().getId());

        if (message.getText().length() > 140) {
            logger.error("MessageService. send method. Message " + message.getId() + " too long");
            throw new BadRequestException("Message " + message.getId() + " too long");
        }

        long userFromId = message.getUserFrom().getId();
        long userToId = message.getUserTo().getId();

        if (relationshipDAO.getFriendRelationshipByUsersId(userFromId, userToId) == null) {
            logger.error("MessageService. send method. You can send message only to friends");
            throw new BadRequestException("You can send message only to friends");
        }

        message.setDateSent(new Date());
        return messageDAO.save(message);
    }

    public Message read(Message message) throws InternalServerError, BadRequestException {
        logger.info("MessageService. read method. Read message " + message.getId());

        if (message.getDateRead() != null) {
            logger.error("MessageService. read method. Message " + message.getId() + " already read");
            throw new BadRequestException("Message " + message.getId() + " already read");
        }

        message.setDateRead(new Date());
        return messageDAO.update(message);
    }

    public Message edit(Message message) throws BadRequestException, InternalServerError {
        logger.info("MessageService. edit method. Edit message " + message.getId());

        if (message.getDateRead() != null) {
            logger.error("MessageService. edit method. Message " + message.getId() + " was read");
            throw new BadRequestException("Message " + message.getId() + " was read");
        }

        if (message.getText().length() > 140) {
            logger.error("MessageService. edit method. Message " + message.getId() + " too long");
            throw new BadRequestException("Message " + message.getId() + " too long");
        }

        message.setDateEdited(new Date());
        return messageDAO.update(message);
    }

    public void updateDateDeleted(Message message) throws BadRequestException, InternalServerError {
        logger.info("MessageService. updateDateDeleted method. Delete message " + message.getId());

        if (message.getDateRead() != null) {
            logger.error("MessageService. updateDateDeleted method. Message " + message.getId() + " was read");
            throw new BadRequestException("Message " + message.getId() + " was read");
        }

        if (message.getDateDeleted() != null) {
            logger.error("MessageService. updateDateDeleted method. Message " + message.getId() + " already deleted");
            throw new BadRequestException("Message " + message.getId() + " already deleted");
        }

        message.setDateDeleted(new Date());
        messageDAO.update(message);
    }
}
