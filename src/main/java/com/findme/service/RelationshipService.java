package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import com.findme.models.ValidationData;
import com.findme.service.updateRelationshipValidation.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j
public class RelationshipService {

    private RelationshipDAO relationshipDAO;
    private UserDAO userDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO, UserDAO userDAO) {
        this.relationshipDAO = relationshipDAO;
        this.userDAO = userDAO;
    }

    public Relationship getRelationshipByUsersId(long userIdFrom, long userIdTo) throws InternalServerError {
        log.info("RelationshipService getRelationshipStatus method. Selecting status of relationship between user "
                + userIdFrom + " and " + userIdTo);

        if (userIdFrom == userIdTo) {
            return null;
        }

        return relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);
    }

    public Relationship save(long userIdFrom, long userIdTo) throws InternalServerError, BadRequestException {
        log.info("RelationshipService save method. Saving relationship between user " + userIdFrom + " and "
                + userIdTo);

        validateUsersId(userIdFrom, userIdTo);

        User userTo = userDAO.findById(userIdTo);

        if (userTo == null) {
            log.error("RelationshipService save method. User who receives friend request does not exist");
            throw new BadRequestException("User who receives friend request does not exist");
        }

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        if (relationship != null) {
            log.error("RelationshipService save method. Relationship between users " + userIdFrom + " and " + userIdTo +
                    " already exists");

            throw new BadRequestException("Relationship between users " + userIdFrom + " and " + userIdTo
                    + " already exists");
        }

        Relationship newRelationship = new Relationship();
        newRelationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());
        newRelationship.setUserFrom(userDAO.findById(userIdFrom));
        newRelationship.setUserTo(userTo);
        newRelationship.setLastStatusChange(new Date());

        validateForSave(newRelationship);
        return relationshipDAO.save(newRelationship);
    }

    public Relationship update(long userIdFrom, long userIdTo, String status)
            throws InternalServerError, BadRequestException {
        log.info("RelationshipService update method. Updating relationship between users " + userIdFrom + " and "
                + userIdTo);

        validateUsersId(userIdFrom, userIdTo);

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        validateForUpdate(status, relationship);

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        relationship.setLastStatusChange(new Date());

        return relationshipDAO.update(relationship);
    }

    private void validateForSave(Relationship relationship) throws InternalServerError, BadRequestException {
        log.info("RelationshipService validateForSave method. Validating for saving relationship");
        ValidationData validationData = new ValidationData();
        validationData.setRelationship(relationship);

        GeneralValidator maxFriendsValidator = new MaxFriendsValidator();

        try {
            maxFriendsValidator.validate(validationData);
        } catch (BadRequestException e) {
            log.error("RelationshipService validateForSave method. " + e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            log.error("RelationshipService validateForSave method. " + e.getMessage());
            throw new InternalServerError(e.getMessage());
        }
    }

    private void validateForUpdate(String status, Relationship relationship)
            throws InternalServerError, BadRequestException {

        log.info("RelationshipService validateForUpdate method. Validation for updating relationship");
        long userIdFrom = relationship.getUserFrom().getId();
        long userIdTo = relationship.getUserTo().getId();

        ValidationData validationData = new ValidationData();
        validationData.setRelationship(relationship);
        validationData.setStatus(status);
        validationData.setUserFromFriendsCount(relationshipDAO.getUserFriendsCount(userIdFrom));
        validationData.setUserToFriendsCount(relationshipDAO.getUserFriendsCount(userIdTo));
        validationData.setOutcomeRequestsCount(relationshipDAO.getOutcomeRequestsCount(userIdFrom));

        GeneralValidator relationshipStatusValidator = new RelationshipStatusValidator();
        relationshipStatusValidator.linkWith(new FriendshipTimeValidator().linkWith(new MaxFriendsValidator()
                .linkWith(new MaxOutcomeRequestValidator())));

        try {
            relationshipStatusValidator.validate(validationData);
        } catch (BadRequestException e) {
            log.error("RelationshipService validateForUpdate method. " + e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            log.error("RelationshipService validateForUpdate method. " + e.getMessage());
            throw new InternalServerError(e.getMessage());
        }
    }

    private void validateUsersId(long userIdFrom, long userIdTo) throws BadRequestException {
        log.info("RelationshipService validateUsersId method. Validating users id " + userIdFrom + " and " + userIdTo);

        if (userIdTo <= 0) {
            log.error("RelationshipService validateUsersId method. Wrong user's id " + userIdTo);
            throw new BadRequestException("Wrong user's id " + userIdTo);
        }

        if (userIdFrom == userIdTo) {
            log.error("RelationshipService validateUsersId method. User " + userIdFrom + " can't send request to " +
                    "himself");
            throw new BadRequestException("User " + userIdFrom + " can't sent request to himself");
        }
    }
}