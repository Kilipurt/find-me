package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.exception.UnauthorizedException;
import com.findme.models.RelationshipStatus;

public class LoggedInUserValidator extends Criteria {

    public void validate()
            throws BadRequestException, UnauthorizedException {
        if (getValidationData().getLoggedInUser() == null) {
            throw new UnauthorizedException("User is not authorized");
        }

        if (getValidationData().getLoggedInUser().getId() == getValidationData().getUserIdTo()
                || getValidationData().getLoggedInUser().getId() == getValidationData().getUserIdFrom()) {
            return;
        }

        if (getValidationData().getLoggedInUser().getId() == getValidationData().getUserIdFrom()
                && (getValidationData().getStatus().equals(RelationshipStatus.DELETED.toString())
                || getValidationData().getStatus().equals(RelationshipStatus.REQUEST_SENT.toString()))) {
            return;
        }

        if (getValidationData().getLoggedInUser().getId() == getValidationData().getUserIdTo()
                && (getValidationData().getStatus().equals(RelationshipStatus.FRIENDS.toString())
                || getValidationData().getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString()))) {
            return;
        }

        throw new BadRequestException("User " + getValidationData().getLoggedInUser().getId() + " has not enough rights");
    }
}
