package by.issoft.training.authorization;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

/*
In this class we generate token. Singleton pattern is implemented.
Lazy Initialization is used as we need to get token in time when we make a response and going to use it.
In Eager initialization token will be created at the time of class loading. As token has particular time of living and as result
there is possibility to use old token when we make a request.
In this implementation expiration date of token is not taken into account - will be fixed in the next tasks.
*/
public class TokenGenerator {
    Scope scope;
    Map<Scope, String> tokensStorage = new HashMap<>();
    //New instance of the class is created
    private static TokenGenerator instance = null;

    //Private constructor is created for class
    private TokenGenerator() {
    }

    //Method to create new token if it does not exist
    public static TokenGenerator getInstance() {
        if (instance == null) {
            instance = new TokenGenerator();
        }
        return instance;
    }

    public Map<Scope, String> createAccessToken(Scope scope) {
        if (!tokensStorage.containsKey(scope)) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(readInfoFromProperties("url") + "/oauth/token");
                String auth = readInfoFromProperties("user_name") + ":" + readInfoFromProperties("password");
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
                String authHeader = "Basic " + new String(encodedAuth);
                request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
                request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
                List<NameValuePair> nameValuePair = new ArrayList<>();
                nameValuePair.add(new BasicNameValuePair("grant_type", readInfoFromProperties("grant_type")));
                nameValuePair.add(new BasicNameValuePair("scope", scope.getScopeStringValue()));
                request.setEntity(new UrlEncodedFormEntity(nameValuePair));
                ResponseHandler<String> responseHandler = new HandlerOfResponse();
                String httpResponse = httpclient.execute(request, responseHandler);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(httpResponse);
                String token = node.get("access_token").asText();
                tokensStorage.put(scope, token);
            } catch (IOException ex) {
                throw new RuntimeException("Authorization failed!");
            }
        }
        return tokensStorage;
    }
}
