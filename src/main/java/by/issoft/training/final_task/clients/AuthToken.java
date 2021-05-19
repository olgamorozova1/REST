package by.issoft.training.final_task.clients;

import by.issoft.training.authorization.Scope;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;
import static io.restassured.RestAssured.given;

public class AuthToken {
    Map<Scope, String> authTokens = new HashMap<>();
    private static AuthToken instance = null;

    private AuthToken() {
    }

    public static AuthToken getInstance() {
        if (instance == null) {
            instance = new AuthToken();
        }
        return instance;
    }

    public Map<Scope, String> getToken(Scope scope) {
        if (!authTokens.containsKey(scope)) {
            try {
                Response response = given()
                        .auth()
                        .preemptive()
                        .basic(readInfoFromProperties("user_name"), readInfoFromProperties("password"))
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("grant_type", readInfoFromProperties("grant_type"))
                        .formParam("scope", scope.getScopeStringValue())
                        .when()
                        .post(readInfoFromProperties("url") + "/oauth/token");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(response.getBody().asString());
                String token = node.get("access_token").asText();
                authTokens.put(scope, token);
            } catch (IOException ioException) {
                throw new RuntimeException("Authorization failed!");
            }
        }
        return authTokens;
    }
}
