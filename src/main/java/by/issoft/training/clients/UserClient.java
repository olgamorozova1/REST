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
        int responseCode = 0;
        post.setRequestBody(convertObjectToJson(userDto));
        try (CloseableHttpResponse response = post.executeRequest("/users")) {
            responseCode = response.getStatusLine().getStatusCode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return responseCode;
    }

    @Step("Get all users from the application")
    public Pair<Integer, List<UserDto>> getUsers() {
        return createResponseAndBodyPairFromGetUsersRequest(get.executeRequest("/users"));
    }

    @Step("Get users with specified parameter")
    public Pair<Integer, List<UserDto>> getUsers(String parameter, Integer age, String sex) {
        return createResponseAndBodyPairFromGetUsersRequest(get.executeRequest("/users", parameter, age, sex));
    }

    private Pair<Integer, List<UserDto>> createResponseAndBodyPairFromGetUsersRequest(CloseableHttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        HttpEntity responseBodyEntity = response.getEntity();
        List<UserDto> responseBody = Converter.convertHttpEntityToObject(responseBodyEntity, UserDto.class);
        try {
            EntityUtils.consume(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return new ImmutablePair<>(responseCode, responseBody);
    }

    @Step("Update user with PUT request")
    public int updateUserEntirely(UpdateUserDto updateUserDto) {
        int responseCode = 0;
        put.setRequestBody(convertObjectToJson(updateUserDto));
        try (CloseableHttpResponse response = put.executeRequest("/users")) {
            responseCode = response.getStatusLine().getStatusCode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return responseCode;
    }

    @Step("Update user with PATCH request")
    public int updateUserPartially(UpdateUserDto updateUserDto) {
        int responseCode = 0;
        patch.setRequestBody(convertObjectToJson(updateUserDto));
        try (CloseableHttpResponse response = patch.executeRequest("/users")) {
            responseCode = response.getStatusLine().getStatusCode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return responseCode;
    }

    @Step("Delete user")
    public int deleteUser(UserDto user, String contentType) {
        int responseCode = 0;
        delete = new Delete(Scope.WRITE, contentType);
        if (contentType.equals("application/xml")) {
            delete.setRequestBody(convertObjectToXml(user));
        } else {
            delete.setRequestBody(convertObjectToJson(user));
        }
        try (CloseableHttpResponse response = delete.executeRequest("/users")) {
            responseCode = response.getStatusLine().getStatusCode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return responseCode;
    }

    @Step("Upload users")
    public Pair<Integer, String> uploadUsers(List<?> listOfObjects) {
        int responseCode = 0;
        String responseBodyString = null;
        try (CloseableHttpResponse response = post.executeUploadRequest("/users/upload", listOfObjects)) {
            responseCode = response.getStatusLine().getStatusCode();
            HttpEntity responseBodyEntity = response.getEntity();
            responseBodyString = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return new ImmutablePair<>(responseCode, responseBodyString);
    }

}
