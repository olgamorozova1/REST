package by.issoft.training.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Converter {
    static ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(Object object) {
        return convertObjectToStringFormat(object, mapper);
    }

    public static String convertObjectToXml(Object object) {
        return convertObjectToStringFormat(object, new XmlMapper());
    }

    private static String convertObjectToStringFormat(Object object, ObjectMapper mapper) {
        String value = null;
        try {
            value = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
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

    public static <T> String convertListToJson(List<T> list) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String json = null;
        try {
            mapper.writeValue(byteArrayOutputStream, list);
            byte[] data = byteArrayOutputStream.toByteArray();
            json = new String(data, StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return json;
    }

    public static <T> void convertListToJsonFile(List<T> list, String pathname) {
        try {
            mapper.writeValue(new File(pathname), list);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
