package com.booking.context;

import com.booking.model.Booking;
import io.restassured.response.Response;

/**
 * Scenario-scoped state shared between step definition classes.
 * A new instance is created per scenario by cucumber-picocontainer.
 */
public class TestContext {

    private String authToken;
    private Integer createdBookingId;
    private Booking createdBooking;
    private Response lastResponse;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isAuthenticated() {
        return authToken != null;
    }

    public Integer getCreatedBookingId() {
        return createdBookingId;
    }

    public void setCreatedBookingId(Integer createdBookingId) {
        this.createdBookingId = createdBookingId;
    }

    public Booking getCreatedBooking() {
        return createdBooking;
    }

    public void setCreatedBooking(Booking createdBooking) {
        this.createdBooking = createdBooking;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }
}
