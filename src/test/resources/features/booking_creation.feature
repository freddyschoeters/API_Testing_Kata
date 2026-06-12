@booking @creation
Feature: Booking creation

  As a client of the booking API
  I want to create bookings
  So that a room can be reserved

  Scenario: Create a booking with valid data
    When I create a booking with valid details
    Then the booking is created successfully
    And the response contains the created booking details

  Scenario Outline: Creating a booking with invalid data is rejected
    When I create a booking with an invalid "<field>" of "<value>"
    Then the booking creation fails with status code 400
    And the response contains a validation error

    Examples:
      | field     | value                                    |
      | firstname | Jo                                       |
      | lastname  | ThisLastNameIsDefinitelyTooLongToBeValid |
      | email     | not-an-email                             |
      | phone     | 123                                      |

  Scenario: Creating a booking with checkout before checkin is rejected
    When I create a booking where the checkout date is before the checkin date
    Then the booking creation fails with status code 409
    And the response contains an error message
