package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.exception.UnauthorizedException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;

public class LoggedInUserValidator extends GeneralValidator {

    public void validate(ValidationData validationData) throws Exception {

        if (validationData.getLoggedInUser() == null) {
            throw new UnauthorizedException("User is not authorized");
        }

        if (validationData.getLoggedInUser().getId() == validationData.getUserIdTo()
                || validationData.getLoggedInUser().getId() == validationData.getUserIdFrom()) {
            validateNext(validationData);
        }

        if (validationData.getLoggedInUser().getId() == validationData.getUserIdFrom()
                && (validationData.getStatus().equals(RelationshipStatus.DELETED.toString())
                || validationData.getStatus().equals(RelationshipStatus.REQUEST_SENT.toString()))) {
            validateNext(validationData);
        }

        if (validationData.getLoggedInUser().getId() == validationData.getUserIdTo()
                && (validationData.getStatus().equals(RelationshipStatus.FRIENDS.toString())
                || validationData.getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString()))) {
            validateNext(validationData);
        }

        throw new BadRequestException("User " + validationData.getLoggedInUser().getId() + " has not enough rights");
    }
}
