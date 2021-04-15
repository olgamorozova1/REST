package by.issoft.training.test;

import by.issoft.training.objects.UserDto;

import by.issoft.training.utils.StringGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.*;;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task20ZipCodesTest extends BaseTest {
    List<String> actualListOfZipCodes = new ArrayList<>();
    List<String> expectedListOfZipCodes = new ArrayList<>();

    @Test
    void getAllZipCodesTest() {
        expectedListOfZipCodes.add(StringGenerator.generateZipCode());
        expectedListOfZipCodes.add(StringGenerator.generateZipCode());
        zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(200, zipCodesClient.getZipCodesResponse().getStatusLine().getStatusCode()),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    void postNewZipCodeTest() {
        expectedListOfZipCodes.add(StringGenerator.generateZipCode());
        CloseableHttpResponse addZipCodeResponse = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(actualListOfZipCodes.containsAll(expectedListOfZipCodes)));
    }

    @Test
    void postDuplicatedZipCode() {
        String newZipCode = StringGenerator.generateZipCode();
        String duplicatedZipCode = StringGenerator.generateZipCode();
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
    void postAlreadyUsedZipCode() {
        String usedZipCode = StringGenerator.generateZipCode();
        UserDto userDto = new UserDto(20, "Alex", "MALE", usedZipCode);
        userClient.createUser(userDto);
        expectedListOfZipCodes.add(usedZipCode);
        CloseableHttpResponse addUsedZipCodeResponse = zipCodesClient.expandAvailableZipCodes(expectedListOfZipCodes);
        actualListOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addUsedZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertEquals(0, Collections.frequency(actualListOfZipCodes, usedZipCode)));
    }
}



