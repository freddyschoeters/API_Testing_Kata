package com.booking.stepdefinitions;

import com.booking.client.AuthApiClient;
import com.booking.context.TestContext;
import com.booking.mappers.AuthMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class AuthenticationSteps {

    private final AuthApiClient authApiClient;
    private final TestContext context;

    public AuthenticationSteps(AuthApiClient authApiClient, TestContext context) {
        this.authApiClient = authApiClient;
        this.context = context;
    }

    @Given("I am authenticated as {string} with password {string}")
    public void iAmAuthenticatedAsWithPassword(String username, String password) {
        Response response = authApiClient.login(username, password);
        response.then().statusCode(200);
        context.set("authToken", AuthMapper.tokenFromResponse(response));
    }

    @When("I authenticate with username {string} and password {string}")
    public void iAuthenticateWithUsernameAndPassword(String username, String password) {
        context.set("loginResponse", authApiClient.login(username, password));
    }

    @Then("the authentication is successful and a token is returned")
    public void theAuthenticationIsSuccessfulAndATokenIsReturned() {
        Response loginResponse = context.get("loginResponse");
        loginResponse.then().statusCode(200);
        String token = AuthMapper.tokenFromResponse(loginResponse);
        if (token == null) {
            throw new AssertionError("Expected a token but got none");
        }
        context.set("authToken", token);
    }

    @Then("the authentication fails with status code {int}")
    public void theAuthenticationFailsWithStatusCode(int statusCode) {
        Response loginResponse = context.get("loginResponse");
        loginResponse.then().statusCode(statusCode);
    }
}
