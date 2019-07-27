package com.findme.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFilter {
    private long userPagePostedId;
    private long userPostedId;
    private long loggedInUserId;
    private PostsFiltrationType type;
}