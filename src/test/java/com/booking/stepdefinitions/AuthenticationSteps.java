package com.booking.stepdefinitions;

import com.booking.client.AuthApiClient;
import com.booking.context.TestContext;
import com.booking.model.dto.AuthResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

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
        context.setAuthToken(response.as(AuthResponse.class).getToken());
    }

    @When("I authenticate with username {string} and password {string}")
    public void iAuthenticateWithUsernameAndPassword(String username, String password) {
        context.setLastResponse(authApiClient.login(username, password));
    }

    @Then("the authentication is successful and a token is returned")
    public void theAuthenticationIsSuccessfulAndATokenIsReturned() {
        context.getLastResponse().then().statusCode(200);
        AuthResponse response = context.getLastResponse().as(AuthResponse.class);
        assertThat(response.getToken(), not(blankOrNullString()));
        context.setAuthToken(response.getToken());
    }

    @Then("the authentication fails with status code {int}")
    public void theAuthenticationFailsWithStatusCode(int statusCode) {
        context.getLastResponse().then().statusCode(statusCode);
    }
}
