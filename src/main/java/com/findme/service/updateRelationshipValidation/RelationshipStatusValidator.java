package com.findme.service.updateRelationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;
import lombok.extern.log4j.Log4j;

@Log4j
public class RelationshipStatusValidator extends GeneralValidator {

    public void validate(ValidationData validationData) throws Exception {
        String status = validationData.getStatus();
        String oldStatus = validationData.getRelationship().getStatus();
        long userIdFrom = validationData.getRelationship().getUserFrom().getId();
        long userIdTo = validationData.getRelationship().getUserTo().getId();

        if (validationData.getRelationship() == null) {
            log.error("Relationship between users " + userIdFrom + " and " + userIdTo + " is not exist");
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

        log.error("RelationshipStatusValidator validate method. It's impossible to update relationship to the " +
                "status " + validationData.getStatus());

        throw new BadRequestException("It's impossible to update relationship to the status "
                + validationData.getStatus());
    }
}
