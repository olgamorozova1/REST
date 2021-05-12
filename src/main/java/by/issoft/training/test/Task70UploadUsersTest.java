package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.*;

public class Task70UploadUsersTest extends BaseTest {
    List<String> listOfZipCodes = new ArrayList<>();
    UserDto user1;
    UserDto user2;
    List<UserDto> listOfUsersToAdd = new ArrayList<>();
    List<UserDto> listOfUsersAfterUpload;
    List<UserDto> listOfUsersBeforeUpload;
    String zipCode1;
    String zipCode2;
    Pair<Integer, String> responseCodeAndBody;


    @BeforeEach
    public void prepareData() {
        zipCode1 = generateZipCode();
        zipCode2 = generateZipCode();
        listOfZipCodes.add(zipCode1);
        listOfZipCodes.add(zipCode2);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        user1 = new UserDto(30, generateUserName(), "MALE", zipCode1);
        listOfUsersToAdd.add(user1);
    }

    @Test
    @Description(value = "Test checks whether users could be uploaded, response code and body on upload request")
    public void uploadUsers() {
        user2 = new UserDto(31, generateUserName(), "FEMALE", zipCode2);
        listOfUsersToAdd.add(user2);
        responseCodeAndBody = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, responseCodeAndBody.getLeft()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersToAdd), "All users were uploaded"),
                () -> assertEquals("Number of users = " + Integer.toString(listOfUsersToAdd.size()),
                        responseCodeAndBody.getRight()));
    }

    @Test
    @Description(value = "Test checks whether users with invalid zip code could be uploaded, response code on upload request")
    @Flaky
    public void uploadUsersWithInvalidZipCode() {
        listOfUsersBeforeUpload = userClient.getUsers().getRight();
        user2 = new UserDto(31, generateUserName(), "FEMALE", "Invalid ZIP code");
        listOfUsersToAdd.add(user2);
        responseCodeAndBody = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(424, responseCodeAndBody.getLeft()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload), "List of users has not changed after failed upload request"));
    }

    @Test
    @Description(value = "Test checks whether users without name field could be uploaded, response code on upload request")
    @Flaky
    public void uploadUsersWithoutName() {
        listOfUsersBeforeUpload = userClient.getUsers().getRight();
        user2 = new UserDto();
        user2.setAge(34);
        user2.setSex("FEMALE");
        user2.setZipCode(zipCode2);
        listOfUsersToAdd.add(user2);
        responseCodeAndBody = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, responseCodeAndBody.getLeft()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload), "List of users has not changed after failed upload request"));
    }

    @Test
    @Description(value = "Test checks whether users without sex field could be uploaded, response code on upload request")
    @Flaky
    public void uploadUsersWithoutSex() {
        listOfUsersBeforeUpload = userClient.getUsers().getRight();
        user2 = new UserDto();
        user2.setAge(34);
        user2.setName(generateUserName());
        user2.setZipCode(zipCode2);
        listOfUsersToAdd.add(user2);
        responseCodeAndBody = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, responseCodeAndBody.getLeft()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload), "List of users has not changed after failed upload request"));
    }

    @Test
    @Description(value = "Test checks whether request without file with users could be sent, response code on upload request")
    public void sendUploadRequestWithoutFile() {
        listOfUsersBeforeUpload = userClient.getUsers().getRight();
        responseCodeAndBody = userClient.uploadUsers(null);
        listOfUsersAfterUpload = userClient.getUsers().getRight();
        Assertions.assertAll(
                () -> assertEquals(400, responseCodeAndBody.getLeft()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload), "List of users has not changed after failed upload request"));
    }
}

