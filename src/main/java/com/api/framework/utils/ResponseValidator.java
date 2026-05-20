package com.api.framework.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for common response validations.
 */
public class ResponseValidator {

    private static final Logger log = LoggerFactory.getLogger(ResponseValidator.class);

    private ResponseValidator() {}

    public static void assertStatusCode(Response response, int expectedStatus) {
        log.info("Asserting status code: expected={}, actual={}",
                expectedStatus, response.statusCode());
        assertThat(response.statusCode())
                .withFailMessage("Expected HTTP status %d but got %d. Body: %s",
                        expectedStatus, response.statusCode(), response.body().asString())
                .isEqualTo(expectedStatus);
    }

    public static void assertBodyFieldNotNull(Response response, String fieldPath) {
        Object value = response.jsonPath().get(fieldPath);
        log.info("Asserting field '{}' is not null, value={}", fieldPath, value);
        assertThat(value)
                .withFailMessage("Field '%s' should not be null", fieldPath)
                .isNotNull();
    }

    public static void assertBodyContains(Response response, String fieldPath, Object expectedValue) {
        Object actual = response.jsonPath().get(fieldPath);
        log.info("Asserting field '{}' equals '{}'", fieldPath, expectedValue);
        assertThat(actual)
                .withFailMessage("Expected field '%s' to be '%s' but was '%s'",
                        fieldPath, expectedValue, actual)
                .isEqualTo(expectedValue);
    }

    public static void assertJsonSchema(Response response, String schemaPath) {
        log.info("Validating response against schema: {}", schemaPath);
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }

    public static void assertResponseTime(Response response, long maxMilliseconds) {
        long responseTime = response.time();
        log.info("Asserting response time: actual={}ms, max={}ms",
                responseTime, maxMilliseconds);
        assertThat(responseTime)
                .withFailMessage("Response time %dms exceeded maximum %dms",
                        responseTime, maxMilliseconds)
                .isLessThanOrEqualTo(maxMilliseconds);
    }
}