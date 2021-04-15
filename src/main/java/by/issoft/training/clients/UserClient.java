package by.issoft.training.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.*;
import by.issoft.training.utils.JsonConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.List;

public class UserClient {
    Post post = new Post(Scope.WRITE);
    Get get = new Get(Scope.READ);
    Put put = new Put(Scope.WRITE);
    Patch patch = new Patch(Scope.WRITE);

    public CloseableHttpResponse createUser(UserDto userDto) {
        post.setRequestBody(JsonConverter.convertObjectToJson(userDto));
        return post.executeRequest("/users");
    }

    public CloseableHttpResponse getUsersResponse() {
        return get.executeRequest("/users");
    }

    public List<UserDto> getUsers() {
        HttpEntity responseBodyEntity = getUsersResponse().getEntity();
        return JsonConverter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public List<UserDto> getUsers(ParametersForGetRequest parameters, String value) {
        HttpEntity responseBodyEntity = getUsersResponse(parameters, value).getEntity();
        return JsonConverter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public List<UserDto> getUsers(ParametersForGetRequest parameters, int value) {
        HttpEntity responseBodyEntity = getUsersResponse(parameters, value).getEntity();
        return JsonConverter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public CloseableHttpResponse getUsersResponse(ParametersForGetRequest parameters, String value) {
        return get.executeRequest("/users", parameters, value);
    }

    public CloseableHttpResponse getUsersResponse(ParametersForGetRequest parameters, int value) {
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
}