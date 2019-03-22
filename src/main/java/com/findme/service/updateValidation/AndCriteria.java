package com.findme.service.updateValidation;

import java.util.List;

public class AndCriteria extends Criteria {
    private List<Criteria> criteriaList;

    public AndCriteria(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public void validate() throws Exception {
        for (Criteria criteria : criteriaList) {
            criteria.validate();
        }
    }
}
