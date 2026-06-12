package com.booking.stepdefinitions;

import com.booking.builders.BookingTestDataBuilder;
import com.booking.client.BookingApiClient;
import com.booking.context.TestContext;
import com.booking.model.Booking;
import com.booking.model.dto.ErrorResponse;
import com.booking.model.dto.ValidationErrorResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static com.booking.builders.BookingTestDataBuilder.aBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.blankOrNullString;

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
        context.setLastResponse(bookingApiClient.create(builder.build()));
    }

    @Then("the booking is created successfully")
    public void theBookingIsCreatedSuccessfully() {
        context.getLastResponse().then().statusCode(201);
    }

    @And("the response contains the created booking details")
    public void theResponseContainsTheCreatedBookingDetails() {
        Booking created = context.getLastResponse().as(Booking.class);
        Integer bookingId = context.getLastResponse().jsonPath().getInt("bookingid");
        assertThat(bookingId, notNullValue());
        assertThat(created.getFirstname(), equalTo("Jim"));
        assertThat(created.getLastname(), equalTo("Brown"));
        context.setCreatedBookingId(bookingId);
    }

    @Then("the booking creation fails with status code {int}")
    public void theBookingCreationFailsWithStatusCode(int statusCode) {
        context.getLastResponse().then().statusCode(statusCode);
    }

    @And("the response contains a validation error")
    public void theResponseContainsAValidationError() {
        ValidationErrorResponse response = context.getLastResponse().as(ValidationErrorResponse.class);
        assertThat(response.getErrors(), is(not(empty())));
    }

    @And("the response contains an error message")
    public void theResponseContainsAnErrorMessage() {
        ErrorResponse response = context.getLastResponse().as(ErrorResponse.class);
        assertThat(response.getError(), is(not(blankOrNullString())));
    }
}
