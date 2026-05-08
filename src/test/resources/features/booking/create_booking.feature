@booking
Feature: Create Booking

  @smoke @create
  Scenario: Successfully create a booking with valid data
    When I create a booking with valid details
    Then the response status should be 201
    And the response should contain a booking id
    And the response should contain the booking details
    And the response should match the booking schema

  @smoke @create @datatable
  Scenario: Successfully create a booking using data table
    When I create a booking with the following details:
      | firstname | John          |
      | lastname  | Doe           |
      | email     | john@test.com |
      | phone     | 07123456789   |
    Then the response status should be 201
    And the response should contain a booking id
    And the response should contain the booking details
    And the response should match the booking schema