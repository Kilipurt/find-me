package com.findme.service.updateRelationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;
import lombok.extern.log4j.Log4j;

@Log4j
public class MaxOutcomeRequestValidator extends GeneralValidator {

    @Override
    public void validate(ValidationData validationData) throws Exception {

        if (validationData.getOutcomeRequestsCount() == 10) {
            log.error("MaxOutcomeRequestValidator validate method. User can send only 10 friend requests");
            throw new BadRequestException("You can send only 10 friend requests");
        }

        validateNext(validationData);
    }
}
