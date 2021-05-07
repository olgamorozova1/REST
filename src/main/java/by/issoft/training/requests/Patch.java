package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import io.qameta.allure.httpclient.AllureHttpClientResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class Patch extends Request {
    private String requestBody;

    public Patch(Scope scope) {
        super(scope);
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public CloseableHttpResponse executeRequest(String path) {
        try (CloseableHttpClient httpclient = HttpClients.custom().addInterceptorFirst(new AllureHttpClientResponse()).build()) {
            HttpPatch patchRequest = new HttpPatch(readInfoFromProperties("url") + path);
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
