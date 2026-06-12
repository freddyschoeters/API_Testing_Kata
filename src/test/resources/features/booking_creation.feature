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

  Scenario Outline: Creating a booking with a firstname at boundary values
    When I create a booking with an invalid "firstname" of "<value>"
    Then the booking creation fails with status code 400
    And the response contains a validation error
    Examples:
      | value |
      | Jo    |
      | J     |

  Scenario Outline: Creating a booking with a firstname at valid boundary values
    When I create a booking with a valid "firstname" of "<value>"
    Then the booking is created successfully
    Examples:
      | value              |
      | Jim                |
      | JimJimJimJimJimJim |

  Scenario Outline: Creating a booking with a phone at boundary values
    When I create a booking with an invalid "phone" of "<value>"
    Then the booking creation fails with status code 400
    And the response contains a validation error
    Examples:
      | value      |
      | 1234567890 |
      | 1234       |

  Scenario Outline: Creating a booking with a phone at valid boundary values
    When I create a booking with a valid "phone" of "<value>"
    Then the booking is created successfully
    Examples:
      | value                 |
      | 12345678901           |
      | 123456789012345678901 |

  Scenario Outline: Creating a booking with an invalid email format is rejected
    When I create a booking with an invalid "email" of "<value>"
    Then the booking creation fails with status code 400
    And the response contains a validation error
    Examples:
      | value          |
      | @nodomain.com  |
      | test@          |
      | test @test.com |

  Scenario Outline: Creating a booking with a missing mandatory field is rejected
    When I create a booking without the "<field>" field
    Then the booking creation fails with status code <errorCode>
    And the response contains a validation error
    Examples:
      | field        | errorCode |
      | firstname    | 400       |
      | lastname     | 400       |
      | email        | 400       |
      | phone        | 400       |
      | bookingdates | 500       |

  Scenario: Creating a booking with roomid 0 is rejected
    When I create a booking with roomid 0
    Then the booking creation fails with status code 400
    And the response contains a validation error

  Scenario: Creating a booking where checkout equals checkin is rejected
    When I create a booking where checkout equals checkin
    Then the booking creation fails with status code 400
    And the response contains an error message