package com.api.tests.stepdefinitions;

import com.api.framework.utils.ResponseValidator;
import com.api.tests.builders.BookingRequestBuilder;
import com.api.tests.clients.BookingClient;
import com.api.tests.hooks.ScenarioContext;
import com.api.tests.models.request.BookingRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import java.util.Map;

public class BookingStepDefinitions {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(BookingStepDefinitions.class);
    private final BookingClient bookingClient = new BookingClient();

    @When("I create a booking with valid details")
    public void iCreateABookingWithValidDetails() {
        Response response = null;
        for (int attempt = 1; attempt <= 10; attempt++) {
            BookingRequest request = BookingRequestBuilder.validBooking();
            response = bookingClient.createBooking(request);
            if (response.statusCode() == 201) {
                ScenarioContext.get().setLastCreatedBookingId(
                        response.jsonPath().getInt("bookingid"));
                break;
            }
            log.warn("Attempt {} failed with status {}, retrying...", attempt, response.statusCode());
        }
        ScenarioContext.get().setLastResponse(response);
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

    @Then("the response should match the booking request data")
    public void theResponseShouldMatchTheBookingRequestData() {
        Response response = ScenarioContext.get().getLastResponse();
        ResponseValidator.assertBodyContains(response, "firstname", "John");
        ResponseValidator.assertBodyContains(response, "lastname", "Doe");
        ResponseValidator.assertBodyContains(response, "depositpaid", true);
        ResponseValidator.assertBodyContains(response, "bookingdates.checkin",
                ScenarioContext.get().getLastCheckinDate());
        ResponseValidator.assertBodyContains(response, "bookingdates.checkout",
                ScenarioContext.get().getLastCheckoutDate());
    }

    @Given("a valid booking exists")
    public void aValidBookingExists() {
        Response response = null;
        BookingRequest request = null;
        for (int attempt = 1; attempt <= 10; attempt++) {
            request = BookingRequestBuilder.validBooking();
            response = bookingClient.createBooking(request);
            if (response.statusCode() == 201) {
                ScenarioContext.get().setLastCreatedBookingId(
                        response.jsonPath().getInt("bookingid"));
                ScenarioContext.get().setLastCheckinDate(
                        request.getBookingdates().getCheckin());
                ScenarioContext.get().setLastCheckoutDate(
                        request.getBookingdates().getCheckout());
                break;
            }
            log.warn("Attempt {} failed with status {}, retrying...", attempt, response.statusCode());
        }
    }

    @When("I retrieve the booking with a valid token")
    public void iRetrieveTheBookingWithValidToken() {
        int bookingId = ScenarioContext.get().getLastCreatedBookingId();
        String token = ScenarioContext.get().getAuthToken();
        Response response = bookingClient.getBookingById(bookingId, token);
        ScenarioContext.get().setLastResponse(response);
    }

    @Then("the response should contain field {string} with value {string}")
    public void theResponseShouldContainFieldWithValue(String field, String value) {
        ResponseValidator.assertBodyContains(
                ScenarioContext.get().getLastResponse(), field, value);
    }

    @When("I update the booking with valid details")
    public void iUpdateTheBookingWithValidDetails() {
        int bookingId = ScenarioContext.get().getLastCreatedBookingId();
        String token = ScenarioContext.get().getAuthToken();
        String checkin = ScenarioContext.get().getLastCheckinDate();
        String checkout = ScenarioContext.get().getLastCheckoutDate();
        BookingRequest request = BookingRequestBuilder.updatedBooking(checkin, checkout);
        Response response = bookingClient.updateBooking(bookingId, request, token);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I delete the booking with a valid token")
    public void iDeleteTheBookingWithValidToken() {
        int bookingId = ScenarioContext.get().getLastCreatedBookingId();
        String token = ScenarioContext.get().getAuthToken();
        Response response = bookingClient.deleteBooking(bookingId, token);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I retrieve the booking without a token")
    public void iRetrieveTheBookingWithoutToken() {
        int bookingId = ScenarioContext.get().getLastCreatedBookingId();
        Response response = bookingClient.getBookingByIdWithoutAuth(bookingId);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I delete the booking without a token")
    public void iDeleteTheBookingWithoutToken() {
        int bookingId = ScenarioContext.get().getLastCreatedBookingId();
        Response response = bookingClient.deleteBookingWithoutAuth(bookingId);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I create a booking with firstname {string} and lastname {string}")
    public void iCreateABookingWithFirstnameAndLastname(String firstname, String lastname) {
        BookingRequest request = BookingRequestBuilder.invalidFirstAndLastnameBooking(firstname, lastname);
        Response response = bookingClient.createBooking(request);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I create a booking with invalid dates {string} and {string}")
    public void iCreateABookingWithInvalidDates(String checkin, String checkout) {
        BookingRequest request = BookingRequestBuilder.invalidDatesBooking(checkin, checkout);
        Response response = bookingClient.createBooking(request);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I create a booking with an invalid email {string}")
    public void iCreateABookingWithAnInvalidEmail(String email) {
        BookingRequest request = BookingRequestBuilder.invalidEmail(email);
        Response response = bookingClient.createBooking(request);
        ScenarioContext.get().setLastResponse(response);
    }

    @When("I create a booking with an invalid phone number {string}")
    public void iCreateABookingWithAnInvalidPhoneNumber(String phone) {
        BookingRequest request = BookingRequestBuilder.invalidPhonenumber(phone);
        Response response = bookingClient.createBooking(request);
        ScenarioContext.get().setLastResponse(response);
    }

    @Then("the response should contain error message")
    public void theResponseShouldContainErrorMessage() {
        ResponseValidator.assertBodyFieldNotNull(
                ScenarioContext.get().getLastResponse(), "errors");
    }

    @When("I create a booking with the following details:")
    public void iCreateABookingWithFollowingDetails(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Response response = null;
        for (int attempt = 1; attempt <= 10; attempt++) {
            BookingRequest request = BookingRequestBuilder.validBookingFromTable(data);
            response = bookingClient.createBooking(request);
            if (response.statusCode() == 201) {
                ScenarioContext.get().setLastCreatedBookingId(
                        response.jsonPath().getInt("bookingid"));
                break;
            }
            log.warn("Attempt {} failed with status {}, retrying...", attempt, response.statusCode());
        }
        ScenarioContext.get().setLastResponse(response);
    }
}