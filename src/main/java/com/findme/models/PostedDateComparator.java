package com.findme.models;

import java.util.Comparator;

public class PostedDateComparator implements Comparator<Post> {

    @Override
    public int compare(Post o1, Post o2) {
        return o1.getDatePosted().compareTo(o2.getDatePosted());
    }
}
