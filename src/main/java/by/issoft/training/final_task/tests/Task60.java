package by.issoft.training.final_task.tests;

import by.issoft.training.objects.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.*;

public class Task60 extends BaseTestFinalTask {
    List<String> listOfZipCodes = new ArrayList<>();
    UserDto userToDelete;
    String zipCode = generateZipCode();
    List<UserDto> listOfUsersAfterDelete = new ArrayList<>();
    List<String> listOfZipCodesAfterUserDelete = new ArrayList<>();
    UserDto user;
    int deleteResponseCode;

    @BeforeEach
    public void prepareData() {
        listOfZipCodes.add(zipCode);
        zipCodesClientFinalTask.addZipCode(listOfZipCodes);
        user = new UserDto(30, generateUserName(), "MALE", zipCode);
        userClientFinalTask.addUser(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"application/json", "application/xml"})
    public void deleteUser(String contentType) {
        deleteResponseCode = userClientFinalTask.deleteUser(user, contentType);
        listOfUsersAfterDelete = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserDelete = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(204, deleteResponseCode),
                () -> assertFalse(listOfUsersAfterDelete.contains(user), "User exists after deleting"),
                () -> assertTrue(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "Zip code gets to the list of available zip codes when user is deleted"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"application/json", "application/xml"})
    public void deleteUserRequestWithOnlyRequiredFields(String contentType) {
        userToDelete = new UserDto();
        userToDelete.setName(user.getName());
        userToDelete.setSex(user.getSex());
        deleteResponseCode = userClientFinalTask.deleteUser(userToDelete, contentType);
        listOfUsersAfterDelete = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserDelete = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(204, deleteResponseCode),
                () -> assertFalse(listOfUsersAfterDelete.contains(user), "User exists after deleting"),
                () -> assertTrue(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "Zip code gets to the list of available zip codes when user is deleted"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"application/json", "application/xml"})
    public void deleteUserRequestWithoutNameFields(String contentType) {
        userToDelete = new UserDto();
        userToDelete.setAge(user.getAge());
        userToDelete.setSex(user.getSex());
        userToDelete.setZipCode(user.getZipCode());
        deleteResponseCode = userClientFinalTask.deleteUser(userToDelete, contentType);
        listOfUsersAfterDelete = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserDelete = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, deleteResponseCode),
                () -> assertTrue(listOfUsersAfterDelete.contains(user), "User with same age, sex and zip code is not deleted"),
                () -> assertFalse(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "If delete request fails zip code from this request returned to list of available zip codes"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"application/json", "application/xml"})
    public void deleteUserRequestWithoutSexFields(String contentType) {
        userToDelete = new UserDto();
        userToDelete.setAge(user.getAge());
        userToDelete.setName(user.getName());
        userToDelete.setZipCode(user.getZipCode());
        deleteResponseCode = userClientFinalTask.deleteUser(userToDelete, contentType);
        listOfUsersAfterDelete = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserDelete = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, deleteResponseCode),
                () -> assertTrue(listOfUsersAfterDelete.contains(user), "User with same age, sex and zip code is not deleted"),
                () -> assertFalse(listOfZipCodesAfterUserDelete.contains(zipCode),
                        "If delete request fails zip code from this request returned to list of available zip codes"));
    }
}
