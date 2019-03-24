package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static Configuration ourInstance = new Configuration();
    private Properties prop = new Properties();
    private String fileName = "config.app";

    private Configuration() {
        System.out.println("Configuration loaded.");
        loadConfiguration();
    }

    public static Configuration getInstance() {
        return ourInstance;
    }

    public String getProp(String propName){
        return prop.getProperty(propName);
    }

    private void loadConfiguration() {
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
