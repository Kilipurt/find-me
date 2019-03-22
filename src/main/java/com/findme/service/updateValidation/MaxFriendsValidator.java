package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;

public class MaxFriendsValidator extends Criteria {

    @Override
    public void validate() throws InternalServerError, BadRequestException {
        int userFromFriendsCount = getRelationshipDAO().getUserFriendsCount(getValidationData().getUserIdFrom());

        if (userFromFriendsCount == 100) {
            throw new BadRequestException("User " + getValidationData().getUserIdFrom() + " can has only 100 friends");
        }

        int userToFriendsCount = getRelationshipDAO().getUserFriendsCount(getValidationData().getUserIdTo());

        if (userToFriendsCount == 100) {
            throw new BadRequestException("User " + getValidationData().getUserIdTo() + " can has only 100 friends");
        }
    }
}
