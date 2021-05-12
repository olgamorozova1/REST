package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
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
    int createUserResponseCode;

    @Test
    @Description(value = "Test checks whether user can be created with POST request")
    public void createUser() {
        zipCode = generateZipCode();
        listOfZipCodes.add(zipCode);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        UserDto newUser = new UserDto(20, generateUserName(), "FEMALE", zipCode);
        createUserResponseCode = userClient.createUser(newUser);
        List<UserDto> users = userClient.getUsers().getRight();
        List<String> listOfZipCodesAfterAddingUser = zipCodesClient.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponseCode),
                () -> assertTrue(users.contains(newUser)),
                () -> assertFalse(listOfZipCodesAfterAddingUser.contains(zipCode)));
    }

    @Test
    @Description(value = "Test checks whether user with only required parameters specified can be created with POST request")
    public void createUserWithOnlyRequiredField() {
        UserDto newUserWithOnlyRequiredFields = new UserDto(generateUserName(), "FEMALE");
        createUserResponseCode = userClient.createUser(newUserWithOnlyRequiredFields);
        List<UserDto> listOfUsersAfterAddingNew = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponseCode),
                () -> assertTrue(listOfUsersAfterAddingNew.contains(newUserWithOnlyRequiredFields)));
    }

    @Test
    @Description(value = "Test checks whether user with invalid ZIP code can be created with POST request")
    public void createUserWithInvalidZipCode() {
        UserDto userWithInvalidZipCode = new UserDto(24, generateUserName(), "FEMALE", generateZipCode());
        createUserResponseCode = userClient.createUser(userWithInvalidZipCode);
        List<UserDto> listOfUsersAfterAddingNew = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(424, createUserResponseCode),
                () -> assertFalse(listOfUsersAfterAddingNew.contains(userWithInvalidZipCode)));
    }

    @Test
    @Description(value = "Test checks whether duplicated user can be created with POST request")
    @Flaky
    public void createDuplicatedUser() {
        String userName = generateUserName();
        UserDto user = new UserDto(userName, "MALE");
        userClient.createUser(user);
        UserDto duplicatedUser = new UserDto(userName, "MALE");
        createUserResponseCode = userClient.createUser(duplicatedUser);
        List<UserDto> listOfUsersAfterAddingDuplicatedUser = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(400, createUserResponseCode),
                () -> assertEquals(1, Collections.frequency(listOfUsersAfterAddingDuplicatedUser, duplicatedUser)));
    }
}


