package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Task30CreateUserTest extends BaseTest{

    @Test
    public void createUser() {
        String listOfZipCodesBeforeAddingUser = zipCodesClient.getZipCodes();
        String zipCodeToCreateUser = zipCodesClient.retrieveAnyOfAvailableZipCodes(listOfZipCodesBeforeAddingUser);
        UserDto newUser = new UserDto(20, "Anna", "FEMALE", zipCodeToCreateUser);
        CloseableHttpResponse createUserResponse = userClient.createUser(newUser);
        UserDto[] users = userClient.getUsers();
        String listOfZipCodesAfterAddingUser = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, newUser)),
                () -> assertFalse(listOfZipCodesAfterAddingUser.contains(zipCodeToCreateUser)));
    }

    @Test
    public void createUserWithOnlyRequiredField() {
        UserDto newUserWithOnlyRequiredFields = new UserDto("Sofia", "FEMALE");
        CloseableHttpResponse createUserResponse = userClient.createUser(newUserWithOnlyRequiredFields);
        UserDto[] listOfUsersAfterAddingNew = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(listOfUsersAfterAddingNew, newUserWithOnlyRequiredFields)));
    }

    @Test
    public void creteUserWithInvalidZipCode() {
        UserDto userWithInvalidZipCode = new UserDto(24, "Kate", "FEMALE", "test");
        CloseableHttpResponse createUserWithInvalidZIPCodeResponse = userClient.createUser(userWithInvalidZipCode);
        UserDto[] listOfUsersAfterAddingNew = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(424, createUserWithInvalidZIPCodeResponse.getStatusLine().getStatusCode()),
                () -> assertFalse(userClient.checkIfUserExistInArray(listOfUsersAfterAddingNew, userWithInvalidZipCode)));
    }

    @Test
    public void createDuplicatedUser() {
        String listOfAvailableZipCodes = zipCodesClient.getZipCodes();
        String zipCodeToCreateUser = zipCodesClient.retrieveAnyOfAvailableZipCodes(listOfAvailableZipCodes);
        UserDto user = new UserDto(20, "Alex", "MALE", zipCodeToCreateUser);
        userClient.createUser(user);
        UserDto duplicatedUser = new UserDto("Alex", "MALE");
        CloseableHttpResponse createDuplicatedUserResponse = userClient.createUser(duplicatedUser);
        UserDto[] listOfUsersAfterAddingDuplicatedUser = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(400, createDuplicatedUserResponse.getStatusLine().getStatusCode()),
                () -> assertFalse(userClient.checkIfUserExistInArray(listOfUsersAfterAddingDuplicatedUser, duplicatedUser)));
    }
}


