package by.issoft.training.requests;

import io.qameta.allure.httpclient.AllureHttpClientRequest;
import io.qameta.allure.httpclient.AllureHttpClientResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpConnection {
    private static CloseableHttpClient instance = null;

    private HttpConnection() {
    }

    public static CloseableHttpClient getInstance() {
        if (instance == null) {
            instance =
                    HttpClients
                            .custom()
                            .addInterceptorFirst(new AllureHttpClientResponse())
                            .addInterceptorLast(new AllureHttpClientRequest())
                            .build();
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
