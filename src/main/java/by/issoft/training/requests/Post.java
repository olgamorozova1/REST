package by.issoft.training.requests;

import by.issoft.training.authorization.Scope;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static by.issoft.training.utils.Converter.convertListToJsonFile;
import static by.issoft.training.utils.PropertiesReader.readInfoFromProperties;

public class Post extends Request {
    private String requestBody;

    public Post(Scope scope) {
        super(scope);
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public CloseableHttpResponse executeRequest(String path) {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(readInfoFromProperties("url") + path);
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            StringEntity entity = new StringEntity(requestBody);
            postRequest.setEntity(entity);
            response = httpclient.execute(postRequest);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return response;
    }

    public HttpResponse executeUploadRequest(String path, List<?> listOfObjects) {
        HttpResponse uploadResponse = null;
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(readInfoFromProperties("url") + path);
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            String pathname = "./target/objects.json";
            convertListToJsonFile(listOfObjects, pathname);
            builder.addBinaryBody("file", new File(pathname));
            HttpEntity entity = builder.build();
            postRequest.setEntity(entity);
            uploadResponse = httpclient.execute(postRequest);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return uploadResponse;
    }
}
