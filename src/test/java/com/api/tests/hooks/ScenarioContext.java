package com.api.tests.hooks;

import io.restassured.response.Response;

public class ScenarioContext {

    private static final ThreadLocal<ScenarioContext> INSTANCE =
            ThreadLocal.withInitial(ScenarioContext::new);

    private Response lastResponse;
    private Integer lastCreatedBookingId;
    private String authToken;
    private String lastCheckinDate;
    private String lastCheckoutDate;

    private ScenarioContext() {}

    public static ScenarioContext get() {
        return INSTANCE.get();
    }

    public static void reset() {
        INSTANCE.remove();
    }

    public Response getLastResponse() { return lastResponse; }
    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Integer getLastCreatedBookingId() { return lastCreatedBookingId; }
    public void setLastCreatedBookingId(Integer id) {
        this.lastCreatedBookingId = id;
    }

    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getLastCheckinDate() { return lastCheckinDate; }
    public void setLastCheckinDate(String date) { this.lastCheckinDate = date; }

    public String getLastCheckoutDate() { return lastCheckoutDate; }
    public void setLastCheckoutDate(String date) { this.lastCheckoutDate = date; }
}