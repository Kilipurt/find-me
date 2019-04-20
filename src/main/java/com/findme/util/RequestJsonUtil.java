package com.findme.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestJsonUtil {
    private ObjectMapper mapper;

    private UserParserUtil userParserUtil;

    @Autowired
    public RequestJsonUtil(UserParserUtil userParserUtil) {
        this.userParserUtil = userParserUtil;
    }

    public String getJsonFromList(List<User> users) throws IOException {
        Map<String, Object> incomeRequestsMap = new HashMap<>();
        incomeRequestsMap.put("users", userParserUtil.usersListToMapUsersList(users));
        return createObjectMapper().writeValueAsString(incomeRequestsMap);
    }

    private ObjectMapper createObjectMapper() {
        if (mapper == null)
            mapper = new ObjectMapper();

        return mapper;
    }
}
