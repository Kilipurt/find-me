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

import javax.persistence.criteria.CriteriaBuilder;

@Service
public class RelationshipService {

    private RelationshipDAO relationshipDAO;
    private UserDAO userDAO;

    @Autowired
    public RelationshipService(RelationshipDAO relationshipDAO, UserDAO userDAO) {
        this.relationshipDAO = relationshipDAO;
        this.userDAO = userDAO;
    }

    public String getRelationshipStatus(long userIdFrom, long userIdTo) throws BadRequestException, InternalServerError {
        validateUsersId(userIdFrom, userIdTo);
        return relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo).getStatus();
    }

    public Relationship save(long userIdFrom, long userIdTo) throws InternalServerError, BadRequestException {
        validateUsersId(userIdFrom, userIdTo);

        User userFrom = userDAO.findById(userIdFrom);
        User userTo = userDAO.findById(userIdTo);

        validateRelationshipForSave(userFrom, userTo);

        Relationship newRelationship = new Relationship();
        newRelationship.setStatus(RelationshipStatus.REQUEST_SENT.toString());
        newRelationship.setUserFrom(userFrom);
        newRelationship.setUserTo(userTo);

        return relationshipDAO.save(newRelationship);
    }

    public Relationship update(long userIdFrom, long userIdTo, String status, User loginUser)
            throws InternalServerError, BadRequestException {
        validateUsersId(userIdFrom, userIdTo);

        User userFrom = userDAO.findById(userIdFrom);
        User userTo = userDAO.findById(userIdTo);

        if (status.equals(RelationshipStatus.FRIENDS.toString()) && !loginUser.getId().equals(userTo.getId())) {
            throw new BadRequestException("User " + loginUser.getId()
                    + " has not enough rights to accept user " + userFrom.getId() + " to friends");
        }

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        if (relationship == null) {
            throw new BadRequestException("Relationship between user "
                    + userIdFrom + " and user " + userIdTo + " is not exist");
        }

        relationship.setStatus(RelationshipStatus.valueOf(status).toString());
        return relationshipDAO.update(relationship);
    }

    public void delete(long userIdFrom, long userIdTo) throws InternalServerError, BadRequestException {
        validateUsersId(userIdFrom, userIdTo);

        User userFrom = userDAO.findById(userIdFrom);
        User userTo = userDAO.findById(userIdTo);
        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userIdFrom, userIdTo);

        validateRelationshipForDelete(userFrom, userTo, relationship);

        relationshipDAO.delete(relationship.getId());
    }

    private void validateRelationshipForDelete(User userFrom, User userTo, Relationship relationship)
            throws BadRequestException {

        if (userFrom == null) {
            throw new BadRequestException("User who want to delete relationship does not exist");
        }

        if (userTo == null) {
            throw new BadRequestException("User who got friend request that must be deleted does not exist");
        }

        if (relationship == null) {
            throw new BadRequestException("Relationship between users " + userFrom.getId() + " and " + userTo.getId() + " does not exist");
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

    private void validateRelationshipForSave(User userFrom, User userTo)
            throws BadRequestException, InternalServerError {
        if (userFrom == null) {
            throw new BadRequestException("User who sent friend request does not not exist");
        }

        if (userTo == null) {
            throw new BadRequestException("User who will get friend request does not exist");
        }

        Relationship relationship = relationshipDAO.getRelationshipByUsersId(userFrom.getId(), userTo.getId());

        if (relationship != null) {
            throw new BadRequestException("Relationship between users " + userFrom.getId() + " and " + userTo.getId() + " already exist");
        }
    }
}