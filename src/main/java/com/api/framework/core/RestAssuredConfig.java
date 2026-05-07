package com.api.framework.core;

import com.api.framework.config.EnvironmentConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Bootstraps RestAssured with base URI and shared request settings.
 * Call setup() once before the test suite runs.
 */
public class RestAssuredConfig {

    private RestAssuredConfig() {}

    public static void setup() {
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static RequestSpecification buildBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
}