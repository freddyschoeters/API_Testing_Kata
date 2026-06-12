package com.booking.hooks;

import com.booking.client.AuthApiClient;
import com.booking.client.BookingApiClient;
import com.booking.config.ApiConfig;
import com.booking.context.TestContext;
import com.booking.model.dto.AuthResponse;
import com.booking.model.dto.HealthStatus;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.restassured.response.Response;

/**
 * Cleans up any booking created during a scenario, so the test suite
 * does not leave data behind between runs.
 */
public class Hooks {

    private final TestContext context;
    private final BookingApiClient bookingApiClient;
    private final AuthApiClient authApiClient;

    public Hooks(TestContext context, BookingApiClient bookingApiClient, AuthApiClient authApiClient) {
        this.context = context;
        this.bookingApiClient = bookingApiClient;
        this.authApiClient = authApiClient;
    }

    @BeforeAll
    public static void checkApiIsUp() {
        Response response = new BookingApiClient().health();
        if (response.getStatusCode() != 200) {
            throw new IllegalStateException("Booking API health check returned status " + response.getStatusCode());
        }

        HealthStatus status = response.as(HealthStatus.class);
        if (!"UP".equals(status.getStatus())) {
            throw new IllegalStateException("Booking API is not up: " + status.getStatus());
        }
    }

    @After
    public void deleteCreatedBooking() {
        Integer bookingId = context.get("bookingId");
        if (bookingId == null) {
            return;
        }

        String token = context.get("authToken");
        if (token == null) {
            ApiConfig config = ApiConfig.getInstance();
            token = authApiClient.login(config.authUsername(), config.authPassword())
                    .as(AuthResponse.class)
                    .getToken();
        }

        bookingApiClient.delete(bookingId, token);
    }
}
