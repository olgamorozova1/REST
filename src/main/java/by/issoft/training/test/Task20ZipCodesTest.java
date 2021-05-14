package by.issoft.training.test;

import by.issoft.training.objects.UserDto;

import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static by.issoft.training.utils.StringGenerator.generateUserName;
import static by.issoft.training.utils.StringGenerator.generateZipCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task20ZipCodesTest extends BaseTest {
    List<String> actualListOfZipCodes = new ArrayList<>();
    List<String> expectedListOfZipCodes = new ArrayList<>();
    Pair<Integer, List<String>> responseCodeAndBody;
    int addZipCodeResponseCode;

    @Test
    @Description(value = "Test checks whether ZIP codes can be get with GET request")
    @Flaky
    void getAllZipCodesTest() {
        expectedListOfZipCodes.add(generateZipCode());
        expectedListOfZipCodes.add(generateZipCode());
        zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        responseCodeAndBody = zipCodesClient.getZipCodes();
        actualListOfZipCodes = responseCodeAndBody.getRight();
        Assertions.assertAll(
                () -> assertEquals(200, responseCodeAndBody.getLeft()),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    @Description(value = "Test checks whether new ZIP codes can be added")
    void postNewZipCodeTest() {
        expectedListOfZipCodes.add(generateZipCode());
        addZipCodeResponseCode = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponseCode),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    @Description(value = "Test checks whether duplicated ZIP codes can be added")
    @Flaky
    void postDuplicatedZipCode() {
        String newZipCode = generateZipCode();
        String duplicatedZipCode = generateZipCode();
        expectedListOfZipCodes.add(duplicatedZipCode);
        zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        expectedListOfZipCodes.add(newZipCode);
        addZipCodeResponseCode = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponseCode),
                () -> assertTrue(actualListOfZipCodes.contains(newZipCode)),
                () -> assertEquals(1, Collections.frequency(actualListOfZipCodes, duplicatedZipCode)));
    }

    @Test
    @Description(value = "Test checks whether already used ZIP code can be added")
    @Flaky
    void postAlreadyUsedZipCode() {
        String usedZipCode = generateZipCode();
        actualListOfZipCodes.add(usedZipCode);
        zipCodesClient.expandAvailableZipCodes(actualListOfZipCodes);
        UserDto userDto = new UserDto(20, generateUserName(), "MALE", usedZipCode);
        userClient.createUser(userDto);
        expectedListOfZipCodes.add(usedZipCode);
        addZipCodeResponseCode = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes().getRight();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponseCode),
                () -> assertEquals(0, Collections.frequency(actualListOfZipCodes, usedZipCode)));
    }
}



