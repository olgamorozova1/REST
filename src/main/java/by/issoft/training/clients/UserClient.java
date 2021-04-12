package by.issoft.training.clients;

import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.*;
import by.issoft.training.utils.JsonConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UserClient {
    Post post = new Post();
    Get get = new Get();
    Put put = new Put();
    Patch patch = new Patch();

    public CloseableHttpResponse createUser(UserDto userDto) {
        post.setRequestBody(JsonConverter.convertObjectToJson(userDto));
        return post.executeRequest("/users");
    }

    public UserDto[] getUsers() {
        CloseableHttpResponse response = get.executeRequest("/users");
        HttpEntity responseBodyEntity = response.getEntity();
        String listOfUsers = null;
        try {
            listOfUsers = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return JsonConverter.convertJsonToObject(listOfUsers);
    }

    public UserDto[] getUsers(GetParameters parameters, String value) {
        CloseableHttpResponse response = get.executeRequest("/users", parameters, value);
        HttpEntity responseBodyEntity = response.getEntity();
        String listOfUsers = null;
        try {
            listOfUsers = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return JsonConverter.convertJsonToObject(listOfUsers);
    }

    public CloseableHttpResponse getUsersResponse() {
        return get.executeRequest("/users");
    }

    public CloseableHttpResponse getUsersResponse(GetParameters parameters, String value) {
        return get.executeRequest("/users", parameters, value);
    }

    public CloseableHttpResponse updateUserEntirely(UpdateUserDto updateUserDto) {
        put.setRequestBody(JsonConverter.convertObjectToJson(updateUserDto));
        return put.executeRequest("/users");
    }

    public CloseableHttpResponse updateUserPartially(UpdateUserDto updateUserDto) {
        patch.setRequestBody(JsonConverter.convertObjectToJson(updateUserDto));
        return patch.executeRequest("/users");
    }

    public static boolean checkIfUserExistInArray(UserDto[] listOfUserDtos, UserDto userDto) {
        for (UserDto singleUserDto : listOfUserDtos) {
            if (singleUserDto.equals(userDto)) {
                return true;
            }
        }
        return false;
    }

}
