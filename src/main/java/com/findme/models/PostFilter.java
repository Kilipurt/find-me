package com.findme.models;

public class PostFilter {
    private long userPagePostedId;
    private long userPostedId;
    private long loggedInUserId;
    private PostsFiltrationType type;

    public PostFilter(long userPagePostedId, long userPostedId, long loggedInUserId, PostsFiltrationType type) {
        this.userPagePostedId = userPagePostedId;
        this.userPostedId = userPostedId;
        this.loggedInUserId = loggedInUserId;
        this.type = type;
    }

    public long getUserPagePostedId() {
        return userPagePostedId;
    }

    public long getUserPostedId() {
        return userPostedId;
    }

    public long getLoggedInUserId() {
        return loggedInUserId;
    }

    public PostsFiltrationType getType() {
        return type;
    }

    public void setUserPagePostedId(long userPagePostedId) {
        this.userPagePostedId = userPagePostedId;
    }

    public void setUserPostedId(long userPostedId) {
        this.userPostedId = userPostedId;
    }

    public void setLoggedInUserId(long loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public void setType(PostsFiltrationType type) {
        this.type = type;
    }
}
