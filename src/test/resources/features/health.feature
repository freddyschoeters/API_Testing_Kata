@health
Feature: Booking API health check

  Scenario: The booking API is up and running
    When I check the health of the booking API
    Then the API reports that it is up
