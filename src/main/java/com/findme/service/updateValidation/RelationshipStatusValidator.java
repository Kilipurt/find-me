package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;

public class RelationshipStatusValidator extends GeneralValidator {

    public void validate(ValidationData validationData) throws Exception {
        if (validationData.getRelationship() == null) {
            throw new BadRequestException("Relationship between users "
                    + validationData.getUserIdFrom() + " and " + validationData.getUserIdTo() + " is not exist");
        }

        if (validationData.getRelationship().getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())
                && (validationData.getStatus().equals(RelationshipStatus.FRIENDS.toString())
                || validationData.getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString())
                || validationData.getStatus().equals(RelationshipStatus.DELETED.toString()))) {
            validateNext(validationData);
        }

        if (validationData.getRelationship().getStatus().equals(RelationshipStatus.FRIENDS.toString())
                && validationData.getStatus().equals(RelationshipStatus.PAST_FRIENDS.toString())) {
            validateNext(validationData);
        }

        if (validationData.getRelationship().getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString())
                && validationData.getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())) {
            validateNext(validationData);
        }

        if (validationData.getRelationship().getStatus().equals(RelationshipStatus.PAST_FRIENDS.toString())
                && validationData.getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())) {
            validateNext(validationData);
        }

        throw new BadRequestException("It's impossible to update relationship to the status "
                + validationData.getStatus());
    }
}
