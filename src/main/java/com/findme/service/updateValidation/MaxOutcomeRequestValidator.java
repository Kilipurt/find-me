package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;

public class MaxOutcomeRequestValidator extends GeneralValidator {

    @Override
    public void validate(ValidationData validationData) throws Exception {

        if (validationData.getOutcomeRequestsCount() == 10) {
            throw new BadRequestException("You can send only 10 friend requests");
        }

        validateNext(validationData);
    }
}
