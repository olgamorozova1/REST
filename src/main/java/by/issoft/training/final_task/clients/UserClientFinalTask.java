package by.issoft.training.final_task.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.objects.UpdateUserDto;
import by.issoft.training.objects.UserDto;
import by.issoft.training.utils.Converter;;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

import static by.issoft.training.utils.Converter.convertListToJsonFile;
import static by.issoft.training.utils.Converter.convertObjectToXml;
import static io.restassured.RestAssured.given;

public class UserClientFinalTask extends ClientBase {
    public int addUser(UserDto userDto) {
        return given()
                .spec(createSpecification(Scope.WRITE))
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .post("/users")
                .then()
                .extract()
                .statusCode();
    }

    public Pair<Integer, List<UserDto>> getUsers() {
        Response response = given()
                .spec(createSpecification(Scope.READ))
                .when()
                .get("/users");
        int responseCode = response.getStatusCode();
        List<UserDto> responseBody = Converter.convertJsonToObject(response.getBody().asString(), UserDto.class);
        return new ImmutablePair<>(responseCode, responseBody);
    }

    public Pair<Integer, List<UserDto>> getUsers(String parameter, Integer age, String sex) {
        Response response = null;
        if (age != null) {
            response = given()
                    .spec(createSpecification(Scope.READ))
                    .param(parameter, age)
                    .when()
                    .get("/users");
        } else {
            response = given()
                    .spec(createSpecification(Scope.READ))
                    .param(parameter, sex)
                    .when()
                    .get("/users");
        }
        int responseCode = response.getStatusCode();
        List<UserDto> responseBody = Converter.convertJsonToObject(response.getBody().asString(), UserDto.class);
        return new ImmutablePair<>(responseCode, responseBody);
    }

    public int updateUserEntirely(UpdateUserDto updateUserDto) {
        return given()
                .spec(createSpecification(Scope.WRITE))
                .contentType(ContentType.JSON)
                .body(updateUserDto)
                .when()
                .put("/users")
                .then()
                .extract()
                .statusCode();
    }

    public int updateUserPartially(UpdateUserDto updateUserDto) {
        return given()
                .spec(createSpecification(Scope.WRITE))
                .contentType(ContentType.JSON)
                .body(updateUserDto)
                .when()
                .patch("/users")
                .then()
                .extract()
                .statusCode();
    }

    public int deleteUser(UserDto user, String contentType) {
        Response response = null;
        if (contentType.equals("application/xml")) {
            response = given()
                    .spec(createSpecification(Scope.WRITE))
                    .contentType(contentType)
                    .body(convertObjectToXml(user))
                    .when()
                    .delete("/users");
        } else {
            response = given()
                    .spec(createSpecification(Scope.WRITE))
                    .contentType(contentType)
                    .body(user)
                    .when()
                    .delete("/users");
        }
        return response.getStatusCode();
    }

    public Pair<Integer, String> uploadUsers(List<?> listOfObjects) {
        String pathname = "./target/objects.json";
        convertListToJsonFile(listOfObjects, pathname);
        Response response = given()
                .spec(createSpecification(Scope.WRITE))
                .multiPart(new File(pathname))
                .when()
                .post("/users/upload");
        int responseCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        return new ImmutablePair<>(responseCode, responseBody);
    }
}
