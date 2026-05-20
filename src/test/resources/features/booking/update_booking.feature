@booking
Feature: Update Booking

  @wip @update
  Scenario: Update a booking with valid authentication
    Given a valid booking exists
    When I update the booking with valid details
    Then the response status should be 200
    And the response should contain field "firstname" with value "UpdatedName"
    And the response should contain field "lastname" with value "UpdatedLastname"