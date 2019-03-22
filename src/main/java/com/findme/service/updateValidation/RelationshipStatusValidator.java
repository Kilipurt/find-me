package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;

public class RelationshipStatusValidator extends Criteria {

    public void validate() throws BadRequestException {
        if (getValidationData().getRelationship() == null) {
            throw new BadRequestException("Relationship between users "
                    + getValidationData().getUserIdFrom() + " and " + getValidationData().getUserIdTo() + " is not exist");
        }

        if (getValidationData().getRelationship().getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())
                && (getValidationData().getStatus().equals(RelationshipStatus.FRIENDS.toString())
                || getValidationData().getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString())
                || getValidationData().getStatus().equals(RelationshipStatus.DELETED.toString()))) {
            return;
        }

        if (getValidationData().getRelationship().getStatus().equals(RelationshipStatus.FRIENDS.toString())
                && getValidationData().getStatus().equals(RelationshipStatus.PAST_FRIENDS.toString())) {
            return;
        }

        if (getValidationData().getRelationship().getStatus().equals(RelationshipStatus.REQUEST_DECLINED.toString())
                && getValidationData().getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())) {
            return;
        }

        if (getValidationData().getRelationship().getStatus().equals(RelationshipStatus.PAST_FRIENDS.toString())
                && getValidationData().getStatus().equals(RelationshipStatus.REQUEST_SENT.toString())) {
            return;
        }

        throw new BadRequestException("It's impossible to update relationship to the status "
                + getValidationData().getStatus());
    }
}
