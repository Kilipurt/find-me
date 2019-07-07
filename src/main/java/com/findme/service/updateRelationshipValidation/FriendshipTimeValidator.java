package com.findme.service.updateRelationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;
import org.apache.log4j.Logger;

import java.util.Date;

public class FriendshipTimeValidator extends GeneralValidator {

    private Logger logger = Logger.getLogger(FriendshipTimeValidator.class);

    @Override
    public void validate(ValidationData validationData) throws Exception {

        if (!RelationshipStatus.PAST_FRIENDS.toString().equals(validationData.getStatus())) {
            validateNext(validationData);
        }

        long milliseconds = new Date().getTime() - validationData.getRelationship().getLastStatusChange().getTime();
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

        if (days < 3) {
            logger.error("FriendshipTimeValidator validate method. Users must be friends at least 3 days in order to " +
                    "updateDateDeleted this user from friends list");
            throw new BadRequestException("You must be friends at least 3 days " +
                    "in order to updateDateDeleted this user from friends list");
        }

        validateNext(validationData);
    }
}
