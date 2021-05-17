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
        int responseCode = 0;
        List<String> responseBody = null;
        try (CloseableHttpResponse response = get.executeRequest("/zip-codes")) {
            HttpEntity responseBodyEntity = response.getEntity();
            String listOfZipCodes = EntityUtils.toString(responseBodyEntity);
            responseCode = response.getStatusLine().getStatusCode();
            responseBody = Converter.convertJsonToObject(listOfZipCodes, String.class);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return new ImmutablePair<>(responseCode, responseBody);
    }

    @Step("Add new ZIP code")
    public int expandAvailableZipCodes(List<String> zipCodes) {
        post.setRequestBody(Converter.convertObjectToJson(zipCodes));
        int responseCode = 0;
        try (CloseableHttpResponse response = post.executeRequest("/zip-codes/expand")) {
            responseCode = response.getStatusLine().getStatusCode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return responseCode;
    }
}
