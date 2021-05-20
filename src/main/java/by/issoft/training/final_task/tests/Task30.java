package by.issoft.training.final_task.tests;

import by.issoft.training.objects.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.*;

public class Task30 extends BaseTestFinalTask {
    List<String> listOfZipCodes = new ArrayList<>();
    String zipCode;
    int createUserResponseCode;

    @Test
    public void addUser() {
        zipCode = generateZipCode();
        listOfZipCodes.add(zipCode);
        zipCodesClientFinalTask.addZipCode(listOfZipCodes);
        UserDto newUser = new UserDto(20, generateUserName(), "FEMALE", zipCode);
        createUserResponseCode = userClientFinalTask.addUser(newUser);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        List<String> listOfZipCodesAfterAddingUser = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponseCode),
                () -> assertTrue(users.contains(newUser)),
                () -> assertFalse(listOfZipCodesAfterAddingUser.contains(zipCode)));
    }

    @Test
    public void createUserWithOnlyRequiredField() {
        UserDto newUserWithOnlyRequiredFields = new UserDto(generateUserName(), "FEMALE");
        createUserResponseCode = userClientFinalTask.addUser(newUserWithOnlyRequiredFields);
        List<UserDto> listOfUsersAfterAddingNew = userClientFinalTask.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, createUserResponseCode),
                () -> assertTrue(listOfUsersAfterAddingNew.contains(newUserWithOnlyRequiredFields)));
    }

    @Test
    public void createUserWithInvalidZipCode() {
        UserDto userWithInvalidZipCode = new UserDto(24, generateUserName(), "FEMALE", generateZipCode());
        createUserResponseCode = userClientFinalTask.addUser(userWithInvalidZipCode);
        List<UserDto> listOfUsersAfterAddingNew = userClientFinalTask.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(424, createUserResponseCode),
                () -> assertFalse(listOfUsersAfterAddingNew.contains(userWithInvalidZipCode)));
    }

    @Test
    public void createDuplicatedUser() {
        String userName = generateUserName();
        UserDto user = new UserDto(userName, "MALE");
        userClientFinalTask.addUser(user);
        UserDto duplicatedUser = new UserDto(userName, "MALE");
        createUserResponseCode = userClientFinalTask.addUser(duplicatedUser);
        List<UserDto> listOfUsersAfterAddingDuplicatedUser = userClientFinalTask.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(400, createUserResponseCode),
                () -> assertEquals(1, Collections.frequency(listOfUsersAfterAddingDuplicatedUser, duplicatedUser)));
    }
}
