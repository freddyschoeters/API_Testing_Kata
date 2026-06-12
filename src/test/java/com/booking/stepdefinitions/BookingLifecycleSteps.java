package com.booking.stepdefinitions;

import com.booking.client.BookingApiClient;
import com.booking.context.TestContext;
import com.booking.mappers.BookingMapper;
import com.booking.model.Booking;
import com.booking.model.dto.BookingSearchResponse;
import com.booking.model.dto.PatchBookingRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.LocalDate;

import static com.booking.builders.BookingTestDataBuilder.aBooking;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingLifecycleSteps {

    private final BookingApiClient bookingApiClient;
    private final TestContext context;

    public BookingLifecycleSteps(BookingApiClient bookingApiClient, TestContext context) {
        this.bookingApiClient = bookingApiClient;
        this.context = context;
    }

    @Given("a booking exists")
    public void aBookingExists() {
        Booking bookingBody = aBooking().build();
        Response response = bookingApiClient.create(bookingBody);
        context.set("bookingId", BookingMapper.bookingIdFromResponse(response));
        context.set("bookingBody", bookingBody);
    }

    @When("I retrieve the booking with a valid token")
    public void iRetrieveTheBookingWithAValidToken() {
        Integer bookingId = context.get("bookingId");
        String authToken = context.get("authToken");
        context.set("apiResponse", bookingApiClient.getById(bookingId, authToken));
    }

    @When("I retrieve the booking without a token")
    public void iRetrieveTheBookingWithoutAToken() {
        Integer bookingId = context.get("bookingId");
        context.set("apiResponse", bookingApiClient.getById(bookingId, null));
    }

    @And("the booking details match what was created")
    public void theBookingDetailsMatchWhatWasCreated() {
        Response response = context.get("apiResponse");
        Booking retrieved = response.as(Booking.class);
        Booking expected = context.get("bookingBody");
        assertCoreFieldsMatch(expected, retrieved);
    }

    @When("I update the booking with new details using a valid token")
    public void iUpdateTheBookingWithNewDetailsUsingAValidToken() {
        // Shift the dates a year past the booking's own current dates so the
        // update doesn't self-conflict with its existing reservation.
        Booking bookingBody = context.get("bookingBody");
        LocalDate newCheckin = bookingBody.getBookingdates().getCheckin().plusYears(1);
        Booking updated = aBooking()
                .withFirstname("Updated")
                .withLastname("Guest")
                .withCheckin(newCheckin)
                .withCheckout(newCheckin.plusDays(2))
                .build();
        Integer bookingId = context.get("bookingId");
        String authToken = context.get("authToken");
        context.set("apiResponse", bookingApiClient.update(bookingId, updated, authToken));
        context.set("bookingBody", updated);
    }

    @When("I update the booking without a token")
    public void iUpdateTheBookingWithoutAToken() {
        Booking updated = aBooking().build();
        Integer bookingId = context.get("bookingId");
        context.set("apiResponse", bookingApiClient.update(bookingId, updated, null));
    }

    @And("the response contains the updated booking details")
    public void theResponseContainsTheUpdatedBookingDetails() {
        Response response = context.get("apiResponse");
        Booking updated = BookingMapper.fromWrapperResponse(response);
        Booking expected = context.get("bookingBody");
        assertCoreFieldsMatch(expected, updated);
    }

    @When("I partially update the booking's first name using a valid token")
    public void iPartiallyUpdateTheBookingsFirstNameUsingAValidToken() {
        PatchBookingRequest patch = new PatchBookingRequest().withFirstname("Patched");
        Integer bookingId = context.get("bookingId");
        String authToken = context.get("authToken");
        context.set("apiResponse", bookingApiClient.patch(bookingId, patch, authToken));
    }

    @When("I delete the booking with a valid token")
    public void iDeleteTheBookingWithAValidToken() {
        Integer bookingId = context.get("bookingId");
        String authToken = context.get("authToken");
        context.set("apiResponse", bookingApiClient.delete(bookingId, authToken));
        context.set("bookingId", null);
    }

    @When("I delete the booking without a token")
    public void iDeleteTheBookingWithoutAToken() {
        Integer bookingId = context.get("bookingId");
        context.set("apiResponse", bookingApiClient.delete(bookingId, null));
    }

    @When("I search for bookings in the booking's room with a valid token")
    public void iSearchForBookingsInTheBookingsRoomWithAValidToken() {
        Booking bookingBody = context.get("bookingBody");
        String authToken = context.get("authToken");
        context.set("apiResponse", bookingApiClient.search(bookingBody.getRoomid(), authToken));
    }

    @And("the search results include the created booking")
    public void theSearchResultsIncludeTheCreatedBooking() {
        Response response = context.get("apiResponse");
        BookingSearchResponse searchResponse = response.as(BookingSearchResponse.class);
        Booking expected = context.get("bookingBody");
        boolean found = searchResponse.getBookings().stream()
                .anyMatch(booking -> coreFieldsMatch(expected, booking));
        assertTrue(found, "Expected the search results to include the created booking");
    }

    @Then("the booking is deleted successfully")
    public void theBookingIsDeletedSuccessfully() {
        Response response = context.get("apiResponse");
        response.then().statusCode(202);
    }

    @Then("the request succeeds with status code {int}")
    public void theRequestSucceedsWithStatusCode(int statusCode) {
        Response response = context.get("apiResponse");
        response.then().statusCode(statusCode);
    }

    @Then("the request is rejected with status code {int}")
    public void theRequestIsRejectedWithStatusCode(int statusCode) {
        Response response = context.get("apiResponse");
        response.then().statusCode(statusCode);
    }

    private void assertCoreFieldsMatch(Booking expected, Booking actual) {
        assertEquals(expected.getRoomid(), actual.getRoomid());
        assertEquals(expected.getFirstname(), actual.getFirstname());
        assertEquals(expected.getLastname(), actual.getLastname());
        assertEquals(expected.getDepositpaid(), actual.getDepositpaid());
        assertEquals(expected.getBookingdates().getCheckin(), actual.getBookingdates().getCheckin());
        assertEquals(expected.getBookingdates().getCheckout(), actual.getBookingdates().getCheckout());
    }

    private boolean coreFieldsMatch(Booking expected, Booking actual) {
        return expected.getRoomid().equals(actual.getRoomid())
                && expected.getFirstname().equals(actual.getFirstname())
                && expected.getLastname().equals(actual.getLastname())
                && expected.getDepositpaid().equals(actual.getDepositpaid())
                && expected.getBookingdates().getCheckin().equals(actual.getBookingdates().getCheckin())
                && expected.getBookingdates().getCheckout().equals(actual.getBookingdates().getCheckout());
    }
}
