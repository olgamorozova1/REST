package by.issoft.training.final_task.clients;

import by.issoft.training.authorization.Scope;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Map;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class ClientBase {
    Map<Scope, String> token;
    RequestSpecification requestSpecification;

    public ClientBase() {
        getAccessToken();
    }

    public void getAccessToken() {
        AuthToken authToken = AuthToken.getInstance();
        token = authToken.getToken(Scope.READ);
        token.putAll(authToken.getToken(Scope.WRITE));
    }

    public RequestSpecification createSpecification(Scope scope) {
        try {
            requestSpecification = new RequestSpecBuilder()
                    .setBaseUri(readInfoFromProperties("url"))
                    .addHeader("Authorization", "Bearer " + token.get(scope))
                    .build();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return requestSpecification;
    }
}

