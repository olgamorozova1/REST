package by.issoft.training;

import by.issoft.training.Constants.Constants;
import by.issoft.training.Constants.Scope;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
In this class we generate token. Singleton pattern is implemented.
Lazy Initialization is used as we need to get token in time when we make a response and going to use it.
In Eager initialization token will be created at the time of class loading. As token has particular time of living and as result
there is possibility to use old token when we make a request.
In this implementation expiration date of token is not taken into account - will be fixed in the next tasks.
*/
public class TokenGenerator {
    Scope scope;
    //New instance of the class is created
    private static TokenGenerator instance = null;

    //Private constructor is created for class
    private TokenGenerator(Scope scope) {
        this.scope = scope;
    }

    //Method to create new token if it is not exist or scope of existing token is different
    public static TokenGenerator getInstance(Scope scope) {
        if (instance == null || (instance != null && instance.scope != scope)) {
            instance = new TokenGenerator(scope);
            instance.createAccessToken(scope);
        }
        return instance;
    }

    private String createAccessToken(Scope scope) {
        String token = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(Constants.BASE_URL + "/oauth/token");
            String auth = Constants.USER_NAME + ":" + Constants.PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("grant_type", Constants.GRANT_TYPE));
            nameValuePair.add(new BasicNameValuePair("scope", scope.getScopeStringValue()));
            request.setEntity(new UrlEncodedFormEntity(nameValuePair));
            ResponseHandler<String> responseHandler = new HandlerOfResponse();
            String httpResponse = httpclient.execute(request, responseHandler);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(httpResponse);
            token = node.get("access_token").asText();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(token);
        return token;
    }
}
