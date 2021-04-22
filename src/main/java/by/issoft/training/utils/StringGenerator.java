package by.issoft.training.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringGenerator {
    public static String generateZipCode() {
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }

    public static String generateUserName() {
        return "User_" + RandomStringUtils.randomAlphanumeric(5).toLowerCase();
    }
}
