package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;
import org.apache.log4j.Logger;

public class RelationshipStatusValidator extends GeneralValidator {

    private Logger logger = Logger.getLogger(RelationshipStatusValidator.class);

    public void validate(ValidationData validationData) throws Exception {
        String status = validationData.getStatus();
        String oldStatus = validationData.getRelationship().getStatus();
        long userIdFrom = validationData.getRelationship().getUserFrom().getId();
        long userIdTo = validationData.getRelationship().getUserTo().getId();

        if (validationData.getRelationship() == null) {
            logger.error("Relationship between users " + userIdFrom + " and " + userIdTo + " is not exist");
            throw new BadRequestException("Relationship between users " + userIdFrom + " and " + userIdTo
                    + " is not exist");
        }

        if (oldStatus.equals(RelationshipStatus.REQUEST_SENT.toString())
                && (status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString())
                || status.equals(RelationshipStatus.DELETED.toString()))) {
            validateNext(validationData);
        }

        if (oldStatus.equals(RelationshipStatus.FRIENDS.toString())
                && status.equals(RelationshipStatus.PAST_FRIENDS.toString())) {
            validateNext(validationData);
        }

        if (oldStatus.equals(RelationshipStatus.REQUEST_DECLINED.toString())
                && status.equals(RelationshipStatus.REQUEST_SENT.toString())) {
            validateNext(validationData);
        }

        if (oldStatus.equals(RelationshipStatus.PAST_FRIENDS.toString())
                && status.equals(RelationshipStatus.REQUEST_SENT.toString())) {
            validateNext(validationData);
        }

        logger.error("RelationshipStatusValidator validate method. It's impossible to update relationship to the " +
                "status " + validationData.getStatus());
        throw new BadRequestException("It's impossible to update relationship to the status "
                + validationData.getStatus());
    }
}
