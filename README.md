# Kata API Testing in Java

API Testing and Java Exercise: Setting up a Basic API Test Automation Framework.

## Objective

The objective of this exercise is to evaluate your knowledge on API testing and Java by setting up a basic API Test
Automation framework using Rest-Assured and Cucumber. You will need to create a test suite that executes a few tests
against one endpoint of a hotel booking website and evaluates their responses.

## Background

The application under test is a simple hotel booking website where you can book a room and also send a form with a
request.

The website can be accessed at https://automationintesting.online/.

The Swagger documentation for the two endpoints you will be testing can be found at:

Booking endpoint: https://automationintesting.online/booking/swagger-ui/index.html  
Optionally, you also have the Authentican endpoint: https://automationintesting.online/auth/swagger-ui/index.html

### Swagger

This website is an external application which is not in our control.  
We noticed that the Swagger documentation is sometimes not available on the mentioned URL above.  
As a backup, you can find the Swagger documentation in this repository
at [src/test/resources/spec/booking.yaml](src/test/resources/spec/booking.yaml)

The Open API Spec file is only supported in the Ultimate version of IntelliJ IDEA. But you can copy the content of the
file and paste it in an online Swagger editor like https://editor.swagger.io/ to visualize the API documentation.

### Authentication

In order to authenticate yourself, the required credentials are:

* Username: `admin`
* Password: `password`

## Task

You are provided with an extremely basic API test project.

Please clone the project and create a new branch with your name. At the end, please push your branch to this project.

The project to start from, can be found here: https://github.com/freddyschoeters/API_Testing_kata

Your task is to set up an API Test Automation framework from this project using Java, Rest-Assured, and Cucumber (feel
free to add more dependencies if required).

It is up to you to define the test cases. You don’t need to have a full coverage, but you need to show enough variation
on the types of tests that you would need to write and execute, and what to check in the response.

This kata has the purpose to evaluate both your technical skills as well as your testing skills.

`For this task, you will use the booking endpoint.`

## Requirements

* Use Java as the programming language
* Use Rest-Assured as the API testing library
* Use Cucumber as the BDD framework
* Design your codebase using a proper Java design pattern
* Write good tests with correct checks
* Use Git for version control and push your codebase to an open GitHub repository
* Make regular commits to demonstrate your progress

## Deliverables

* Your branch pushed in the provided project.
* A comprehensive test suite covering the scenarios mentioned above
* A well-structured codebase with proper design patterns and comments
* Regular commits demonstrating your progress

## Evaluation Criteria

* Being able to successfully run the tests
* Correctness and completeness of the test suite
* Quality of the codebase (design patterns, structure, code quality, …)
* Use of Rest-Assured and Cucumber features
* Commit history and progress demonstration

---

## Test Framework

### Running the tests

```
mvn clean test
```

This generates the model/DTO sources, compiles the project and runs the full Cucumber suite against the live API
at https://automationintesting.online/api. A Cucumber HTML/JSON report is written to `target/cucumber-reports.html` and
`target/cucumber.json`.

Before any scenario runs, a `@BeforeAll` hook checks the booking API's health endpoint and fails the build fast (with a
clear error message) if it isn't reporting `UP`.

### Continuous Integration

A GitHub Actions workflow (`.github/workflows/api-test.yaml`) runs `mvn clean test` on every push and pull request to
`main`, and uploads the Cucumber HTML/JSON report as a build artifact.

### Architecture & design patterns

The framework lives under `src/test/java/com/booking`:

* **Test Data Builder** (`builders/BookingTestDataBuilder`) — fluent builder providing a valid default booking (
  `aBooking()`), with `.withX(...)` overrides (including `.withoutBookingdates()`) used to construct invalid and
  boundary-value payloads for negative tests.
* **Service/Facade objects** (`client/AuthApiClient`, `client/BookingApiClient`) — wrap all Rest-Assured calls for the
  `/auth` and `/booking` resources (login, create/get/update/patch/delete/search booking, health check), hiding endpoint
  paths, query parameters, headers and cookies from the step definitions.
* **Mappers** (`mappers/BookingMapper`, `mappers/AuthMapper`) — small pure functions that extract typed values (booking
  id, nested booking, auth token) from raw Rest-Assured `Response` objects, keeping that translation logic out of the
  step definitions.
* **Singleton configuration** (`config/ApiConfig`) — loads `src/test/resources/config.properties` once and builds the
  shared Rest-Assured `RequestSpecification` (base URI, content type, Jackson `ObjectMapper` configured with
  `JavaTimeModule` for `LocalDate` fields).
* **Shared scenario context via dependency injection** (`context/TestContext`, wired through `cucumber-picocontainer`) —
  a generic `String`-keyed `set`/`get` store carrying values such as the auth token, the created booking id/body, and
  the latest API response between step definitions within a scenario.
* **Hooks** (`hooks/Hooks`) — a `@BeforeAll` hook verifies the booking API is up before the suite runs, and an `@After`
  hook deletes any booking created during a scenario, keeping the live API clean between runs.

Feature files live in `src/test/resources/features/`:

* `health.feature` — the booking API health check.
* `authentication.feature` — login with valid and invalid credentials.
* `booking-creation.feature` — valid booking creation, validation errors, conflicting/equal check-in and check-out
  dates, and boundary values for firstname, phone, email and missing mandatory fields.
* `booking-lifecycle.feature` — retrieving, updating and deleting an existing booking with and without authentication, a
  check that `PATCH` is not supported, and searching for bookings by room.

### Code generation plugins

Two Maven plugins generate model/DTO classes at `generate-test-sources`, registered as test source roots via
`build-helper-maven-plugin`:

* **`openapi-generator-maven-plugin`** generates the `Booking` and `BookingDates` model classes (`com.booking.model`)
  directly from the OpenAPI spec at `src/test/resources/spec/booking.yaml`, used both as the request body for
  create/update and to deserialize responses.
* **`jsonschema2pojo-maven-plugin`** generates the remaining request/response DTOs (`com.booking.model.dto`) from
  hand-written JSON Schemas in `src/test/resources/schemas/`: `AuthRequest`, `AuthResponse`, `BookingWrapperResponse`,
  `BookingSearchResponse`, `ValidationErrorResponse`, `ErrorResponse`, `HealthStatus`, and `PatchBookingRequest`.
  `BookingWrapperResponse` and `BookingSearchResponse` reuse the generated `Booking` type via `existingJavaType` so both
  plugins' output compose cleanly.

