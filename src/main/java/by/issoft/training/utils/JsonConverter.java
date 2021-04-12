package by.issoft.training.utils;

import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonConverter {
    static ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(UserDto userDto) {
        String json = null;
        try {
            json = mapper.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String convertObjectToJson(UpdateUserDto updateUserDto) {
        String json = null;
        try {
            json = mapper.writeValueAsString(updateUserDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static UserDto[] convertJsonToObject(String json) {
        UserDto[] userDtos = null;
        try {
            userDtos = mapper.readValue(json, UserDto[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userDtos;
    }
}
