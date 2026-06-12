@authentication
Feature: Authentication

  Scenario: Successful login with valid credentials returns a token
    When I authenticate with username "admin" and password "password"
    Then the authentication is successful and a token is returned

  Scenario: Login with invalid credentials is rejected
    When I authenticate with username "admin" and password "wrong-password"
    Then the authentication fails with status code 401
