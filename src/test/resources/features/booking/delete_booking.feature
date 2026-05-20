@booking
Feature: Delete Booking

  @smoke @delete
  Scenario: Delete a booking with valid authentication
    Given a valid booking exists
    When I delete the booking with a valid token
    Then the response status should be 202