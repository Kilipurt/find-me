package com.findme.service.updateRelationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;
import org.apache.log4j.Logger;

public class MaxOutcomeRequestValidator extends GeneralValidator {

    private Logger logger = Logger.getLogger(MaxOutcomeRequestValidator.class);

    @Override
    public void validate(ValidationData validationData) throws Exception {

        if (validationData.getOutcomeRequestsCount() == 10) {
            logger.error("MaxOutcomeRequestValidator validate method. User can send only 10 friend requests");
            throw new BadRequestException("You can send only 10 friend requests");
        }

        validateNext(validationData);
    }
}
