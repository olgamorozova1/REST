package by.issoft.training.test;

import by.issoft.training.requests.GetRequest;
import by.issoft.training.requests.PostRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZipCodesTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();

    @Test
    @Order(1)
    void getAllZipCodesTest() {
        String response = getRequest.executeRequest("/zip-codes");
        String responseCode = StringUtils.substringAfter(response, "Response code: ");
        String responseBody = StringUtils.substringBetween(response, "[", "]");
        Assertions.assertAll(
                () -> assertEquals("200", responseCode),
                () -> assertEquals("\"12345\",\"23456\",\"ABCDE\"", responseBody));
    }

    @Test
    @Order(2)
    void postNewZipCodeTest() {
        String existingZipCodes = StringUtils.substringBetween(getRequest.executeRequest("/zip-codes"), "[", "]");
        String newZipCode = "test";
        postRequest.setRequestBody("[\n" +
                "  \"" + newZipCode + "\"\n" +
                "]");
        String response = postRequest.executeRequest("/zip-codes/expand");
        String responseCode = StringUtils.substringAfter(response, "Response code: ");
        String responseBody = StringUtils.substringBetween(response, "[", "]");
        Assertions.assertAll(
                () -> assertEquals("201", responseCode),
                () -> assertEquals(existingZipCodes + "," + "\"" + newZipCode + "\"", responseBody));
    }

    @Test
    @Order(3)
    void postDuplicatedZipCode() {
        String existingZipCodes = StringUtils.substringBetween(getRequest.executeRequest("/zip-codes"), "[", "]");
        String newZipCode = "12345";
        postRequest.setRequestBody("[\n" +
                "  \"" + newZipCode + "\"\n" +
                "]");
        String response = postRequest.executeRequest("/zip-codes/expand");
        String responseCode = StringUtils.substringAfter(response, "Response code: ");
        String responseBody = StringUtils.substringBetween(response, "[", "]");
        Assertions.assertAll(
                () -> assertEquals("201", responseCode),
                () -> assertEquals(existingZipCodes, responseBody));
    }

    @Test
    @Order(4)
    void postAlreadyUsedZipCode() {
        String usedZipCode = "ABCDE";
        postRequest.setRequestBody("{\n" +
                "  \"age\": 0,\n" +
                "  \"name\": \"string\",\n" +
                "  \"sex\": \"FEMALE\",\n" +
                "  \"zipCode\": \"" + usedZipCode + "\"\n" +
                "}");
        postRequest.executeRequest("/users");
        String existingZipCodes = StringUtils.substringBetween(getRequest.executeRequest("/zip-codes"), "[", "]");
        PostRequest postRequest2 = new PostRequest();
        postRequest2.setRequestBody("[\n" +
                "  \"" + usedZipCode + "\"\n" +
                "]");
        String response2 = postRequest2.executeRequest("/zip-codes/expand");
        String responseCode = StringUtils.substringAfter(response2, "Response code: ");
        String responseBody = StringUtils.substringBetween(response2, "[", "]");
        Assertions.assertAll(
                () -> assertEquals("201", responseCode),
                () -> assertEquals(existingZipCodes, responseBody));
    }
}



