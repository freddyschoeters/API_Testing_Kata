package com.booking.stepdefinitions;

import com.booking.builders.BookingTestDataBuilder;
import com.booking.client.BookingApiClient;
import com.booking.context.TestContext;
import com.booking.mappers.BookingMapper;
import com.booking.model.Booking;
import com.booking.model.dto.ErrorResponse;
import com.booking.model.dto.ValidationErrorResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.LocalDate;

import static com.booking.builders.BookingTestDataBuilder.aBooking;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingCreationSteps {

    private final BookingApiClient bookingApiClient;
    private final TestContext context;

    public BookingCreationSteps(BookingApiClient bookingApiClient, TestContext context) {
        this.bookingApiClient = bookingApiClient;
        this.context = context;
    }

    @When("I create a booking with valid details")
    public void iCreateABookingWithValidDetails() {
        createBooking(aBooking());
    }

    @When("I create a booking with an invalid {string} of {string}")
    public void iCreateABookingWithAnInvalidFieldOf(String field, String value) {
        BookingTestDataBuilder builder = aBooking();
        switch (field) {
            case "firstname" -> builder.withFirstname(value);
            case "lastname" -> builder.withLastname(value);
            case "email" -> builder.withEmail(value);
            case "phone" -> builder.withPhone(value);
            default -> throw new IllegalArgumentException("Unsupported field: " + field);
        }
        createBooking(builder);
    }

    @When("I create a booking where the checkout date is before the checkin date")
    public void iCreateABookingWhereTheCheckoutDateIsBeforeTheCheckinDate() {
        BookingTestDataBuilder builder = aBooking()
                .withCheckin(LocalDate.now().plusDays(5))
                .withCheckout(LocalDate.now().plusDays(1));
        createBooking(builder);
    }

    private void createBooking(BookingTestDataBuilder builder) {
        context.set("createBookingResponse", bookingApiClient.create(builder.build()));
    }

    @Then("the response contains a validation error")
    public void theResponseContainsAValidationError() {
        Response response = context.get("createBookingResponse");
        ValidationErrorResponse validationErrorResponse = response.as(ValidationErrorResponse.class);
        assertFalse(validationErrorResponse.getErrors().isEmpty());
    }

    @Then("the response contains an error message")
    public void theResponseContainsAnErrorMessage() {
        Response response = context.get("createBookingResponse");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertFalse(errorResponse.getError() == null || errorResponse.getError().isBlank());
    }
    
    @Then("the response contains the created booking details")
    public void theResponseContainsTheCreatedBookingDetails() {
        Response createdBooking = context.get("createBookingResponse");
        Integer bookingId = BookingMapper.bookingIdFromResponse(createdBooking);
        assertNotNull(bookingId);

        Booking booking = createdBooking.as(Booking.class);
        assertEquals("Jim", booking.getFirstname());
        assertEquals("Brown", booking.getLastname());
        context.set("bookingId", bookingId);
    }

    @Then("the booking is created successfully")
    public void theBookingIsCreatedSuccessfully() {
        Response response = context.get("createBookingResponse");
        response.then().statusCode(201);
    }

    @Then("the booking creation fails with status code {int}")
    public void theBookingCreationFailsWithStatusCode(int statusCode) {
        Response response = context.get("createBookingResponse");
        response.then().statusCode(statusCode);
    }
}
