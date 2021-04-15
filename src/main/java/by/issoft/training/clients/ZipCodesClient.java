package by.issoft.training.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.requests.Get;
import by.issoft.training.requests.Post;
import by.issoft.training.utils.JsonConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;


public class ZipCodesClient {
    Post post = new Post(Scope.WRITE);
    Get get = new Get(Scope.READ);

    public List<String> getZipCodes() {
        CloseableHttpResponse response = get.executeRequest("/zip-codes");
        HttpEntity responseBodyEntity = response.getEntity();
        String listOfZipCodes = null;
        try {
            listOfZipCodes = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return JsonConverter.convertJsonToObject(listOfZipCodes, String.class);
    }

    public CloseableHttpResponse getZipCodesResponse() {
        return get.executeRequest("/zip-codes");
    }

    public CloseableHttpResponse expandAvailableZipCodes(List<String> zipCodes) {
        post.setRequestBody(JsonConverter.convertListOfStringsToJson(zipCodes));
        return post.executeRequest("/zip-codes/expand");
    }

}