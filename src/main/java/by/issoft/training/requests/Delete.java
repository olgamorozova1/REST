package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;

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
        try {
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
