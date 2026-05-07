@booking
Feature: Booking API

  # ─── Create Booking ───────────────────────────────────────────────────────────
  @smoke @create
  Scenario: Successfully create a booking with valid data
    When I create a booking with valid details
    Then the response status should be 201
    And the response should contain a booking id
    And the response should contain the booking details
    And the response should match the booking schema

  # ─── Get Booking ─────────────────────────────────────────────────────────────
  @smoke @read
  Scenario: Retrieve a booking by ID with valid authentication
    Given a valid booking exists
    When I retrieve the booking with a valid token
    Then the response status should be 200
    And the response should contain field "firstname" with value "John"
    And the response should contain field "lastname" with value "Doe"