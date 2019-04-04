package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;

public class MaxFriendsValidator extends GeneralValidator {

    @Override
    public void validate(ValidationData validationData) throws Exception {

        if (validationData.getUserFromFriendsCount() == 100) {
            throw new BadRequestException("User " + validationData.getUserIdFrom() + " can has only 100 friends");
        }

        if (validationData.getUserToFriendsCount() == 100) {
            throw new BadRequestException("User " + validationData.getUserIdTo() + " can has only 100 friends");
        }

        validateNext(validationData);
    }
}
