package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.Relationship;

import java.util.Date;

public class FriendshipTimeValidator extends Criteria {

    @Override
    public void validate() throws InternalServerError, BadRequestException {
        Relationship relationship = getRelationshipDAO().getRelationshipByUsersId(getValidationData().getUserIdFrom(), getValidationData().getUserIdTo());

        long milliseconds = new Date().getTime() - relationship.getLastStatusChange().getTime();
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

        if (days < 3) {
            throw new BadRequestException("You must be friends at least 3 days " +
                    "in order to delete this user from friends list");
        }
    }
}
