package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Task60DeleteUserTest extends BaseTest {
    List<String> listOfZipCodes = new ArrayList<>();
    UserDto userToDelete;
    String zipCode = generateZipCode();
    List<UserDto> listOfUsersAfterDelete = new ArrayList<>();
    List<String> listOfZipCodesAfterUserDelete = new ArrayList<>();
    CloseableHttpResponse deleteResponse;
    UserDto user;

    @BeforeEach
    public void prepareData() {
        listOfZipCodes.add(zipCode);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        user = new UserDto(30, generateUserName(), "MALE", zipCode);
        userClient.createUser(user);
    }

    @Test
    public void deleteUser() {
        deleteResponse = userClient.deleteUser(user);
        listOfUsersAfterDelete = userClient.getUsers();
        listOfZipCodesAfterUserDelete = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(204, deleteResponse.getStatusLine().getStatusCode()),
                () -> assertFalse(listOfUsersAfterDelete.contains(user), "User exists after deleting"),
                () -> assertTrue(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "Zip code gets to the list of available zip codes when user is deleted"));
    }

    @Test
    public void deleteUserRequestWithOnlyRequiredFields() {
        userToDelete = new UserDto();
        userToDelete.setName(user.getName());
        userToDelete.setSex(user.getSex());
        deleteResponse = userClient.deleteUser(userToDelete);
        listOfUsersAfterDelete = userClient.getUsers();
        listOfZipCodesAfterUserDelete = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(204, deleteResponse.getStatusLine().getStatusCode()),
                () -> assertFalse(listOfUsersAfterDelete.contains(user), "User exists after deleting"),
                () -> assertTrue(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "Zip code gets to the list of available zip codes when user is deleted"));
    }

    @Test
    public void deleteUserRequestWithoutNameFields() {
        userToDelete = new UserDto();
        userToDelete.setAge(user.getAge());
        userToDelete.setSex(user.getSex());
        userToDelete.setZipCode(user.getZipCode());
        deleteResponse = userClient.deleteUser(userToDelete);
        listOfUsersAfterDelete = userClient.getUsers();
        listOfZipCodesAfterUserDelete = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(409, deleteResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterDelete.contains(user), "User with same age, sex and zip code is not deleted"),
                () -> assertFalse(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "If delete request fails zip code from this request returned to list of available zip codes"));
    }

    @Test
    public void deleteUserRequestWithoutSexFields() {
        userToDelete = new UserDto();
        userToDelete.setAge(user.getAge());
        userToDelete.setName(user.getName());
        userToDelete.setZipCode(user.getZipCode());
        deleteResponse = userClient.deleteUser(userToDelete);
        listOfUsersAfterDelete = userClient.getUsers();
        listOfZipCodesAfterUserDelete = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(409, deleteResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterDelete.contains(user), "User with same age, sex and zip code is not deleted"),
                () -> assertFalse(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "If delete request fails zip code from this request returned to list of available zip codes"));
    }
}

