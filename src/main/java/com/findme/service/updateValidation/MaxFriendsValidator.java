package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;

public class MaxFriendsValidator extends GeneralValidator {

    @Override
    public void validate(ValidationData validationData) throws Exception {

        long userIdFrom = validationData.getRelationship().getUserFrom().getId();
        long userIdTo = validationData.getRelationship().getUserTo().getId();

        if (validationData.getUserFromFriendsCount() == 100) {
            throw new BadRequestException("User " + userIdFrom + " can has only 100 friends");
        }

        if (validationData.getUserToFriendsCount() == 100) {
            throw new BadRequestException("User " + userIdTo + " can has only 100 friends");
        }

        validateNext(validationData);
    }
}
