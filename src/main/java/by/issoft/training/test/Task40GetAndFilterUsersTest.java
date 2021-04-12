package by.issoft.training.test;

import by.issoft.training.objects.UserDto;
import by.issoft.training.requests.GetParameters;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task40GetAndFilterUsersTest extends BaseTest {
    static UserDto user1;
    static UserDto user2;
    static UserDto user3;
    UserDto[] expectedUsers;
    UserDto[] actualUsers;

    @BeforeAll
    public static void prepareData() {
        user1 = new UserDto(20, "Alex", "MALE");
        user2 = new UserDto(19, "Anna", "FEMALE");
        user3 = new UserDto(21, "Kate", "FEMALE");
        userClient.createUser(user1);
        userClient.createUser(user2);
        userClient.createUser(user3);
    }

    @Test
    @Order(1)
    public void getUsers() {
        expectedUsers = new UserDto[]{user1, user2, user3};
        actualUsers = userClient.getUsers();
        Assertions.assertAll(
                () -> assertEquals(200, userClient.getUsersResponse().getStatusLine().getStatusCode()),
                () -> assertTrue(Arrays.equals(expectedUsers, actualUsers)));
    }

    @Test
    @Order(2)
    public void getUsersOlderThan() {
        expectedUsers = new UserDto[]{user3};
        actualUsers = userClient.getUsers(GetParameters.OlDER_THAN, "20");
        Assertions.assertAll(
                () -> assertEquals(200, userClient
                        .getUsersResponse(GetParameters.OlDER_THAN, "20")
                        .getStatusLine()
                        .getStatusCode()),
                () -> assertTrue(Arrays.equals(expectedUsers, actualUsers)));
    }

    @Test
    @Order(3)
    public void getUsersYoungerThan() {
        expectedUsers = new UserDto[]{user2};
        actualUsers = userClient.getUsers(GetParameters.YOUNGER_THAN, "20");
        Assertions.assertAll(
                () -> assertEquals(200, userClient
                        .getUsersResponse(GetParameters.YOUNGER_THAN, "20")
                        .getStatusLine()
                        .getStatusCode()),
                () -> assertTrue(Arrays.equals(expectedUsers, actualUsers)));
    }

    @Test
    @Order(4)
    public void getUsersBySex() {
        expectedUsers = new UserDto[]{user2, user3};
        actualUsers = userClient.getUsers(GetParameters.SEX, "FEMALE");
        Assertions.assertAll(
                () -> assertEquals(200, userClient
                        .getUsersResponse(GetParameters.SEX, "FEMALE")
                        .getStatusLine()
                        .getStatusCode()),
                () -> assertTrue(Arrays.equals(expectedUsers, actualUsers)));
    }
}

