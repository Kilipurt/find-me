package com.findme.service.updateValidation;

import com.findme.dao.RelationshipDAO;
import com.findme.models.ValidationData;

public abstract class Criteria {

    private static RelationshipDAO relationshipDAO;

    private static ValidationData validationData;

    public static RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }

    public static ValidationData getValidationData() {
        return validationData;
    }

    public static void setRelationshipDAO(RelationshipDAO relationshipDAO) {
        Criteria.relationshipDAO = relationshipDAO;
    }

    public static void setValidationData(ValidationData validationData) {
        Criteria.validationData = validationData;
    }

    public abstract void validate() throws Exception;
}
