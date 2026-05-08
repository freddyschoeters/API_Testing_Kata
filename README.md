# Booking API Test Automation

Automated API tests for the Restful Booker platform.

## Tools Used

- Java 17
- REST Assured
- Cucumber
- JUnit 5
- Maven

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
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@negative"
mvn test -Dcucumber.filter.tags="@security"
mvn test -Dcucumber.filter.tags="@datatable"
```

## Test Scenarios

**Smoke Tests**
- Create booking with valid data
- Create booking using data table
- Get booking by ID
- Delete booking

**Security Tests**
- Get booking without authentication
- Delete booking without authentication

**Negative Tests**
- Create booking with short firstname and lastname
- Create booking with invalid dates
- Create booking with invalid email (Scenario Outline)
- Create booking with short phone number

**Work In Progress**
- Update booking (marked @wip)

## Test Report

After running, HTML report is available at:
```
target/cucumber-reports/cucumber.html
```

## Notes

- Tests run against https://automationintesting.online
- Retry logic handles conflicts in the shared environment
- Framework follows BDD approach with Cucumber and Gherkin