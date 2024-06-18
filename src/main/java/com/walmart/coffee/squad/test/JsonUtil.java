package com.walmart.coffee.squad.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.springframework.util.Assert;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtil {
   
    private static ObjectMapper mapperWithRoot = new ObjectMapper();

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        mapperWithRoot.setDateFormat(sdf);
        mapperWithRoot.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapperWithRoot.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithRoot.registerModule(new JodaModule());
        mapperWithRoot.registerModule(new JavaTimeModule());
        mapperWithRoot.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapperWithRoot.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        mapperWithRoot.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperWithRoot.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapperWithRoot.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapperWithRoot.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    }
    
    private static ObjectMapper mapperWORoot = new ObjectMapper();

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        mapperWORoot.setDateFormat(sdf);
        mapperWORoot.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapperWORoot.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWORoot.registerModule(new JodaModule());
        mapperWORoot.registerModule(new JavaTimeModule());
        mapperWORoot.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapperWORoot.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        mapperWORoot.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperWORoot.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    
    /**
     * Returns default ObjectMapper.
     * Default to mapper with no root value
     * @return ObjectMapper
     */
    public static ObjectMapper getMapper() {
        return getMapperWithNoRootValue();
    }
    
    public static ObjectMapper getMapperWithNoRootValue() {
        return mapperWORoot;
    }
    
    public static ObjectMapper getMapperWithRootValue() {
        return mapperWithRoot;
    }
    
    private JsonUtil() {
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        Assert.notNull(obj, "Input object for json conversion is null");
        return getMapper().writeValueAsString(obj);
    }

    public static <T> T fromJson(String json, TypeReference<T> valueType) throws IOException {
        Assert.notNull(valueType, "Input type reference to be used for json conversion is null");
        return getMapper().readValue(json, valueType);
    }

    public static <T> T fromJson(String json, Class<T> valueType) throws IOException {
        Assert.notNull(valueType, "Input class type to be used for json conversion is null");
        return getMapper().readValue(json, valueType);
    }
    
    public static String toJsonWithRootValue(Object obj) throws JsonProcessingException {
        Assert.notNull(obj, "Input object for json conversion is null");
        return mapperWithRoot.writeValueAsString(obj);
    }

    public static <T> T fromJsonWithRootValue(String json, TypeReference<T> valueType) throws IOException {
        Assert.notNull(valueType, "Input type reference to be used for json conversion is null");
        return mapperWithRoot.readValue(json, valueType);
    }

    public static <T> T fromJsonWithRootValue(String json, Class<T> valueType) throws IOException {
        Assert.notNull(valueType, "Input class type to be used for json conversion is null");
        return mapperWithRoot.readValue(json, valueType);
    }
}

