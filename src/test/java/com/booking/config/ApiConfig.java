package com.booking.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads the test configuration once and exposes the base Rest-Assured
 * request specification shared by every API client.
 */
public final class ApiConfig {

    private static final ApiConfig INSTANCE = new ApiConfig();

    private final Properties properties = new Properties();

    private ApiConfig() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    public static ApiConfig getInstance() {
        return INSTANCE;
    }

    public String baseUri() {
        return properties.getProperty("base.uri");
    }

    public String authUsername() {
        return properties.getProperty("auth.username");
    }

    public String authPassword() {
        return properties.getProperty("auth.password");
    }

    public RequestSpecification baseRequestSpec() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RestAssuredConfig config = RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory((type, charset) -> objectMapper));

        return new RequestSpecBuilder()
                .setBaseUri(baseUri())
                .setContentType("application/json")
                .setConfig(config)
                .build();
    }
}
