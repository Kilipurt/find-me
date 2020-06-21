package com.findme.service.updaterelationshipvalidation;

import com.findme.models.ValidationData;

public abstract class GeneralValidator {

    private GeneralValidator next;

    public abstract void validate(ValidationData validationData) throws Exception;

    public GeneralValidator linkWith(GeneralValidator next) {
        this.next = next;
        return next;
    }

    public void validateNext(ValidationData validationData) throws Exception {
        if (next != null) {
            next.validate(validationData);
        }
    }
}
