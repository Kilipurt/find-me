package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
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

        Relationship newRelationship = new Relationship();
        newRelationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());
        newRelationship.setUserFrom(userDAO.findById(userIdFrom));
        newRelationship.setUserTo(userTo);

        return relationshipDAO.save(newRelationship);
    }

    public Relationship update(long userIdFrom, long userIdTo, String status, User loggedInUser)
            throws InternalServerError, BadRequestException {
        validateUsersId(userIdFrom, userIdTo);

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        validateLoggedInUser(loggedInUser, userIdFrom, userIdTo, status);
        validateRelationship(userIdFrom, userIdTo, status, relationship);

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        return relationshipDAO.update(relationship);
    }

    private void validateLoggedInUser(User loggedInUser, long userIdFrom, long userIdTo, String status) throws BadRequestException {
        if (loggedInUser == null) {
            throw new BadRequestException("User is not authorized");
        }

        if (loggedInUser.getId() != userIdTo || loggedInUser.getId() != userIdFrom) {
            throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
        }

        if (loggedInUser.getId() == userIdFrom && !(status.equals(RelationshipStatus.DELETED.toString())
                || status.equals(RelationshipStatus.REQUEST_SENT.toString()))) {
            throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
        }

        if (loggedInUser.getId() == userIdTo && !(status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString()))) {
            throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
        }
    }

    private void validateRelationship(long userIdFrom, long userIdTo, String status, Relationship relationship) throws BadRequestException {
        if (relationship == null) {
            throw new BadRequestException("Relationship between user "
                    + userIdFrom + " and user " + userIdTo + " is not exist");
        }

        if (status.equals(relationship.getStatus())) {
            throw new BadRequestException("It's impossible to update relationship to the same status");
        }

        if (relationship.getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())
                && !(status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString())
                || status.equals(RelationshipStatus.DELETED.toString()))) {
            throw new BadRequestException("It's impossible to update relationship from status " +
                    "REQUEST_SENT to other expect FRIENDS or REQUEST_DECLINED or DELETED");
        }

        if (relationship.getStatus().equals(RelationshipStatus.FRIENDS.toString())
                && !status.equals(RelationshipStatus.PAST_FRIENDS.toString())) {
            throw new BadRequestException("It's impossible to update relationship from status " +
                    "FRIENDS to other expect PAST_FRIENDS");
        }

        if (relationship.getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString())
                && !status.equals(RelationshipStatus.REQUEST_SENT.toString())) {
            throw new BadRequestException("It's impossible to update relationship from status " +
                    "REQUEST_DECLINED to other expect REQUEST_SENT");
        }

        if (relationship.getStatus().equals(RelationshipStatus.PAST_FRIENDS.toString())
                && !status.equals(RelationshipStatus.REQUEST_SENT.toString())) {
            throw new BadRequestException("It's impossible to update relationship from status " +
                    "PAST_FRIENDS to other expect REQUEST_SENT");
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