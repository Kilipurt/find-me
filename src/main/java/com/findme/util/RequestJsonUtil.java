package com.findme.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.models.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestJsonUtil {
    private ObjectMapper mapper;

    public String getJsonFromList(List<User> users) throws IOException {
        Map<String, Object> incomeRequestsMap = new HashMap<>();
        List<Map<String, Object>> requests = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());
            requests.add(userMap);
        }

        incomeRequestsMap.put("users", requests);
        return createObjectMapper().writeValueAsString(incomeRequestsMap);
    }

    private ObjectMapper createObjectMapper() {
        if (mapper == null)
            mapper = new ObjectMapper();

        return mapper;
    }
}
