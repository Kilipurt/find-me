package com.findme.service.updateRelationshipValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.RelationshipStatus;
import com.findme.models.ValidationData;
import lombok.extern.log4j.Log4j;

import java.util.Date;

@Log4j
public class FriendshipTimeValidator extends GeneralValidator {

    @Override
    public void validate(ValidationData validationData) throws Exception {

        if (!RelationshipStatus.PAST_FRIENDS.toString().equals(validationData.getStatus())) {
            validateNext(validationData);
        }

        long milliseconds = new Date().getTime() - validationData.getRelationship().getLastStatusChange().getTime();
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

        if (days < 3) {
            log.error("FriendshipTimeValidator validate method. Users must be friends at least 3 days in order to " +
                    "deleteSingleMessage this user from friends list");
            throw new BadRequestException("You must be friends at least 3 days " +
                    "in order to deleteSingleMessage this user from friends list");
        }

        validateNext(validationData);
    }
}
