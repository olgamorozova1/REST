package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import by.issoft.training.authorization.TokenGenerator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

public abstract class Request {
    CloseableHttpResponse response;
    String authHeader;
    Scope scope;
    CloseableHttpClient httpclient;

    public Request(Scope scope) {
        this.scope = scope;
        authHeader = getToken();
        httpclient = HttpConnection.getInstance();
    }

    public abstract CloseableHttpResponse executeRequest(String path);

    private String getToken() {
        TokenGenerator tokenGenerator = TokenGenerator.getInstance();
        Map<Scope, String> token = tokenGenerator.createAccessToken(Scope.READ);
        token.putAll(tokenGenerator.createAccessToken(Scope.WRITE));
        return "Bearer " + token.get(scope);
    }
}
