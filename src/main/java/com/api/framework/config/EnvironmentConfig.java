package com.api.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {

    private static final Properties properties = new Properties();
    private static EnvironmentConfig instance;

    private EnvironmentConfig() {
        String env = System.getProperty("env", "dev");
        loadProperties(env + ".properties");
    }

    public static EnvironmentConfig getInstance() {
        if (instance == null) {
            instance = new EnvironmentConfig();
        }
        return instance;
    }

    private void loadProperties(String fileName) {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config/" + fileName)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getAdminUsername() {
        return properties.getProperty("admin.username");
    }

    public String getAdminPassword() {
        return properties.getProperty("admin.password");
    }
}
