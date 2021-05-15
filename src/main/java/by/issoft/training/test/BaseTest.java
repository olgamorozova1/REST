package by.issoft.training.test;

import by.issoft.training.clients.UserClient;
import by.issoft.training.clients.ZipCodesClient;
import by.issoft.training.requests.HttpConnection;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    ZipCodesClient zipCodesClient = new ZipCodesClient();
    static UserClient userClient = new UserClient();

    @AfterAll
    public static void closeConnection() {
        HttpConnection.closeConnection();
    }
}
