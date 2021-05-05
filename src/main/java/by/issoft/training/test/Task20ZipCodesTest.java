package by.issoft.training.test;

import by.issoft.training.objects.UserDto;

import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
import org.apache.http.client.methods.CloseableHttpResponse;
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

    @Test
    @Description(value = "Test checks whether ZIP codes can be get with GET request")
    @Flaky
    void getAllZipCodesTest() {
        expectedListOfZipCodes.add(generateZipCode());
        expectedListOfZipCodes.add(generateZipCode());
        zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(200, zipCodesClient.getZipCodesResponse().getStatusLine().getStatusCode()),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    @Description(value = "Test checks whether new ZIP codes can be added")
    void postNewZipCodeTest() {
        expectedListOfZipCodes.add(generateZipCode());
        CloseableHttpResponse addZipCodeResponse = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponse.getStatusLine().getStatusCode()),
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
        CloseableHttpResponse addZipCodeResponse = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(actualListOfZipCodes.contains(newZipCode)),
                () -> assertEquals(1, Collections.frequency(actualListOfZipCodes, duplicatedZipCode)));
    }

    @Test
    @Description(value = "Test checks whether already used ZIP code can be added")
    @Flaky
    void postAlreadyUsedZipCode() {
        String usedZipCode = generateZipCode();
        UserDto userDto = new UserDto(20, generateUserName(), "MALE", usedZipCode);
        userClient.createUser(userDto);
        expectedListOfZipCodes.add(usedZipCode);
        CloseableHttpResponse addUsedZipCodeResponse = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addUsedZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertEquals(0, Collections.frequency(actualListOfZipCodes, usedZipCode)));
    }
}



