package com.booking.stepdefinitions;

import com.booking.client.BookingApiClient;
import com.booking.context.TestContext;
import com.booking.model.dto.HealthStatus;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HealthSteps {

    private final BookingApiClient bookingApiClient;
    private final TestContext context;

    public HealthSteps(BookingApiClient bookingApiClient, TestContext context) {
        this.bookingApiClient = bookingApiClient;
        this.context = context;
    }

    @When("I check the health of the booking API")
    public void iCheckTheHealthOfTheBookingApi() {
        context.setLastResponse(bookingApiClient.health());
    }

    @Then("the API reports that it is up")
    public void theApiReportsThatItIsUp() {
        context.getLastResponse().then().statusCode(200);
        HealthStatus status = context.getLastResponse().as(HealthStatus.class);
        assertThat(status.getStatus(), equalTo("UP"));
    }
}
