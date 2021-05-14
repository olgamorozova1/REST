package by.issoft.training.clients;

import by.issoft.training.authorization.Scope;
import by.issoft.training.requests.Get;
import by.issoft.training.requests.Post;
import by.issoft.training.utils.Converter;
import io.qameta.allure.Step;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;


public class ZipCodesClient {
    Post post = new Post(Scope.WRITE);
    Get get = new Get(Scope.READ);

    @Step("Get all available ZIP codes")
    public Pair<Integer, List<String>> getZipCodes() {
        CloseableHttpResponse response = get.executeRequest("/zip-codes");
        HttpEntity responseBodyEntity = response.getEntity();
        String listOfZipCodes = null;
        try {
            listOfZipCodes = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        int responseCode = response.getStatusLine().getStatusCode();
        List<String> responseBody = Converter.convertJsonToObject(listOfZipCodes, String.class);
        return new ImmutablePair<>(responseCode, responseBody);
    }

    @Step("Add new ZIP code")
    public int expandAvailableZipCodes(List<String> zipCodes) {
        post.setRequestBody(Converter.convertObjectToJson(zipCodes));
        CloseableHttpResponse response = post.executeRequest("/zip-codes/expand");
        return response.getStatusLine().getStatusCode();
    }
}
