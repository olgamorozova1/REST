package by.issoft.training.test;

import by.issoft.training.objects.User;
import by.issoft.training.requests.GetRequest;
import by.issoft.training.requests.PostRequest;
import by.issoft.training.utils.JsonConverter;
import by.issoft.training.utils.UserExistInArrayCheck;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Task30CreateUserTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();

    @Test
    public void createUser() {
        //We need to get all available ZIP codes to take first for using in POST request to add user
        String zipCodesResponse = getRequest.executeRequest("/zip-codes");
        String firstZipCode = StringUtils.substringBetween(zipCodesResponse, "\"", "\"");
        //Create user object
        User user = new User(20, "Anna", "FEMALE", firstZipCode);
        //converting object User to json to send it in request payload for POST to add user
        postRequest.setRequestBody(JsonConverter.convertObjectToJson(user));
        String postUsersResponse = postRequest.executeRequest("/users");
        //response contains String with code and body, fetching only response code to verify it
        String responseCode = StringUtils.substringAfter(postUsersResponse, "Response code: ");
        //We need to receive all users for /users endpoint to make sure new user was added
        String usersListResponse = getRequest.executeRequest("/users");
        //Getting only response body without response code
        String usersList = StringUtils.substringBefore(usersListResponse, "Response code:");
        //Making array of user as we received them as json
        User[] users = JsonConverter.convertJsonToObject(usersList);
        //Verifying whether user exists in array of users
        boolean isUserAdded = UserExistInArrayCheck.checkIfUserExistInArray(users, user);
        //Getting all zip codes which exist now after adding new user
        String zipCodesAfterAddingUser = getRequest.executeRequest("/zip-codes");
        //Comparing whether used zip code is presented in the list of all zip codes
        boolean isZipCodeDeleted = !zipCodesAfterAddingUser.contains(firstZipCode);
        Assertions.assertAll(
                () -> assertEquals("201", responseCode),
                () -> assertEquals(true, isUserAdded),
                () -> assertEquals(true, isZipCodeDeleted));
    }

    @Test
    public void createUserWithOnlyRequiredField() {
        //Create user object
        User user = new User("Test", "FEMALE");
        //converting object User to json to send it in request payload for POST to add user
        postRequest.setRequestBody(JsonConverter.convertObjectToJson(user));
        String postUsersResponse = postRequest.executeRequest("/users");
        //response contains String with code and body, fetching only response code to verify it
        String responseCode = StringUtils.substringAfter(postUsersResponse, "Response code: ");
        //We need to receive all users for /users endpoint to make sure new user was added
        String usersListResponse = getRequest.executeRequest("/users");
        //Getting only response body without response code
        String usersList = StringUtils.substringBefore(usersListResponse, "Response code:");
        //Making array of user as we received them as json
        User[] users = JsonConverter.convertJsonToObject(usersList);
        //Verifying whether user exists in array of users
        boolean isUserAdded = UserExistInArrayCheck.checkIfUserExistInArray(users, user);
        Assertions.assertAll(
                () -> assertEquals("201", responseCode),
                () -> assertEquals(true, isUserAdded));
    }

    @Test
    public void creteUserWithInvalidZipCode() {
        //Create user with invalid ZIP code
        User user = new User(24, "Kate", "FEMALE", "test");
        //converting object User to json to send it in request payload for POST to add user
        postRequest.setRequestBody(JsonConverter.convertObjectToJson(user));
        String postUsersResponse = postRequest.executeRequest("/users");
        //response contains String with code and body, fetching only response code to verify it
        String responseCode = StringUtils.substringAfter(postUsersResponse, "Response code: ");
        //We need to receive all users for /users endpoint to make sure new user was not added
        String usersListResponse = getRequest.executeRequest("/users");
        //Getting only response body without response code
        String usersList = StringUtils.substringBefore(usersListResponse, "Response code:");
        //Making array of user as we received them as json
        User[] users = JsonConverter.convertJsonToObject(usersList);
        //Verifying whether user exists in array of users
        boolean isUserAdded = UserExistInArrayCheck.checkIfUserExistInArray(users, user);
        Assertions.assertAll(
                () -> assertEquals("424", responseCode),
                () -> assertEquals(false, isUserAdded));
    }

    @Test
    public void createDuplicatedUser() {
        //We need to get all available ZIP codes to take first for using in POST request to add user
        String zipCodesResponse = getRequest.executeRequest("/zip-codes");
        String firstZipCode = StringUtils.substringBetween(zipCodesResponse, "\"", "\"");
        //Create user object
        User user = new User(20, "Alex", "MALE", firstZipCode);
        //converting object User to json to send it in request payload for POST to add user
        postRequest.setRequestBody(JsonConverter.convertObjectToJson(user));
        //add user 1st time
        postRequest.executeRequest("/users");
        //add user with same name and sex 2nd time
        //Create user object
        User duplicatedUser = new User(20, "Alex", "MALE");
        //converting object User to json to send it in request payload for POST to add user
        postRequest.setRequestBody(JsonConverter.convertObjectToJson(duplicatedUser));
        String postUsersResponse = postRequest.executeRequest("/users");
        //response contains String with code and body, fetching only response code to verify it
        String responseCode = StringUtils.substringAfter(postUsersResponse, "Response code: ");
        //We need to receive all users for /users endpoint to make sure duplicated user was not added
        String usersListResponse = getRequest.executeRequest("/users");
        //Getting only response body without response code
        String usersList = StringUtils.substringBefore(usersListResponse, "Response code:");
        //Making array of user as we received them as json
        User[] users = JsonConverter.convertJsonToObject(usersList);
        //Verifying how much time user exists in array of users
        boolean isUserAdded = UserExistInArrayCheck.checkIfUserExistInArray(users, user);
        Assertions.assertAll(
                () -> assertEquals("400", responseCode),
                () -> assertEquals(false, isUserAdded));
    }
}


