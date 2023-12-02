package callofproject.dev.service.notification.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil
{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObjectToJsonString(Object object) throws IOException
    {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T deserializeJson(String json, Class<T> clazz) throws IOException
    {
        return objectMapper.readValue(json, clazz);
    }
}

