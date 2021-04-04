package by.issoft.training.requests;

import by.issoft.training.authorization.HandlerOfResponse;
import by.issoft.training.authorization.Scope;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class GetRequest extends Request{

    public String executeRequest(String path) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(readInfoFromProperties("url") + path);
            getToken(Scope.READ);
            getRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            ResponseHandler<String> responseHandler= new HandlerOfResponse();;
            response =  httpclient.execute(getRequest, responseHandler);
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }
}
