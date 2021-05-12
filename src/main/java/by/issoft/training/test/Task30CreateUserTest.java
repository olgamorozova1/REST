package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.*;

public class Task30CreateUserTest extends BaseTest {
    List<String> listOfZipCodes = new ArrayList<>();
    String zipCode;

    @Test
    @Description(value = "Test checks whether user can be created with POST request")
    public void createUser() {
        zipCode = generateZipCode();
        listOfZipCodes.add(zipCode);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        UserDto newUser = new UserDto(20, generateUserName(), "FEMALE", zipCode);
        CloseableHttpResponse createUserResponse = userClient.createUser(newUser);
        List<UserDto> users = userClient.getUsers();
        List<String> listOfZipCodesAfterAddingUser = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(users.contains(newUser)),
                () -> assertFalse(listOfZipCodesAfterAddingUser.contains(zipCode)));
    }

    @Test
    @Description(value = "Test checks whether user with only required parameters specified can be created with POST request")
    public void createUserWithOnlyRequiredField() {
        UserDto newUserWithOnlyRequiredFields = new UserDto(generateUserName(), "FEMALE");
        CloseableHttpResponse createUserResponse = userClient.createUser(newUserWithOnlyRequiredFields);
        List<UserDto> listOfUsersAfterAddingNew = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterAddingNew.contains(newUserWithOnlyRequiredFields)));
    }

    @Test
    @Description(value = "Test checks whether user with invalid ZIP code can be created with POST request")
    public void creteUserWithInvalidZipCode() {
        UserDto userWithInvalidZipCode = new UserDto(24, generateUserName(), "FEMALE", generateZipCode());
        CloseableHttpResponse createUserWithInvalidZIPCodeResponse = userClient.createUser(userWithInvalidZipCode);
        List<UserDto> listOfUsersAfterAddingNew = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(424, createUserWithInvalidZIPCodeResponse.getStatusLine().getStatusCode()),
                () -> assertFalse(listOfUsersAfterAddingNew.contains(userWithInvalidZipCode)));
    }

    @Test
    @Description(value = "Test checks whether duplicated user can be created with POST request")
    @Flaky
    public void createDuplicatedUser() {
        String userName = generateUserName();
        zipCode = generateZipCode();
        listOfZipCodes.add(zipCode);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        UserDto user = new UserDto(20, userName, "MALE", zipCode);
        userClient.createUser(user);
        UserDto duplicatedUser = new UserDto(userName, "MALE");
        CloseableHttpResponse createDuplicatedUserResponse = userClient.createUser(duplicatedUser);
        List<UserDto> listOfUsersAfterAddingDuplicatedUser = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(400, createDuplicatedUserResponse.getStatusLine().getStatusCode()),
                () -> assertEquals(1, Collections.frequency(listOfUsersAfterAddingDuplicatedUser, duplicatedUser)));
    }
}


