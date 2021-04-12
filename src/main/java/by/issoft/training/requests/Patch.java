package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class Patch extends Request{
    private String requestBody;

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public CloseableHttpResponse executeRequest(String path) {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPatch patchRequest = new HttpPatch(readInfoFromProperties("url") + path);
            getToken(Scope.WRITE);
            patchRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            patchRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            StringEntity entity = new StringEntity(requestBody);
            patchRequest.setEntity(entity);
            response = httpclient.execute(patchRequest);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }
}
