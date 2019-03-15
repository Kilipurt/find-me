package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.UnauthorizedException;
import com.findme.models.Relationship;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Relationship newRelationship = new Relationship();
        newRelationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());
        newRelationship.setUserFrom(userDAO.findById(userIdFrom));
        newRelationship.setUserTo(userTo);

        return relationshipDAO.save(newRelationship);
    }

    public Relationship update(long userIdFrom, long userIdTo, String status, User loggedInUser)
            throws InternalServerError, BadRequestException, UnauthorizedException {
        validateUsersId(userIdFrom, userIdTo);

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        validateLoggedInUser(loggedInUser, userIdFrom, userIdTo, status);
        validateRelationship(userIdFrom, userIdTo, status, relationship);

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        return relationshipDAO.update(relationship);
    }

    private void validateLoggedInUser(User loggedInUser, long userIdFrom, long userIdTo, String status)
            throws BadRequestException, UnauthorizedException {
        if (loggedInUser == null) {
            throw new UnauthorizedException("User is not authorized");
        }

        if (loggedInUser.getId() == userIdTo || loggedInUser.getId() == userIdFrom) {
            return;
        }

        if (loggedInUser.getId() == userIdFrom
                && (status.equals(RelationshipStatus.DELETED.toString())
                || status.equals(RelationshipStatus.REQUEST_SENT.toString()))) {
            return;
        }

        if (loggedInUser.getId() == userIdTo
                && (status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString()))) {
            return;
        }

        throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
    }

    private void validateRelationship(long userIdFrom, long userIdTo, String status, Relationship relationship) throws BadRequestException {
        if (relationship == null) {
            throw new BadRequestException("Relationship between users "
                    + userIdFrom + " and " + userIdTo + " is not exist");
        }

        if (relationship.getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())
                && (status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString())
                || status.equals(RelationshipStatus.DELETED.toString()))) {
            return;
        }

        if (relationship.getStatus().equals(RelationshipStatus.FRIENDS.toString())
                && status.equals(RelationshipStatus.PAST_FRIENDS.toString())) {
            return;
        }

        if (relationship.getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString())
                && status.equals(RelationshipStatus.REQUEST_SENT.toString())) {
            return;
        }

        if (relationship.getStatus().equals(RelationshipStatus.PAST_FRIENDS.toString())
                && status.equals(RelationshipStatus.REQUEST_SENT.toString())) {
            return;
        }

        throw new BadRequestException("It's impossible to update relationship to the status " + status);
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