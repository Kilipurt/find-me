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

        if (relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo) != null) {
            throw new BadRequestException("Relationship between users " + userIdFrom + " and " + userIdTo + " already exists");
        }

        validateForSave(userIdFrom);

        Relationship newRelationship = new Relationship();
        newRelationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());
        newRelationship.setUserFrom(userDAO.findById(userIdFrom));
        newRelationship.setUserTo(userTo);
        newRelationship.setLastStatusChange(new Date());

        return relationshipDAO.save(newRelationship);
    }

    public Relationship update(long userIdFrom, long userIdTo, String status, User loggedInUser)
            throws InternalServerError, BadRequestException, UnauthorizedException {
        validateUsersId(userIdFrom, userIdTo);

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        validateForUpdate(userIdFrom, userIdTo, status, loggedInUser, relationship);

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        relationship.setLastStatusChange(new Date());

        return relationshipDAO.update(relationship);
    }

    private void validateForSave(long userIdFrom) throws InternalServerError, BadRequestException {
        ValidationData validationData = new ValidationData();
        validationData.setUserIdFrom(userIdFrom);

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

    private void validateForUpdate(long userIdFrom, long userIdTo, String status, User loggedInUser, Relationship relationship)
            throws InternalServerError, BadRequestException, UnauthorizedException {

        ValidationData validationData = new ValidationData();
        validationData.setUserIdFrom(userIdFrom);
        validationData.setUserIdTo(userIdTo);
        validationData.setRelationship(relationship);
        validationData.setLoggedInUser(loggedInUser);
        validationData.setStatus(status);
        validationData.setUserFromFriendsCount(relationshipDAO.getUserFriendsCount(userIdFrom));
        validationData.setUserToFriendsCount(relationshipDAO.getUserFriendsCount(userIdTo));
        validationData.setOutcomeRequestsCount(relationshipDAO.getOutcomeRequestsCount(userIdFrom));

        GeneralValidator relationshipStatusValidator = new RelationshipStatusValidator();
        GeneralValidator loggedInUserValidator = new LoggedInUserValidator();
        relationshipStatusValidator.linkWith(loggedInUserValidator);

        if (RelationshipStatus.PAST_FRIENDS.toString().equals(status)) {
            loggedInUserValidator.linkWith(new FriendshipTimeValidator());
        }

        if (RelationshipStatus.FRIENDS.toString().equals(status)) {
            loggedInUserValidator.linkWith(new MaxFriendsValidator());
        }

        if (RelationshipStatus.REQUEST_SENT.toString().equals(status)) {
            loggedInUserValidator.linkWith(new MaxOutcomeRequestValidator());
        }

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