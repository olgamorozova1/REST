package by.issoft.training.clients;

import by.issoft.training.requests.Get;
import by.issoft.training.requests.Post;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;


public class ZipCodesClient {
    Post post = new Post();
    Get get = new Get();

    public String getZipCodes() {
        CloseableHttpResponse response = get.executeRequest("/zip-codes");
        HttpEntity responseBodyEntity = response.getEntity();
        String listOfZipCodes = null;
        try {
            listOfZipCodes = EntityUtils.toString(responseBodyEntity);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return listOfZipCodes;
    }

    public CloseableHttpResponse getZipCodesResponse() {
        return get.executeRequest("/zip-codes");
    }

    public String retrieveAnyOfAvailableZipCodes(String zipCodes) {
        return StringUtils.substringBetween(zipCodes, "\"", "\"");
    }

    public CloseableHttpResponse expandAvailableZipCodes(List<String> zipCodes) {
        StringBuilder requestBody = new StringBuilder();
        for (int i = 0; i < zipCodes.size(); i++) {
            requestBody.append("\"" + zipCodes.get(i) + "\"");
            if (i != (zipCodes.size() - 1)) {
                requestBody.append(" , ");
            }
        }
        post.setRequestBody("[ " + requestBody + " ]");
        return post.executeRequest("/zip-codes/expand");
    }

}
