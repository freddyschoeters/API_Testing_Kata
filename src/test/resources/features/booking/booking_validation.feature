@booking
Feature: Booking Validation

  @create @negative
  Scenario: Create a booking with short firstname and lastname returns 400
    When I create a booking with firstname "Jo" and lastname "Do"
    Then the response status should be 400
    And the response should contain error message

  @create @negative
  Scenario: Create a booking with invalid dates returns 400
    When I create a booking with invalid dates "2026-00-10" and "2026-00-01"
    Then the response status should be 400
    And the response should contain error message

  @create @negative
  Scenario Outline: Create a booking with an invalid email returns 400
    When I create a booking with an invalid email "<email>"
    Then the response status should be 400
    And the response should contain error message

    Examples:
      | email       |
      | invalid.com |
      | @test.com   |
      | plaintext   |
      | john@       |

  @create @negative
  Scenario: Create a booking with a phone number that is too short returns 400
    When I create a booking with an invalid phone number "9125"
    Then the response status should be 400
    And the response should contain error message