package com.booking.stepdefinitions;

import com.booking.client.BookingApiClient;
import com.booking.context.TestContext;
import com.booking.model.Booking;
import com.booking.model.dto.BookingWrapperResponse;
import com.booking.model.dto.PatchBookingRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.LocalDate;

import static com.booking.builders.BookingTestDataBuilder.aBooking;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingLifecycleSteps {

    private final BookingApiClient bookingApiClient;
    private final TestContext context;

    public BookingLifecycleSteps(BookingApiClient bookingApiClient, TestContext context) {
        this.bookingApiClient = bookingApiClient;
        this.context = context;
    }

    @Given("a booking exists")
    public void aBookingExists() {
        Booking booking = aBooking().build();
        Response response = bookingApiClient.create(booking);
        context.setCreatedBookingId(response.jsonPath().getInt("bookingid"));
        context.setCreatedBooking(booking);
    }

    @When("I retrieve the booking with a valid token")
    public void iRetrieveTheBookingWithAValidToken() {
        context.setLastResponse(bookingApiClient.getById(context.getCreatedBookingId(), context.getAuthToken()));
    }

    @When("I retrieve the booking without a token")
    public void iRetrieveTheBookingWithoutAToken() {
        context.setLastResponse(bookingApiClient.getById(context.getCreatedBookingId(), null));
    }

    @And("the booking details match what was created")
    public void theBookingDetailsMatchWhatWasCreated() {
        Booking retrieved = context.getLastResponse().as(Booking.class);
        assertCoreFieldsMatch(context.getCreatedBooking(), retrieved);
    }

    @When("I update the booking with new details using a valid token")
    public void iUpdateTheBookingWithNewDetailsUsingAValidToken() {
        Booking updated = aBooking()
                .withFirstname("Updated")
                .withLastname("Guest")
                .withCheckin(LocalDate.now().plusDays(10))
                .withCheckout(LocalDate.now().plusDays(12))
                .build();
        context.setLastResponse(bookingApiClient.update(context.getCreatedBookingId(), updated, context.getAuthToken()));
        context.setCreatedBooking(updated);
    }

    @When("I update the booking without a token")
    public void iUpdateTheBookingWithoutAToken() {
        Booking updated = aBooking().build();
        context.setLastResponse(bookingApiClient.update(context.getCreatedBookingId(), updated, null));
    }

    @And("the response contains the updated booking details")
    public void theResponseContainsTheUpdatedBookingDetails() {
        Booking updated = context.getLastResponse().as(BookingWrapperResponse.class).getBooking();
        assertCoreFieldsMatch(context.getCreatedBooking(), updated);
    }

    @When("I partially update the booking's first name using a valid token")
    public void iPartiallyUpdateTheBookingsFirstNameUsingAValidToken() {
        PatchBookingRequest patch = new PatchBookingRequest().withFirstname("Patched");
        context.setLastResponse(bookingApiClient.patch(context.getCreatedBookingId(), patch, context.getAuthToken()));
    }

    @When("I delete the booking with a valid token")
    public void iDeleteTheBookingWithAValidToken() {
        context.setLastResponse(bookingApiClient.delete(context.getCreatedBookingId(), context.getAuthToken()));
        context.setCreatedBookingId(null);
    }

    @When("I delete the booking without a token")
    public void iDeleteTheBookingWithoutAToken() {
        context.setLastResponse(bookingApiClient.delete(context.getCreatedBookingId(), null));
    }

    @Then("the booking is deleted successfully")
    public void theBookingIsDeletedSuccessfully() {
        context.getLastResponse().then().statusCode(202);
    }

    @Then("the request succeeds with status code {int}")
    public void theRequestSucceedsWithStatusCode(int statusCode) {
        context.getLastResponse().then().statusCode(statusCode);
    }

    @Then("the request is rejected with status code {int}")
    public void theRequestIsRejectedWithStatusCode(int statusCode) {
        context.getLastResponse().then().statusCode(statusCode);
    }

    private void assertCoreFieldsMatch(Booking expected, Booking actual) {
        assertThat(actual.getRoomid(), equalTo(expected.getRoomid()));
        assertThat(actual.getFirstname(), equalTo(expected.getFirstname()));
        assertThat(actual.getLastname(), equalTo(expected.getLastname()));
        assertThat(actual.getDepositpaid(), equalTo(expected.getDepositpaid()));
        assertThat(actual.getBookingdates().getCheckin(), equalTo(expected.getBookingdates().getCheckin()));
        assertThat(actual.getBookingdates().getCheckout(), equalTo(expected.getBookingdates().getCheckout()));
    }
}
