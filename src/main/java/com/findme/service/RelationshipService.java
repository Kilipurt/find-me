package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.UnauthorizedException;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import com.findme.models.ValidationData;
import com.findme.service.updateValidation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RelationshipService {

    private RelationshipDAO relationshipDAO;
    private UserDAO userDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO, UserDAO userDAO) {
        this.relationshipDAO = relationshipDAO;
        this.userDAO = userDAO;
    }

    public String getRelationshipStatus(long userIdFrom, long userIdTo) throws InternalServerError {
        if (userIdFrom == userIdTo) {
            return "You are friend to yourself";
        }

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        if (relationship == null) {
            return "Not friends";
        }

        return relationship.getStatus();
    }

    public Relationship save(long userIdFrom, long userIdTo) throws InternalServerError, BadRequestException {
        validateUsersId(userIdFrom, userIdTo);

        User userTo = userDAO.findById(userIdTo);

        if (userTo == null) {
            throw new BadRequestException("User who receives friend request does not exist");
        }

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        if (relationship != null) {
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

    public Relationship update(long userIdFrom, long userIdTo, String status, User loggedInUser)
            throws InternalServerError, BadRequestException, UnauthorizedException {
        validateUsersId(userIdFrom, userIdTo);

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        validateForUpdate(status, loggedInUser, relationship);

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        relationship.setLastStatusChange(new Date());

        return relationshipDAO.update(relationship);
    }

    private void validateForSave(Relationship relationship) throws InternalServerError, BadRequestException {
        ValidationData validationData = new ValidationData();
        validationData.setRelationship(relationship);

        GeneralValidator maxFriendsValidator = new MaxFriendsValidator();

        try {
            maxFriendsValidator.validate(validationData);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    private void validateForUpdate(String status, User loggedInUser, Relationship relationship)
            throws InternalServerError, BadRequestException, UnauthorizedException {

        long userIdFrom = relationship.getUserFrom().getId();
        long userIdTo = relationship.getUserTo().getId();

        ValidationData validationData = new ValidationData();
        validationData.setRelationship(relationship);
        validationData.setLoggedInUser(loggedInUser);
        validationData.setStatus(status);
        validationData.setUserFromFriendsCount(relationshipDAO.getUserFriendsCount(userIdFrom));
        validationData.setUserToFriendsCount(relationshipDAO.getUserFriendsCount(userIdTo));
        validationData.setOutcomeRequestsCount(relationshipDAO.getOutcomeRequestsCount(userIdFrom));

        GeneralValidator relationshipStatusValidator = new RelationshipStatusValidator();
        relationshipStatusValidator.linkWith(new LoggedInUserValidator()
                .linkWith(new FriendshipTimeValidator()
                        .linkWith(new MaxFriendsValidator()
                                .linkWith(new MaxOutcomeRequestValidator()))));

        try {
            relationshipStatusValidator.validate(validationData);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    private void validateUsersId(long userIdFrom, long userIdTo) throws BadRequestException {
        if (userIdFrom <= 0) {
            throw new BadRequestException("Wrong user's id " + userIdFrom);
        }

        if (userIdTo <= 0) {
            throw new BadRequestException("Wrong user's id " + userIdTo);
        }

        if (userIdFrom == userIdTo) {
            throw new BadRequestException("User " + userIdFrom + " can't sent request to himself");
        }
    }
}