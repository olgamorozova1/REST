package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class Put extends Request {
    private String requestBody;

    public Put(Scope scope) {
        super(scope);
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public CloseableHttpResponse executeRequest(String path) {

        try {
            HttpPut putRequest = new HttpPut(readInfoFromProperties("url") + path);
            putRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            putRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            StringEntity entity = new StringEntity(requestBody);
            putRequest.setEntity(entity);
            response = httpclient.execute(putRequest);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }
}
