package com.api.framework.specifications;

import com.api.framework.core.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Factory for building reusable RestAssured request specifications.
 */
public class RequestSpecifications {

    private RequestSpecifications() {}

    /**
     * Base spec with JSON content-type and logging.
     */
    public static RequestSpecification baseSpec() {
        return given().spec(RestAssuredConfig.buildBaseSpec());
    }

    /**
     * Authenticated spec - attaches the token cookie for secured endpoints.
     */
    public static RequestSpecification authenticatedSpec(String token) {
        return baseSpec().header("Cookie", "token=" + token);
    }
}