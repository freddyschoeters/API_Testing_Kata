package com.api.tests.hooks;

import com.api.framework.core.RestAssuredConfig;
import com.api.tests.clients.AuthClient;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHooks {

    private static final Logger log = LoggerFactory.getLogger(TestHooks.class);

    @BeforeAll
    public static void globalSetup() {
        log.info("=== Initialising RestAssured configuration ===");
        RestAssuredConfig.setup();
    }

    @Before
    public void beforeEachScenario() {
        log.info("--- Resetting scenario context ---");
        ScenarioContext.reset();
        String token = AuthClient.getAuthToken();
        ScenarioContext.get().setAuthToken(token);
    }

    @After
    public void afterEachScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("Scenario FAILED: {}", scenario.getName());
            Response lastResponse = ScenarioContext.get().getLastResponse();
            if (lastResponse != null) {
                log.error("Last response status: {}", lastResponse.statusCode());
                log.error("Last response body: {}", lastResponse.body().asString());
            }
        } else {
            log.info("Scenario PASSED: {}", scenario.getName());
        }
        ScenarioContext.reset();
    }
}