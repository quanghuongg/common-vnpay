package vn.vnpay.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {
    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T fromJson(String json, Class object) throws IOException {
        return (T) mapper.readValue(json, object);
    }

    public static Map toMap(Object object) throws IOException {
        return mapper.convertValue(object, Map.class);
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static Object toJsonObject(String content) throws IOException {
        return fromJson(content, Object.class);
    }
}
