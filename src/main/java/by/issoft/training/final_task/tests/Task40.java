package by.issoft.training.final_task.tests;

import by.issoft.training.objects.UserDto;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static org.junit.jupiter.api.Assertions.*;

public class Task40 extends BaseTestFinalTask {
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
        userClientFinalTask.addUser(user1);
        userClientFinalTask.addUser(user2);
        userClientFinalTask.addUser(user3);
    }

    @Test
    public void getUsers() {
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);
        getUsersResponseCodeAndBody = userClientFinalTask.getUsers();
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains all requested users"));
    }

    @Test
    public void getUsersOlderThan() {
        expectedUsers.add(user1);
        expectedUsers.add(user3);
        getUsersResponseCodeAndBody = userClientFinalTask.getUsers("olderThan", 20, null);
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user2), "List of users contains users without requested parameters"));
    }

    @Test
    public void getUsersYoungerThan() {
        expectedUsers.add(user2);
        getUsersResponseCodeAndBody = userClientFinalTask.getUsers("youngerThan", 20, null);
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers)),
                () -> assertFalse(actualUsers.contains(user1), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user3), "List of users contains users without requested parameters"));
    }

    @Test
    public void getUsersBySex() {
        expectedUsers.add(user2);
        expectedUsers.add(user3);
        getUsersResponseCodeAndBody = userClientFinalTask.getUsers("sex", null, "FEMALE");
        actualUsers = getUsersResponseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, getUsersResponseCodeAndBody.getLeft()),
                () -> assertTrue(actualUsers.containsAll(expectedUsers), "List of users contains only users with requested parameters"),
                () -> assertFalse(actualUsers.contains(user1), "List of users contains users without requested parameters"));
    }
}
