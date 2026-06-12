package com.booking.mappers;

import com.booking.model.dto.AuthResponse;
import io.restassured.response.Response;

/**
 * Maps raw Rest-Assured responses to authentication-related values.
 */
public final class AuthMapper {

    private AuthMapper() {
    }

    /**
     * Extracts the authentication token from a successful /auth/login response.
     */
    public static String tokenFromResponse(Response response) {
        return response.as(AuthResponse.class).getToken();
    }
}
