package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;

import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class Get extends Request {
    HttpGet getRequest;

    public Get(Scope scope) {
        super(scope);
    }

    public CloseableHttpResponse executeRequest(String path) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            getRequest = new HttpGet(readInfoFromProperties("url") + path);
            setAuthHeader();
            response = httpclient.execute(getRequest);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }

    public CloseableHttpResponse executeRequest(String path, ParametersForGetRequest parameter, String value) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(readInfoFromProperties("url") + path);
            builder.setParameter(parameter.getParamStringValue(), value);
            getRequest = new HttpGet(builder.build());
            setAuthHeader();
            response = httpclient.execute(getRequest);
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }

    public CloseableHttpResponse executeRequest(String path, ParametersForGetRequest parameter, int value) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(readInfoFromProperties("url") + path);
            builder.setParameter(parameter.getParamStringValue(), Integer.toString(value));
            getRequest = new HttpGet(builder.build());
            setAuthHeader();
            response = httpclient.execute(getRequest);
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }

    public void setAuthHeader() {
        getToken();
        getRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }
}
