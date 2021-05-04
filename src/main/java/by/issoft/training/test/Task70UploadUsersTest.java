package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import org.apache.http.HttpResponse;
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
    HttpResponse uploadResponse;
    String zipCode1;
    String zipCode2;


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
    public void uploadUsers() {
        user2 = new UserDto(31, generateUserName(), "FEMALE", zipCode2);
        listOfUsersToAdd.add(user2);
        uploadResponse = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(201, uploadResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersToAdd)),
                () -> assertEquals("Number of users = " + Integer.toString(listOfUsersToAdd.size()),
                        userClient.getUploadUsersResponseBody(uploadResponse)));
    }

    @Test
    public void uploadUsersWithInvalidZipCode() {
        listOfUsersBeforeUpload = userClient.getUsers();
        user2 = new UserDto(31, generateUserName(), "FEMALE", "Invalid ZIP code");
        listOfUsersToAdd.add(user2);
        uploadResponse = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(424, uploadResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload)));
    }

    @Test
    public void uploadUsersWithoutName() {
        listOfUsersBeforeUpload = userClient.getUsers();
        user2 = new UserDto();
        user2.setAge(34);
        user2.setSex("FEMALE");
        user2.setZipCode(zipCode2);
        listOfUsersToAdd.add(user2);
        uploadResponse = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(409, uploadResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload)));
    }

    @Test
    public void uploadUsersWithoutSex() {
        listOfUsersBeforeUpload = userClient.getUsers();
        user2 = new UserDto();
        user2.setAge(34);
        user2.setName(generateUserName());
        user2.setZipCode(zipCode2);
        listOfUsersToAdd.add(user2);
        uploadResponse = userClient.uploadUsers(listOfUsersToAdd);
        listOfUsersAfterUpload = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(409, uploadResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload)));
    }

    @Test
    public void sendUploadRequestWithoutFile() {
        listOfUsersBeforeUpload = userClient.getUsers();
        uploadResponse = userClient.uploadUsers(null);
        listOfUsersAfterUpload = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(400, uploadResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfUsersAfterUpload.equals(listOfUsersBeforeUpload)));
    }
}

