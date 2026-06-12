package com.booking.hooks;

import com.booking.client.AuthApiClient;
import com.booking.client.BookingApiClient;
import com.booking.config.ApiConfig;
import com.booking.context.TestContext;
import com.booking.model.dto.AuthResponse;
import io.cucumber.java.After;

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

    @After
    public void deleteCreatedBooking() {
        Integer bookingId = context.getCreatedBookingId();
        if (bookingId == null) {
            return;
        }

        String token = context.getAuthToken();
        if (token == null) {
            ApiConfig config = ApiConfig.getInstance();
            token = authApiClient.login(config.authUsername(), config.authPassword())
                    .as(AuthResponse.class)
                    .getToken();
        }

        bookingApiClient.delete(bookingId, token);
    }
}
