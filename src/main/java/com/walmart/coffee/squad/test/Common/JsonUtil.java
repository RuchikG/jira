package com.walmart.coffee.squad.test.Common;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static ObjectMapper getMapper() {
        return objectMapper;
    }

    public static <T> T convertToObject(String jsonString, Class<T> classType) throws IOException {
        T obj = null;
        obj = objectMapper.readValue(jsonString, classType);
        return obj;
    }

    public static <T> T convertToObject(String jsonString, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(jsonString, typeReference);
    }

    public static String convertToString(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }
}