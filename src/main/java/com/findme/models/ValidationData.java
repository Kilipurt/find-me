package com.findme.models;

public class ValidationData {
    private Relationship relationship;
    private User loggedInUser;
    private String status;
    private int userFromFriendsCount;
    private int userToFriendsCount;
    private int outcomeRequestsCount;

    public Relationship getRelationship() {
        return relationship;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public String getStatus() {
        return status;
    }

    public int getUserFromFriendsCount() {
        return userFromFriendsCount;
    }

    public int getUserToFriendsCount() {
        return userToFriendsCount;
    }

    public int getOutcomeRequestsCount() {
        return outcomeRequestsCount;
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

    public void setUserFromFriendsCount(int userFromFriendsCount) {
        this.userFromFriendsCount = userFromFriendsCount;
    }

    public void setUserToFriendsCount(int userToFriendsCount) {
        this.userToFriendsCount = userToFriendsCount;
    }

    public void setOutcomeRequestsCount(int outcomeRequestsCount) {
        this.outcomeRequestsCount = outcomeRequestsCount;
    }
}
