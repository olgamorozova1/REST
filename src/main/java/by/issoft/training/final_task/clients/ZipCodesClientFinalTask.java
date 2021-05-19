package by.issoft.training.final_task.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.utils.Converter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ZipCodesClientFinalTask extends ClientBase {

    public int addZipCode(List<String> zipCodes) {
        return given()
                .spec(createSpecification(Scope.WRITE))
                .contentType(ContentType.JSON)
                .body(zipCodes)
                .when()
                .post("/zip-codes/expand")
                .then()
                .extract()
                .statusCode();
    }

    public Pair<Integer, List<String>> getZipCodes() {
        Response response = given()
                .spec(createSpecification(Scope.READ))
                .when()
                .get("/zip-codes");
        int responseCode = response.getStatusCode();
        List<String> responseBody = Converter.convertJsonToObject(response.getBody().asString(), String.class);
        return new ImmutablePair<>(responseCode, responseBody);
    }
}

