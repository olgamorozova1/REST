package by.issoft.training.test;

import by.issoft.training.clients.UserClient;
import by.issoft.training.clients.ZipCodesClient;

public class BaseTest {
    ZipCodesClient zipCodesClient = new ZipCodesClient();
    static UserClient userClient = new UserClient();
}
