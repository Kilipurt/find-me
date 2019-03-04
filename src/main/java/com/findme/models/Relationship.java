package com.findme.models;

import javax.persistence.*;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship {
    private String status;
    private RelationshipKey relationshipKey;

    @EmbeddedId
    public RelationshipKey getRelationshipKey() {
        return relationshipKey;
    }

    public void setRelationshipKey(RelationshipKey relationshipKey) {
        this.relationshipKey = relationshipKey;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
