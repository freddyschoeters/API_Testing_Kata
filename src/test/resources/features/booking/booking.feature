@booking
Feature: Booking API

  @smoke @create
  Scenario: Successfully create a booking with valid data
    When I create a booking with valid details
    Then the response status should be 201
    And the response should contain a booking id
    And the response should contain the booking details
    And the response should match the booking schema