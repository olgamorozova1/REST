package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.ParametersForGetRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
import org.apache.commons.lang3.tuple.Pair;
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
    Pair<Integer, List<UserDto>> getUsersResponseCodeAndBody;

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
    @Description(value = "Test checks whether users can be get via GET request")
    public void getUsers() {
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);
        getUsersResponseCodeAndBody = userClient.getUsers();
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains all requested users"));
    }

    @Test
    @Description(value = "Test checks whether users older then specified value can be get via GET request with parameter")
    @Flaky
    public void getUsersOlderThan() {
        expectedUsers.add(user1);
        expectedUsers.add(user3);
        getUsersResponseCodeAndBody = userClient.getUsers(ParametersForGetRequest.OLDER_THAN, 20);
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user2), "List of users contains users without requested parameters"));
    }

    @Test
    @Description(value = "Test checks whether users younger then specified value can be get via GET request with parameter")
    public void getUsersYoungerThan() {
        expectedUsers.add(user2);
        getUsersResponseCodeAndBody = userClient.getUsers(ParametersForGetRequest.YOUNGER_THAN, 20);
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers)),
                () -> assertFalse(actualUsers.contains(user1), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user3), "List of users contains users without requested parameters"));
    }

    @Test
    @Description(value = "Test checks whether users with specified sex can be get via GET request with parameter")
    public void getUsersBySex() {
        expectedUsers.add(user2);
        expectedUsers.add(user3);
        getUsersResponseCodeAndBody = userClient.getUsers(ParametersForGetRequest.SEX, "FEMALE");
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user1), "List of users contains users without requested parameters"));
    }
}

