package io.uhha.im.server.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;

public final class JSONUtils {
    private static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();


    public static String toJSONString(Object data) {

        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public static <T> T fromJson(String str, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(str, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T fromJson(byte[] data, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(data, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }


    public static <T> List<T> parseList(String str, Class<T> clazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionLikeType(List.class,clazz);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
