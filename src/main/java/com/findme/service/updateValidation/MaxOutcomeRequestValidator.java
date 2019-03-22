package com.findme.service.updateValidation;

import java.util.prefs.BackingStoreException;

public class MaxOutcomeRequestValidator extends Criteria {

    @Override
    public void validate() throws Exception {
        int outcomeRequestsCount = getRelationshipDAO().getOutcomeRequestsCount(getValidationData().getUserIdFrom());

        if (outcomeRequestsCount == 10) {
            throw new BackingStoreException("You can send only 10 friend requests");
        }
    }
}
