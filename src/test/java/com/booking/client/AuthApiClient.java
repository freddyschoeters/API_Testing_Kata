package com.booking.client;

import com.booking.config.ApiConfig;
import com.booking.model.dto.AuthRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Service object wrapping the /auth endpoint.
 */
public class AuthApiClient {

    private final RequestSpecification requestSpec;

    public AuthApiClient() {
        this.requestSpec = ApiConfig.getInstance().baseRequestSpec();
    }

    public Response login(String username, String password) {
        AuthRequest request = new AuthRequest()
                .withUsername(username)
                .withPassword(password);

        return given().spec(requestSpec)
                .body(request)
                .when()
                .post("/auth/login");
    }
}
