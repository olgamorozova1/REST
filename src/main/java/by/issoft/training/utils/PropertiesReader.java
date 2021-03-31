package by.issoft.training.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    static Properties properties;

    public static Properties readInfoFromProperties() throws IOException {
        FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties");
        properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
