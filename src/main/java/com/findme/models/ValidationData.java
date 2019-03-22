package com.findme.models;

public class ValidationData {
    private long userIdFrom;
    private long userIdTo;
    private Relationship relationship;
    private User loggedInUser;
    private String status;

    public long getUserIdFrom() {
        return userIdFrom;
    }

    public long getUserIdTo() {
        return userIdTo;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public String getStatus() {
        return status;
    }

    public void setUserIdFrom(long userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public void setUserIdTo(long userIdTo) {
        this.userIdTo = userIdTo;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ValidationData(long userIdFrom, long userIdTo, Relationship relationship, User loggedInUser, String status) {
        this.userIdFrom = userIdFrom;
        this.userIdTo = userIdTo;
        this.relationship = relationship;
        this.loggedInUser = loggedInUser;
        this.status = status;
    }
}
