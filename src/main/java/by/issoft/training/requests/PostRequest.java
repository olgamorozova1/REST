package by.issoft.training.requests;

import by.issoft.training.authorization.HandlerOfResponse;
import by.issoft.training.authorization.Scope;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class PostRequest extends Request {
    private String requestBody = null;

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String executeRequest(String path) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(readInfoFromProperties("url") + path);
            getToken(Scope.WRITE);
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            StringEntity entity = new StringEntity(requestBody);
            postRequest.setEntity(entity);
            ResponseHandler<String> responseHandler = new HandlerOfResponse();
            response = httpclient.execute(postRequest, responseHandler);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }
}
