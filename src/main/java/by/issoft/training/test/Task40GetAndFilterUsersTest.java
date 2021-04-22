package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.ParametersForGetRequest;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static org.junit.jupiter.api.Assertions.*;

public class Task40GetAndFilterUsersTest extends BaseTest {
    static UserDto user1;
    static UserDto user2;
    static UserDto user3;
    List<UserDto> expectedUsers = new ArrayList<>();
    List<UserDto> actualUsers = new ArrayList<>();

    @BeforeAll
    public static void prepareData() {
        user1 = new UserDto(20, generateUserName(), "MALE");
        user2 = new UserDto(19, generateUserName(), "FEMALE");
        user3 = new UserDto(21, generateUserName(), "FEMALE");
        userClient.createUser(user1);
        userClient.createUser(user2);
        userClient.createUser(user3);
    }

    @Test
    public void getUsers() {
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);
        actualUsers = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(200, userClient.getUsersResponse().getStatusLine().getStatusCode()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains all requested users"));
    }

    @Test
    public void getUsersOlderThan() {
        expectedUsers.add(user1);
        expectedUsers.add(user3);
        actualUsers = userClient.getUsers(ParametersForGetRequest.OLDER_THAN, 20);
        Assertions.assertAll(
                () -> assertEquals(200, userClient
                        .getUsersResponse(ParametersForGetRequest.OLDER_THAN, 20)
                        .getStatusLine()
                        .getStatusCode()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user2), "List of users contains users without requested parameters"));
    }

    @Test
    public void getUsersYoungerThan() {
        expectedUsers.add(user2);
        actualUsers = userClient.getUsers(ParametersForGetRequest.YOUNGER_THAN, 20);
        Assertions.assertAll(
                () -> assertEquals(200, userClient
                        .getUsersResponse(ParametersForGetRequest.YOUNGER_THAN, 20)
                        .getStatusLine()
                        .getStatusCode()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers)),
                () -> assertFalse(actualUsers.contains(user1), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user3), "List of users contains users without requested parameters"));
    }

    @Test
    public void getUsersBySex() {
        expectedUsers.add(user2);
        expectedUsers.add(user3);
        actualUsers = userClient.getUsers(ParametersForGetRequest.SEX, "FEMALE");
        Assertions.assertAll(
                () -> assertEquals(200, userClient
                        .getUsersResponse(ParametersForGetRequest.SEX, "FEMALE")
                        .getStatusLine()
                        .getStatusCode()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user1), "List of users contains users without requested parameters"));
    }
}

