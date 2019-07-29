package com.findme.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationData {
    private Relationship relationship;
    private String status;
    private int userFromFriendsCount;
    private int userToFriendsCount;
    private int outcomeRequestsCount;
}