package com.api.tests.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for booking API scenarios.
 * Following TDD approach - steps defined before implementation.
 */
public class BookingStepDefinitions {

    @When("I create a booking with valid details")
    public void iCreateABookingWithValidDetails() {
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
    }

    @Then("the response should contain a booking id")
    public void theResponseShouldContainABookingId() {
    }

    @Then("the response should contain the booking details")
    public void theResponseShouldContainBookingDetails() {
    }

    @Then("the response should match the booking schema")
    public void theResponseShouldMatchBookingSchema() {
    }
}