package com.findme.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private ObjectWriter objectWriter;

    public String toJson(Object obj) throws JsonProcessingException {
        return createObjectWriter().writeValueAsString(obj);
    }

    private ObjectWriter createObjectWriter() {
        if (objectWriter == null) {
            objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        }

        return objectWriter;
    }
}
