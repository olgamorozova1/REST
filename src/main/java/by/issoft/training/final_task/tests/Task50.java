package by.issoft.training.final_task.tests;

import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.*;

public class Task50 extends BaseTestFinalTask {
    UserDto userBeforeUpdate;
    UserDto userAfterUpdate;
    UpdateUserDto updateUserDto;
    List<String> listOfZipCodes = new ArrayList<>();
    String zipCodeBeforeUserUpdate = generateZipCode();
    String zipCodeAfterUserUpdate = generateZipCode();
    List<String> listOfZipCodesAfterUserUpdate = new ArrayList<>();
    int updateResponseCode;

    @BeforeEach
    public void prepareData() {
        listOfZipCodes.add(zipCodeBeforeUserUpdate);
        listOfZipCodes.add(zipCodeAfterUserUpdate);
        zipCodesClientFinalTask.addZipCode(listOfZipCodes);
        userBeforeUpdate = new UserDto(22, generateUserName(), "MALE", zipCodeBeforeUserUpdate);
        userClientFinalTask.addUser(userBeforeUpdate);
    }

    @Test
    public void updateUserEntirely() {
        userAfterUpdate = new UserDto(17, generateUserName(), "FEMALE", zipCodeAfterUserUpdate);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserEntirely(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(200, updateResponseCode),
                () -> assertTrue(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(users.contains(userBeforeUpdate), "User which was updated does not exist in list of users"),
                () -> assertTrue(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code which was updated returned in the list of available ZIP codes"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeAfterUserUpdate),
                        "ZIP code used for update is in the list of available zip codes"));
    }

    @Test
    public void updateUserEntirelyToUserWithInvalidZipCode() {
        userAfterUpdate = new UserDto(19, generateUserName(), "FEMALE", generateZipCode());
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserEntirely(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(424, updateResponseCode),
                () -> assertTrue(users.contains(userBeforeUpdate), "User with parameters before update exists in the list of users"),
                () -> assertFalse(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code for user which is not updated returned to the list of available ZIP codes"));
    }

    @Test
    public void updateUserEntirelyToUserWithoutNameField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setAge(30);
        userAfterUpdate.setSex("FEMALE");
        userAfterUpdate.setZipCode(zipCodeAfterUserUpdate);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserEntirely(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponseCode),
                () -> assertTrue(users.contains(userBeforeUpdate), "User with parameters before update exists in the list of users"),
                () -> assertFalse(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code for user which is not updated returned to the list of available ZIP codes"),
                () -> assertTrue(listOfZipCodesAfterUserUpdate.contains(zipCodeAfterUserUpdate),
                        "When update fails ZIP code for update remains in the list of available ZIP codes"));
    }

    @Test
    public void updateUserEntirelyToUserWithoutSexField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setName(generateUserName());
        userAfterUpdate.setAge(15);
        userAfterUpdate.setZipCode(zipCodeAfterUserUpdate);
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserEntirely(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponseCode),
                () -> assertTrue(users.contains(userBeforeUpdate), "User with parameters before update exists in the list of users"),
                () -> assertFalse(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code for user which is not updated returned to the list of available ZIP codes"),
                () -> assertTrue(listOfZipCodesAfterUserUpdate.contains(zipCodeAfterUserUpdate),
                        "When update fails ZIP code for update remains in the list of available ZIP codes"));
    }

    @Test
    public void updateUserPartially() {
        userAfterUpdate = new UserDto(27, userBeforeUpdate.getName(), userBeforeUpdate.getSex(), userBeforeUpdate.getZipCode());
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserPartially(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(200, updateResponseCode),
                () -> assertTrue(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(users.contains(userBeforeUpdate), "User which was updated does not exist in list of users"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code returned to the list of available ZIP codes"));
    }

    @Test
    public void updateUserPartiallyToUserWithInvalidZipCode() {
        userAfterUpdate = new UserDto(userBeforeUpdate.getAge(), userBeforeUpdate.getName(), userBeforeUpdate.getSex(), generateZipCode());
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserPartially(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(424, updateResponseCode),
                () -> assertTrue(users.contains(userBeforeUpdate), "User with parameters before update exists in the list of users"),
                () -> assertFalse(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code returned to the list of available ZIP codes"));
    }

    @Test
    public void updateUserPartiallyToUserWithoutNameField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setAge(userBeforeUpdate.getAge());
        userAfterUpdate.setSex(userBeforeUpdate.getSex());
        userAfterUpdate.setZipCode(userBeforeUpdate.getZipCode());
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserPartially(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponseCode),
                () -> assertTrue(users.contains(userBeforeUpdate), "User with parameters before update exists in the list of users"),
                () -> assertFalse(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code returned to the list of available ZIP codes"));
    }

    @Test
    public void updateUserPartiallyToUserWithoutSexField() {
        userAfterUpdate = new UserDto();
        userAfterUpdate.setName(userBeforeUpdate.getName());
        userAfterUpdate.setAge(userBeforeUpdate.getAge());
        userAfterUpdate.setZipCode(userBeforeUpdate.getZipCode());
        updateUserDto = new UpdateUserDto(userAfterUpdate, userBeforeUpdate);
        updateResponseCode = userClientFinalTask.updateUserPartially(updateUserDto);
        List<UserDto> users = userClientFinalTask.getUsers().getRight();
        listOfZipCodesAfterUserUpdate = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(409, updateResponseCode),
                () -> assertTrue(users.contains(userBeforeUpdate), "User with parameters before update exists in the list of users"),
                () -> assertFalse(users.contains(userAfterUpdate), "User is updated"),
                () -> assertFalse(listOfZipCodesAfterUserUpdate.contains(zipCodeBeforeUserUpdate),
                        "ZIP code returned to the list of available ZIP codes"));
    }
}
