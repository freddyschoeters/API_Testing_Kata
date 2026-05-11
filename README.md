# Booking API Test Automation

Automated API tests for the Restful Booker platform built with REST Assured, Cucumber and JUnit 5.

## Tools Used

- Java 17
- REST Assured 5.3.2
- Cucumber 7.22.2
- JUnit 5
- Lombok
- Jackson
- Logback
- JSON Schema Validator

## Prerequisites

- Java 17+
- Maven 3.6+

## CI (Continuous Integration)

This project uses GitHub Actions for continuous integration.

- Automatically runs on every push to `kata-api-test-automation`
- Can be triggered manually from the Actions tab
- Test report uploaded as artifact after each run

[![API Test Automation](https://github.com/Guruprakash-Mohan/API_Testing_Kata/actions/workflows/ci.yml/badge.svg?branch=kata-api-test-automation)](https://github.com/Guruprakash-Mohan/API_Testing_Kata/actions/workflows/ci.yml)

## How to Run

```bash
git clone https://github.com/Guruprakash-Mohan/API_Testing_Kata.git
cd API_Testing_Kata
git checkout kata-api-test-automation
mvn test
```

## Run by Tag

```bash
# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run negative validation tests
mvn test -Dcucumber.filter.tags="@negative"

# Run security tests
mvn test -Dcucumber.filter.tags="@security"

# Run end to end test
mvn test -Dcucumber.filter.tags="@e2e"

# Run data table scenario
mvn test -Dcucumber.filter.tags="@datatable"

# Run all except work in progress
mvn test -Dcucumber.filter.tags="not @wip"
```

## Framework Architecture

**Main (src/main/java/com/api/framework/)**
- `config/` — EnvironmentConfig loads properties per environment
- `constants/` — ApiConstants centralises endpoints and HTTP status codes
- `core/` — RestAssuredConfig bootstraps REST Assured base URI
- `specifications/` — RequestSpecifications provides base and authenticated specs
- `utils/` — DateUtils and ResponseValidator utilities

**Test (src/test/java/com/api/tests/)**
- `builders/` — BookingRequestBuilder follows Test Data Builder pattern
- `clients/` — AuthClient and BookingClient abstract all API interactions
- `hooks/` — TestHooks manages lifecycle, ScenarioContext shares data between steps
- `models/` — BookingRequest, BookingDates, BookingResponse models
- `runner/` — TestRunner configures Cucumber with JUnit 5
- `stepdefinitions/` — BookingStepDefinitions implements Gherkin steps

**Resources (src/test/resources/)**
- `features/booking/` — Separate feature file per functionality
- `schemas/` — JSON schema files for contract validation
- `config/` — Environment properties files

## Test Coverage

| Feature File | Tags | Description |
|-------------|------|-------------|
| `create_booking.feature` | @smoke @create | Create booking with valid data and Data Table |
| `retrieve_booking.feature` | @smoke @read | Retrieve booking by ID with schema validation |
| `delete_booking.feature` | @smoke @delete | Delete booking with valid authentication |
| `update_booking.feature` | @wip @update | Update booking — work in progress |
| `booking_authentication.feature` | @security | Unauthorized GET and DELETE return 403 |
| `booking_validation.feature` | @negative | Invalid data, missing fields, boundary values |
| `booking_e2e.feature` | @e2e @smoke | Full lifecycle — Create → Retrieve → Delete |

## Key Design Patterns

| Pattern | Where Used |
|---------|-----------|
| **Singleton** | ScenarioContext — shares data between steps |
| **Builder** | BookingRequestBuilder — constructs test data |
| **Factory** | RequestSpecifications — reusable request specs |
| **Page Object** | BookingClient, AuthClient — abstracts API calls |

## Test Design Techniques

| Technique | Where Applied |
|-----------|--------------|
| **Boundary Value Analysis** | Firstname length — testing 1, 2 characters at boundary |
| **Equivalence Partitioning** | Email validation — invalid.com, @test.com, plaintext, john@ |
| **Negative Testing** | Missing fields, invalid data |
| **Contract Testing** | JSON schema validation on create and retrieve responses |
| **End to End Testing** | Full booking lifecycle scenario |

## Notes

- Tests run against `https://automationintesting.online`
- Retry logic handles 409 conflicts in the shared environment
- Auth token is cached per scenario via ScenarioContext
- `@wip` tag excludes in-progress scenarios from runs
- Separate feature files follow Single Responsibility Principle

## Test Report

After running, HTML report available at:
```
target/cucumber-reports/cucumber.html
```
