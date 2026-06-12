@booking-lifecycle
Feature: Booking lifecycle

  As an authenticated administrator
  I want to read, update, partially update and delete bookings
  So that I can manage existing reservations

  Background:
    Given I am authenticated as "admin" with password "password"
    And a booking exists

  Scenario: Retrieve an existing booking with a valid token
    When I retrieve the booking with a valid token
    Then the request succeeds with status code 200
    And the booking details match what was created

  Scenario: Retrieve a booking without a token is rejected
    When I retrieve the booking without a token
    Then the request is rejected with status code 403

  Scenario: Update an existing booking with a valid token
    When I update the booking with new details using a valid token
    Then the request succeeds with status code 200
    And the response contains the updated booking details

  Scenario: Update a booking without a token is rejected
    When I update the booking without a token
    Then the request is rejected with status code 403

  Scenario: Partially updating a booking via PATCH is not supported
    When I partially update the booking's first name using a valid token
    Then the request is rejected with status code 405

  Scenario: Delete an existing booking with a valid token
    When I delete the booking with a valid token
    Then the booking is deleted successfully

  Scenario: Delete a booking without a token is rejected
    When I delete the booking without a token
    Then the request is rejected with status code 403

  Scenario: Search for bookings by room with a valid token
    When I search for bookings in the booking's room with a valid token
    Then the request succeeds with status code 200
    And the search results include the created booking
