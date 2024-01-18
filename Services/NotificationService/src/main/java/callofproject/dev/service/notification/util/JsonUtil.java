package callofproject.dev.service.notification.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * Utility class for converting objects to JSON strings and vice versa.
 */
public class JsonUtil
{
    /**
     * Prevents instantiation of this utility class.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Prevents instantiation of this utility class.
     */
    private JsonUtil()
    {
    }


    /**
     * Converts an object to a JSON string.
     *
     * @param object the object to be converted
     * @return the JSON string
     * @throws IOException if the object cannot be converted to a JSON string
     */
    public static String convertObjectToJsonString(Object object) throws IOException
    {
        return objectMapper.writeValueAsString(object);
    }


    /**
     * Converts a JSON string to an object.
     *
     * @param json  the JSON string to be converted
     * @param clazz the class of the object to be converted
     * @param <T>   the type of the object to be converted
     * @return the object
     * @throws IOException if the JSON string cannot be converted to an object
     */
    public static <T> T deserializeJson(String json, Class<T> clazz) throws IOException
    {
        return objectMapper.readValue(json, clazz);
    }
}

