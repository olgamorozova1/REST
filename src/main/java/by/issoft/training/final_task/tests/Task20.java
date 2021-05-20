package by.issoft.training.final_task.tests;

import by.issoft.training.objects.UserDto;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Task20 extends BaseTestFinalTask {
    List<String> expectedListOfZipCodes = new ArrayList<>();
    List<String> actualListOfZipCodes = new ArrayList<>();
    Pair<Integer, List<String>> responseCodeAndBody;
    int addZipCodeResponseCode;

    @Test
    void getAllZipCodes() {
        expectedListOfZipCodes.add(generateZipCode());
        expectedListOfZipCodes.add(generateZipCode());
        zipCodesClientFinalTask.addZipCode(expectedListOfZipCodes);
        responseCodeAndBody = zipCodesClientFinalTask.getZipCodes();
        actualListOfZipCodes = responseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, responseCodeAndBody.getLeft()),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    void addNewZipCode() {
        expectedListOfZipCodes.add(generateZipCode());
        addZipCodeResponseCode = zipCodesClientFinalTask.addZipCode(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponseCode),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    void addDuplicatedZipCode() {
        String newZipCode = generateZipCode();
        String duplicatedZipCode = generateZipCode();
        expectedListOfZipCodes.add(duplicatedZipCode);
        zipCodesClientFinalTask.addZipCode(expectedListOfZipCodes);
        expectedListOfZipCodes.add(newZipCode);
        addZipCodeResponseCode = zipCodesClientFinalTask.addZipCode(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponseCode),
                () -> assertTrue(actualListOfZipCodes.contains(newZipCode)),
                () -> assertEquals(1, Collections.frequency(actualListOfZipCodes, duplicatedZipCode)));
    }

    @Test
    void addAlreadyUsedZipCode() {
        String usedZipCode = generateZipCode();
        actualListOfZipCodes.add(usedZipCode);
        zipCodesClientFinalTask.addZipCode(actualListOfZipCodes);
        UserDto userDto = new UserDto(20, generateUserName(), "MALE", usedZipCode);
        userClientFinalTask.addUser(userDto);
        expectedListOfZipCodes.add(usedZipCode);
        addZipCodeResponseCode = zipCodesClientFinalTask.addZipCode(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClientFinalTask.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponseCode),
                () -> assertEquals(0, Collections.frequency(actualListOfZipCodes, usedZipCode)));
    }
}
