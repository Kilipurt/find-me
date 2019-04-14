package com.findme.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.models.Post;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class PostJsonUtil {
    private ObjectMapper mapper;

    private UserParserUtil userParserUtil;

    @Autowired
    public PostJsonUtil(UserParserUtil userParserUtil) {
        this.userParserUtil = userParserUtil;
    }

    public String getJsonFromList(List<Post> posts) throws IOException {
        Map<String, Object> allPostsMap = new HashMap<>();
        List<Map<String, Object>> postMapsInList = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("id", post.getId());
            postMap.put("message", post.getMessage());
            postMap.put("datePosted", post.getDatePosted());

//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("id", post.getUserPosted().getId());
//            userMap.put("firstName", post.getUserPosted().getFirstName());
//            userMap.put("lastName", post.getUserPosted().getLastName());
//
//            postMap.put("userPosted", userMap);

            postMap.put("userPosted", userParserUtil.createUserMap(post.getUserPosted()));
            postMap.put("location", post.getLocation());
            postMap.put("usersTagged", getUsersMapFromList(post.getUsersTagged()));
            postMapsInList.add(postMap);
        }

        allPostsMap.put("posts", postMapsInList);
        return createObjectMapper().writeValueAsString(allPostsMap);
    }

    private Map<String, Object> getUsersMapFromList(List<User> users) {
        Map<String, Object> usersMap = new HashMap<>();
//        List<Map<String, Object>> usersMapInList = new ArrayList<>();
//
//        for (User user : users) {
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("id", user.getId());
//            userMap.put("firstName", user.getFirstName());
//            userMap.put("lastName", user.getLastName());
//            usersMapInList.add(userMap);
//        }
//
//        usersMap.put("users", usersMapInList);
        usersMap.put("users", userParserUtil.usersListToMapUsersList(users));
        return usersMap;
    }

    private ObjectMapper createObjectMapper() {
        if (mapper == null)
            mapper = new ObjectMapper();

        return mapper;
    }
}
