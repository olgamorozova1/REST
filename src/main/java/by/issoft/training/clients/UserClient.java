package by.issoft.training.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.*;
import by.issoft.training.utils.Converter;
import io.qameta.allure.Step;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import static by.issoft.training.utils.Converter.convertObjectToJson;
import static by.issoft.training.utils.Converter.convertObjectToXml;

public class UserClient {
    Post post = new Post(Scope.WRITE);
    Get get = new Get(Scope.READ);
    Put put = new Put(Scope.WRITE);
    Patch patch = new Patch(Scope.WRITE);
    Delete delete;

    @Step("Create user")
    public CloseableHttpResponse createUser(UserDto userDto) {
        post.setRequestBody(convertObjectToJson(userDto));
        return post.executeRequest("/users");
    }

    public CloseableHttpResponse getUsersResponse() {
        return get.executeRequest("/users");
    }

    @Step("Get all users from the application")
    public List<UserDto> getUsers() {
        HttpEntity responseBodyEntity = getUsersResponse().getEntity();
        return Converter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    @Step("Get users with specified sex parameter")
    public List<UserDto> getUsers(ParametersForGetRequest parameters, String sex) {
        HttpEntity responseBodyEntity = getUsersResponse(parameters, sex).getEntity();
        return Converter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    @Step("Get users with specified age parameter")
    public List<UserDto> getUsers(ParametersForGetRequest parameters, int age) {
        HttpEntity responseBodyEntity = getUsersResponse(parameters, age).getEntity();
        return Converter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
    }

    public CloseableHttpResponse getUsersResponse(ParametersForGetRequest parameters, String sex) {
        return get.getUsersWithSexParameter("/users", parameters, sex);
    }

    public CloseableHttpResponse getUsersResponse(ParametersForGetRequest parameters, Integer age) {
        return get.getUsersWithAgeParameter("/users", parameters, age);
    }

    @Step("Update user with PUT request")
    public CloseableHttpResponse updateUserEntirely(UpdateUserDto updateUserDto) {
        put.setRequestBody(convertObjectToJson(updateUserDto));
        return put.executeRequest("/users");
    }

    @Step("Update user with PATCH request")
    public CloseableHttpResponse updateUserPartially(UpdateUserDto updateUserDto) {
        patch.setRequestBody(convertObjectToJson(updateUserDto));
        return patch.executeRequest("/users");
    }

    @Step("Delete user")
    public CloseableHttpResponse deleteUser(UserDto user, String contentType) {
        delete = new Delete(Scope.WRITE, contentType);
        if (contentType.equals("application/xml")) {
            delete.setRequestBody(convertObjectToXml(user));
        } else {
            delete.setRequestBody(convertObjectToJson(user));
        }
        return delete.executeRequest("/users");
    }

    @Step("Upload users")
    public HttpResponse uploadUsers(List<?> listOfObjects) {
        return post.executeUploadRequest("/users/upload", listOfObjects);
    }

    public String getUploadUsersResponseBody(HttpResponse uploadResponse) {
        HttpEntity responseBodyEntity = uploadResponse.getEntity();
        String responseString = null;
        try {
            responseString = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return responseString;
    }

}
