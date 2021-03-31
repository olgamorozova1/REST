package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import by.issoft.training.authorization.TokenGenerator;

import java.util.HashMap;

public abstract class Request {
    String response;
    String authHeader;

    public abstract String executeRequest(String path);

    public String getToken(Scope scope) {
        TokenGenerator tokenGenerator = TokenGenerator.getInstance();
        HashMap<Scope, String> token = tokenGenerator.createAccessToken(Scope.READ);
        token.putAll(tokenGenerator.createAccessToken(Scope.WRITE));
        authHeader = "Bearer " + token.get(scope);
        return authHeader;
    }
}
