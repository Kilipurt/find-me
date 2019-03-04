package com.findme.service;

import com.findme.dao.RelationshipDAO;
import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.Relationship;
import com.findme.models.RelationshipKey;
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
        //validateUsersId(userIdFrom, userIdTo);
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

        User userFrom = userDAO.findById(userIdFrom);
        User userTo = userDAO.findById(userIdTo);

        if (userFrom == null || userTo == null) {
            throw new BadRequestException("One of user who want to save relationship does not exist");
        }

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        if (relationship != null
                && (!relationship.getStatus().equals(RelationshipStatus.FRIENDS.toString())
                || !relationship.getStatus().equals(RelationshipStatus.REQUEST_SENT.toString()))) {

            relationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());
            return relationshipDAO.update(relationship);

        } else {

            Relationship newRelationship = new Relationship();
            newRelationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());

            RelationshipKey newRelationshipKey = new RelationshipKey();
            newRelationshipKey.setUserFrom(userFrom);
            newRelationshipKey.setUserTo(userTo);

            newRelationship.setRelationshipKey(newRelationshipKey);

            return relationshipDAO.save(newRelationship);
        }
    }

    public Relationship update(long userIdFrom, long userIdTo, String status, User loggedInUser)
            throws InternalServerError, BadRequestException {
        validateForUpdate(status, loggedInUser, userIdFrom, userIdTo);

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        if (relationship == null) {
            throw new BadRequestException("Relationship between user "
                    + userIdFrom + " and user " + userIdTo + " is not exist");
        }

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        return relationshipDAO.update(relationship);
    }

    private void validateForUpdate(String status, User loggedInUser, long userIdFrom, long userIdTo)
            throws BadRequestException, InternalServerError {

        validateUsersId(userIdFrom, userIdTo);

        User userFrom = userDAO.findById(userIdFrom);
        User userTo = userDAO.findById(userIdTo);

        if (userFrom == null || userTo == null) {
            throw new BadRequestException("One of user who want to update relationship does not exist");
        }

        if (status.equals(RelationshipStatus.FRIENDS.toString()) && !loggedInUser.getId().equals(userTo.getId())) {
            throw new BadRequestException("User " + loggedInUser.getId()
                    + " has not enough rights to accept request from user " + userFrom.getId());
        }

        if (status.equals(RelationshipStatus.REQUEST_DENIED.toString()) && !loggedInUser.getId().equals(userTo.getId())) {
            throw new BadRequestException("User " + loggedInUser.getId()
                    + " has not enough rights to deny request from user " + userFrom.getId());
        }

        if (status.equals(RelationshipStatus.PAST_FRIENDS.toString())
                && (!loggedInUser.getId().equals(userFrom.getId())
                || !loggedInUser.getId().equals(userTo.getId()))) {
            throw new BadRequestException("User " + loggedInUser.getId()
                    + " has not enough rights to delete user " + userFrom.getId() + " from friends");
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