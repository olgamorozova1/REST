package by.issoft.training.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.*;
import by.issoft.training.utils.JsonConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.List;

import static by.issoft.training.utils.JsonConverter.convertObjectToJson;
import static by.issoft.training.utils.JsonConverter.convertObjectToXml;

public class UserClient {
    Post post = new Post(Scope.WRITE);
    Get get = new Get(Scope.READ);
    Put put = new Put(Scope.WRITE);
    Patch patch = new Patch(Scope.WRITE);
    Delete delete;

    public CloseableHttpResponse createUser(UserDto userDto) {
        post.setRequestBody(convertObjectToJson(userDto));
        return post.executeRequest("/users");
    }

    public CloseableHttpResponse getUsersResponse() {
        return get.executeRequest("/users");
    }

    public List<UserDto> getUsers() {
        HttpEntity responseBodyEntity = getUsersResponse().getEntity();
        return JsonConverter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public List<UserDto> getUsers(ParametersForGetRequest parameters, String sex) {
        HttpEntity responseBodyEntity = getUsersResponse(parameters, sex).getEntity();
        return JsonConverter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public List<UserDto> getUsers(ParametersForGetRequest parameters, int age) {
        HttpEntity responseBodyEntity = getUsersResponse(parameters, age).getEntity();
        return JsonConverter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public CloseableHttpResponse getUsersResponse(ParametersForGetRequest parameters, String sex) {
        return get.getUsersWithSexParameter("/users", parameters, sex);
    }

    public CloseableHttpResponse getUsersResponse(ParametersForGetRequest parameters, Integer age) {
        return get.getUsersWithAgeParameter("/users", parameters, age);
    }

    public CloseableHttpResponse updateUserEntirely(UpdateUserDto updateUserDto) {
        put.setRequestBody(convertObjectToJson(updateUserDto));
        return put.executeRequest("/users");
    }

    public CloseableHttpResponse updateUserPartially(UpdateUserDto updateUserDto) {
        patch.setRequestBody(convertObjectToJson(updateUserDto));
        return patch.executeRequest("/users");
    }

    public CloseableHttpResponse deleteUser(UserDto user, String contentType) {
        delete = new Delete(Scope.WRITE, contentType);
        if (contentType.equals("application/xml")) {
            delete.setRequestBody(convertObjectToXml(user));
        } else {
            delete.setRequestBody(convertObjectToJson(user));
        }
        return delete.executeRequest("/users");
    }
}
