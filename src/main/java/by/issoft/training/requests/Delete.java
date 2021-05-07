package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import io.qameta.allure.httpclient.AllureHttpClientRequest;
import io.qameta.allure.httpclient.AllureHttpClientResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class Delete extends Request {
    private String requestBody;
    private String contentType;

    public Delete(Scope scope, String contentType) {
        super(scope);
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public CloseableHttpResponse executeRequest(String path) {
        try (CloseableHttpClient httpclient = HttpClients.custom().addInterceptorFirst(new AllureHttpClientResponse()).build()) {
            HttpDeleteWithBody deleteRequest = new HttpDeleteWithBody(readInfoFromProperties("url") + path);
            deleteRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            deleteRequest.setHeader(HttpHeaders.CONTENT_TYPE, getContentType());
            StringEntity entity = new StringEntity(requestBody);
            deleteRequest.setEntity(entity);
            response = httpclient.execute(deleteRequest);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }
}
