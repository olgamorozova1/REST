package by.issoft.training.test;


import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.test.BaseTest.userClient;
import static by.issoft.training.test.BaseTest.zipCodesClient;
import static org.junit.jupiter.api.Assertions.*;

public class Task50UpdateUserTest {
    UserDto userBeforeUpdate;
    UserDto userAfterUpdate;
    UpdateUserDto updateUserDto;
    List<String> listOfZipCodes = new ArrayList<>();
    String zipCode = "111111";

    @BeforeEach
    public void prepareData() {
        listOfZipCodes.add(zipCode);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        userBeforeUpdate = new UserDto(22, "Jon Snow", "MALE", zipCode);
        userClient.createUser(userBeforeUpdate);
    }

    @Test
    public void updateUserEntirely() {
        String listOfZipCodes = zipCodesClient.getZipCodes();
        String zipCodeToCreateUser = zipCodesClient.retrieveAnyOfAvailableZipCodes(listOfZipCodes);
        userAfterUpdate = new UserDto(17, "Arya Stark", "FEMALE", zipCodeToCreateUser);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserEntirely(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(200, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userAfterUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userBeforeUpdate)));
    }

    @Test
    public void updateUserEntirelyToUserWithInvalidZipCode() {
        userAfterUpdate = new UserDto(19, "Sansa Stark", "FEMALE", "IncorrectZipCode");
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserEntirely(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(424, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userBeforeUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userAfterUpdate)));
    }

    @Test
    public void updateUserEntirelyToUserWithoutNameField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setAge(30);
        userAfterUpdate.setSex("FEMALE");
        userAfterUpdate.setZipCode(zipCode);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserEntirely(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userBeforeUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userAfterUpdate)));
    }

    @Test
    public void updateUserEntirelyToUserWithoutSexField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setName("Brandon Stark");
        userAfterUpdate.setAge(15);
        userAfterUpdate.setZipCode(zipCode);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserEntirely(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userBeforeUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userAfterUpdate)));
    }

    @Test
    public void updateUserPartially() {
        userAfterUpdate = new UserDto(27, "Jon Snow", "MALE", zipCode);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserPartially(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(200, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userAfterUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userBeforeUpdate)));
    }
    @Test
    public void updateUserPartiallyToUserWithInvalidZipCode() {
        userAfterUpdate = new UserDto(19, "Jon Snow", "MALE", "IncorrectZipCode");
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserPartially(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(424, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userBeforeUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userAfterUpdate)));
    }
    @Test
    public void updateUserPartiallyToUserWithoutNameField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setAge(19);
        userAfterUpdate.setSex("MALE");
        userAfterUpdate.setZipCode(zipCode);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserPartially(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userBeforeUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userAfterUpdate)));
    }
    @Test
    public void updateUserPartiallyToUserWithoutSexField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setName("Jon Snow");
        userAfterUpdate.setAge(19);
        userAfterUpdate.setZipCode(zipCode);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        CloseableHttpResponse updateResponse = userClient.updateUserPartially(updateUserDto);
        UserDto[] users = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(userClient.checkIfUserExistInArray(users, userBeforeUpdate)),
                () -> assertFalse(userClient.checkIfUserExistInArray(users, userAfterUpdate)));
    }
}
