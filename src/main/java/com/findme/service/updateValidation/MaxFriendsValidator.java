package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;
import org.apache.log4j.Logger;

public class MaxFriendsValidator extends GeneralValidator {

    private Logger logger = Logger.getLogger(MaxFriendsValidator.class);

    @Override
    public void validate(ValidationData validationData) throws Exception {

        long userIdFrom = validationData.getRelationship().getUserFrom().getId();
        long userIdTo = validationData.getRelationship().getUserTo().getId();

        if (validationData.getUserFromFriendsCount() == 100) {
            logger.error("MaxFriendsValidator validate method. User " + userIdFrom + " can has only 100 friends");
            throw new BadRequestException("User " + userIdFrom + " can has only 100 friends");
        }

        if (validationData.getUserToFriendsCount() == 100) {
            logger.error("MaxFriendsValidator validate method. User " + userIdTo + " can has only 100 friends");
            throw new BadRequestException("User " + userIdTo + " can has only 100 friends");
        }

        validateNext(validationData);
    }
}
