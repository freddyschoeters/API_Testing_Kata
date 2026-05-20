package com.api.tests.clients;

import com.api.framework.constants.ApiConstants;
import com.api.framework.config.EnvironmentConfig;
import com.api.framework.specifications.RequestSpecifications;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Client for authentication operations.
 * Caches token to avoid repeated login calls.
 */
public class AuthClient {

    private static final Logger log = LoggerFactory.getLogger(AuthClient.class);
    private static String cachedToken;

    public static String getAuthToken() {
        if (cachedToken != null) {
            return cachedToken;
        }
        EnvironmentConfig config = EnvironmentConfig.getInstance();
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", config.getAdminUsername());
        credentials.put("password", config.getAdminPassword());

        log.info("Obtaining auth token for user: {}", config.getAdminUsername());

        Response response = RequestSpecifications.baseSpec()
                .body(credentials)
                .post(ApiConstants.AUTH_ENDPOINT);

        if (response.statusCode() != ApiConstants.STATUS_OK) {
            throw new RuntimeException("Authentication failed. Status: "
                    + response.statusCode());
        }
        cachedToken = response.jsonPath().getString("token");
        log.info("Auth token obtained successfully");
        return cachedToken;
    }

    public static void clearToken() {
        cachedToken = null;
    }
}