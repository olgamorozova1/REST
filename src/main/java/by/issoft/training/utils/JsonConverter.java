package by.issoft.training.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonConverter {
    static ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(Object user) {
        String json = null;
        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> List<T> convertJsonToObject(String json, Class<T> tClass) {
        List<T> listOfObjects = null;
        try {
            listOfObjects = mapper.readValue(json, mapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, tClass));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return listOfObjects;
    }

    public static <T> List<T> convertHttpEntityToObject(HttpEntity responseBodyEntity, Class<T> tClass) {
        List<T> listOfObjects = null;
        try {
            listOfObjects = convertJsonToObject(EntityUtils.toString(responseBodyEntity), tClass);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return listOfObjects;
    }

    public static String convertListOfStringsToJson(List<String> listOfStrings) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String json = null;
        try {
            mapper.writeValue(byteArrayOutputStream, listOfStrings);
            byte[] data = byteArrayOutputStream.toByteArray();
            json = new String(data, StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return json;
    }
}
