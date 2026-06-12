package com.booking.stepdefinitions;

import com.booking.client.BookingApiClient;
import com.booking.context.TestContext;
import com.booking.model.dto.HealthStatus;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthSteps {

    private final BookingApiClient bookingApiClient;
    private final TestContext context;

    public HealthSteps(BookingApiClient bookingApiClient, TestContext context) {
        this.bookingApiClient = bookingApiClient;
        this.context = context;
    }

    @When("I check the health of the booking API")
    public void iCheckTheHealthOfTheBookingApi() {
        context.set("apiResponse", bookingApiClient.health());
    }

    @Then("the API reports that it is up")
    public void theApiReportsThatItIsUp() {
        Response response = context.get("apiResponse");
        response.then().statusCode(200);
        HealthStatus status = response.as(HealthStatus.class);
        assertEquals("UP", status.getStatus());
    }
}
