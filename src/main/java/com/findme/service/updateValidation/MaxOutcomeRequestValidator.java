package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;

public class MaxOutcomeRequestValidator extends Criteria {

    @Override
    public void validate() throws InternalServerError, BadRequestException {
        int outcomeRequestsCount = getRelationshipDAO().getOutcomeRequestsCount(getValidationData().getUserIdFrom());

        if (outcomeRequestsCount == 10) {
            throw new BadRequestException("You can send only 10 friend requests");
        }
    }
}
