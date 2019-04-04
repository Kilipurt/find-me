package com.findme.service.updateValidation;

import com.findme.exception.BadRequestException;
import com.findme.models.ValidationData;

import java.util.Date;

public class FriendshipTimeValidator extends GeneralValidator {

    @Override
    public void validate(ValidationData validationData) throws Exception {

        long milliseconds = new Date().getTime() - validationData.getRelationship().getLastStatusChange().getTime();
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

        if (days < 3) {
            throw new BadRequestException("You must be friends at least 3 days " +
                    "in order to delete this user from friends list");
        }

        validateNext(validationData);
    }
}
