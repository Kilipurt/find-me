package com.findme.util;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserParserUtil {

    private UserService userService;

    @Autowired
    public UserParserUtil(UserService userService) {
        this.userService = userService;
    }

    public List<Map<String, Object>> usersListToMapUsersList(List<User> users) {
        List<Map<String, Object>> usersMapInList = new ArrayList<>();

        for (User user : users) {
            usersMapInList.add(createUserMap(user));
        }

        return usersMapInList;
    }

    public Map<String, Object> createUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        return userMap;
    }

    public List<User> parseUsersFromString(String usersTagged) throws BadRequestException, InternalServerError {
        String[] usersTaggedStringArray = usersTagged.split(", ");
        List<User> usersTaggedList = new ArrayList<>();

        for (String userTaggedId : usersTaggedStringArray) {
            usersTaggedList.add(userService.findById(Long.parseLong(userTaggedId)));
        }

        return usersTaggedList;
    }
}
