package by.issoft.training.utils;

import by.issoft.training.objects.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



public class JsonConverter {
    static ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(User user) {
        String json = null;
        try {
            ObjectNode node = mapper.createObjectNode();
            node.put("age", user.getAge());
            node.put("name", user.getName());
            node.put("sex", user.getSex());
            node.put("zipCode", user.getZipCode());
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static User[] convertJsonToObject(String json) {
        User[] users = null;
        try {
            users = mapper.readValue(json, User[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return users;
    }
}
