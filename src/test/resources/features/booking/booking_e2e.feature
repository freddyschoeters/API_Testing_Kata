@booking
Feature: Booking End to End

  @e2e @smoke
  Scenario: Full booking lifecycle - Create, Retrieve and Delete
    When I create a booking with valid details
    Then the response status should be 201
    And the response should contain a booking id
    And the response should contain the booking details
    And the response should match the booking schema
    When I retrieve the booking with a valid token
    Then the response status should be 200
    And the response should contain field "firstname" with value "John"
    And the response should contain field "lastname" with value "Doe"
    When I delete the booking with a valid token
    Then the response status should be 202