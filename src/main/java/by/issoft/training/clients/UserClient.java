package by.issoft.training.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.*;
import by.issoft.training.utils.Converter;
import io.qameta.allure.Step;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
    public int createUser(UserDto userDto) {
        post.setRequestBody(convertObjectToJson(userDto));
        return post.executeRequest("/users").getStatusLine().getStatusCode();
    }

    @Step("Get all users from the application")
    public Pair<Integer, List<UserDto>> getUsers() {
        return createResponseAndBodyPairFromGetUsersRequest(get.executeRequest("/users"));
    }

    @Step("Get users with specified sex parameter")
    public Pair<Integer, List<UserDto>> getUsers(ParametersForGetRequest parameters, String sex) {
        return createResponseAndBodyPairFromGetUsersRequest(get.getUsersWithSexParameter("/users", parameters, sex));
    }

    @Step("Get users with specified age parameter")
    public Pair<Integer, List<UserDto>> getUsers(ParametersForGetRequest parameters, int age) {
        return createResponseAndBodyPairFromGetUsersRequest(get.getUsersWithAgeParameter("/users", parameters, age));
    }

    private Pair<Integer, List<UserDto>> createResponseAndBodyPairFromGetUsersRequest(CloseableHttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        HttpEntity responseBodyEntity = response.getEntity();
        List<UserDto> responseBody = Converter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
        return new ImmutablePair<>(responseCode, responseBody);
    }

    @Step("Update user with PUT request")
    public int updateUserEntirely(UpdateUserDto updateUserDto) {
        put.setRequestBody(convertObjectToJson(updateUserDto));
        return put.executeRequest("/users").getStatusLine().getStatusCode();
    }

    @Step("Update user with PATCH request")
    public int updateUserPartially(UpdateUserDto updateUserDto) {
        patch.setRequestBody(convertObjectToJson(updateUserDto));
        return patch.executeRequest("/users").getStatusLine().getStatusCode();
    }

    @Step("Delete user")
    public int deleteUser(UserDto user, String contentType) {
        delete = new Delete(Scope.WRITE, contentType);
        if (contentType.equals("application/xml")) {
            delete.setRequestBody(convertObjectToXml(user));
        } else {
            delete.setRequestBody(convertObjectToJson(user));
        }
        return delete.executeRequest("/users").getStatusLine().getStatusCode();
    }

    @Step("Upload users")
    public Pair<Integer, String> uploadUsers(List<?> listOfObjects) {
        HttpResponse response = post.executeUploadRequest("/users/upload", listOfObjects);
        int responseCode = response.getStatusLine().getStatusCode();
        HttpEntity responseBodyEntity = response.getEntity();
        String responseBodyString = null;
        try {
            responseBodyString = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return new ImmutablePair<>(responseCode, responseBodyString);
    }

}
