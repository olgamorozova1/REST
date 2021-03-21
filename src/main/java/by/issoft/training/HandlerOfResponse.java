package by.issoft.training;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HandlerOfResponse implements ResponseHandler<String> {
    @Override
    public String handleResponse(final HttpResponse response) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity responseBodyEntity = response.getEntity();
            if (responseBodyEntity == null) {
                throw new ClientProtocolException("Response contains no content");
            } else {
                return EntityUtils.toString(responseBodyEntity);
            }
        } else {
            throw new HttpResponseException(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());
        }
    }
}
