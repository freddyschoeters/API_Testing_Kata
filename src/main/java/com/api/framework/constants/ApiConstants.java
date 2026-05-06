package com.api.framework.constants;

/**
 * Central place for all API endpoint paths and shared constants.
 */
public class ApiConstants {

    private ApiConstants() {}

    // Endpoint paths
    public static final String HEALTH_ENDPOINT  = "/booking/actuator/health";
    public static final String AUTH_ENDPOINT    = "/auth/login";
    public static final String BOOKING_ENDPOINT = "/booking";
    public static final String BOOKING_BY_ID    = "/booking/{id}";

    // HTTP status codes
    public static final int STATUS_OK           = 200;
    public static final int STATUS_CREATED      = 201;
    public static final int STATUS_BAD_REQUEST  = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_NOT_FOUND    = 404;

    // Header keys
    public static final String HEADER_COOKIE       = "Cookie";
    public static final String CONTENT_TYPE_JSON   = "application/json";
    public static final String COOKIE_TOKEN_PREFIX = "token=";
}