package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;
import org.apache.log4j.Logger;

public class LoggedInUserValidator extends GeneralValidator {

    private Logger logger = Logger.getLogger(LoggedInUserValidator.class);

    public void validate(ValidationData validationData) throws Exception {
        long userIdTo =  validationData.getRelationship().getUserTo().getId();
        long userIdFrom = validationData.getRelationship().getUserFrom().getId();
        long loggedInUserId = validationData.getLoggedInUser().getId();
        String status = validationData.getStatus();

        if (loggedInUserId == userIdTo || loggedInUserId == userIdFrom) {
            validateNext(validationData);
        }

        if (loggedInUserId == userIdFrom
                && (status.equals(RelationshipStatus.DELETED.toString())
                || status.equals(RelationshipStatus.REQUEST_SENT.toString()))) {
            validateNext(validationData);
        }

        if (loggedInUserId == userIdTo
                && (status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString()))) {
            validateNext(validationData);
        }

        logger.error("LoggedInUserValidator validate method. User " + validationData.getLoggedInUser().getId()
                + " has not enough rights");
        throw new BadRequestException("User " + validationData.getLoggedInUser().getId() + " has not enough rights");
    }
}
