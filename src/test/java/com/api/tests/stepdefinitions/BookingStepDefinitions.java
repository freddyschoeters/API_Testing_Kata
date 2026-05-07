package com.api.tests.stepdefinitions;

import com.api.framework.utils.ResponseValidator;
import com.api.tests.builders.BookingRequestBuilder;
import com.api.tests.clients.BookingClient;
import com.api.tests.hooks.ScenarioContext;
import com.api.tests.models.request.BookingRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class BookingStepDefinitions {

    private final BookingClient bookingClient = new BookingClient();

    @When("I create a booking with valid details")
    public void iCreateABookingWithValidDetails() {
        BookingRequest request = BookingRequestBuilder.validBooking();
        Response response = bookingClient.createBooking(request);
        ScenarioContext.get().setLastResponse(response);
        if (response.statusCode() == 201) {
            int bookingId = response.jsonPath().getInt("bookingid");
            ScenarioContext.get().setLastCreatedBookingId(bookingId);
        }
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        ResponseValidator.assertStatusCode(
                ScenarioContext.get().getLastResponse(), expectedStatus);
    }

    @Then("the response should contain a booking id")
    public void theResponseShouldContainABookingId() {
        ResponseValidator.assertBodyFieldNotNull(
                ScenarioContext.get().getLastResponse(), "bookingid");
    }

    @Then("the response should contain the booking details")
    public void theResponseShouldContainBookingDetails() {
        Response response = ScenarioContext.get().getLastResponse();
        ResponseValidator.assertBodyFieldNotNull(response, "bookingid");
        ResponseValidator.assertBodyFieldNotNull(response, "roomid");
        ResponseValidator.assertBodyFieldNotNull(response, "firstname");
        ResponseValidator.assertBodyFieldNotNull(response, "lastname");
        ResponseValidator.assertBodyFieldNotNull(response, "depositpaid");
        ResponseValidator.assertBodyFieldNotNull(response, "bookingdates.checkin");
        ResponseValidator.assertBodyFieldNotNull(response, "bookingdates.checkout");
    }

    @Then("the response should match the booking schema")
    public void theResponseShouldMatchBookingSchema() {
        ResponseValidator.assertJsonSchema(
                ScenarioContext.get().getLastResponse(),
                "schemas/booking-response-schema.json");
    }
}