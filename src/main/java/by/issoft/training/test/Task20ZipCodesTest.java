package by.issoft.training.test;

import by.issoft.training.objects.UserDto;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.*;;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Task20ZipCodesTest extends BaseTest {
    List<String> listOfZipCodes = new ArrayList<>();

    @Test
    @Order(1)
    void getAllZipCodesTest() {
        String listOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(200, zipCodesClient.getZipCodesResponse().getStatusLine().getStatusCode()),
                () -> assertEquals("[\"12345\",\"23456\",\"ABCDE\"]", listOfZipCodes));
    }

    @Test
    @Order(2)
    void postNewZipCodeTest() {
        listOfZipCodes.add("67890");
        CloseableHttpResponse addZipCodeResponse = zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        String listOfZipCodesAfterAddingNew = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfZipCodesAfterAddingNew.contains("67890")));
    }

    @Test
    @Order(3)
    void postDuplicatedZipCode() {
        String newZipCode = "246000";
        String duplicatedZipCode = "246012";
        listOfZipCodes.add(duplicatedZipCode);
        zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        listOfZipCodes.add(newZipCode);
        CloseableHttpResponse addZipCodeResponse = zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        String listOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertTrue(listOfZipCodes.contains(newZipCode)),
                () -> assertEquals(1, StringUtils.countMatches(listOfZipCodes, duplicatedZipCode)));
    }

    @Test
    @Order(4)
    void postAlreadyUsedZipCode() {
        String usedZipCode = "246001";
        UserDto userDto = new UserDto(20, "Alex", "MALE", usedZipCode);
        userClient.createUser(userDto);
        listOfZipCodes.add(usedZipCode);
        CloseableHttpResponse addUsedZipCodeResponse = zipCodesClient.expandAvailableZipCodes(listOfZipCodes);
        String listOfZipCodes = zipCodesClient.getZipCodes();
        Assertions.assertAll(
                () -> assertEquals(201, addUsedZipCodeResponse.getStatusLine().getStatusCode()),
                () -> assertEquals(0, StringUtils.countMatches(listOfZipCodes, usedZipCode)));
    }
}



