@booking
Feature: Retrieve Booking

  @smoke @read
  Scenario: Retrieve a booking by ID with valid authentication
    Given a valid booking exists
    When I retrieve the booking with a valid token
    Then the response status should be 200
    And the response should contain field "firstname" with value "John"
    And the response should contain field "lastname" with value "Doe"