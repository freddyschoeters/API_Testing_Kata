@booking
Feature: Booking Authentication

  @security
  Scenario: Retrieve a booking without authentication returns 403
    Given a valid booking exists
    When I retrieve the booking without a token
    Then the response status should be 403

  @security
  Scenario: Delete a booking without authentication returns 403
    Given a valid booking exists
    When I delete the booking without a token
    Then the response status should be 403