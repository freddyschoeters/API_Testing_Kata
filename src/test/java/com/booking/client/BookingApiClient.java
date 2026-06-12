package com.booking.client;

import com.booking.config.ApiConfig;
import com.booking.model.Booking;
import com.booking.model.dto.PatchBookingRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Service object wrapping the /booking endpoint and its health check.
 */
public class BookingApiClient {

    private static final String TOKEN_COOKIE = "token";

    private final RequestSpecification requestSpec;

    public BookingApiClient() {
        this.requestSpec = ApiConfig.getInstance().baseRequestSpec();
    }

    public Response create(Booking booking) {
        return given().spec(requestSpec)
                .body(booking)
                .when()
                .post("/booking");
    }

    public Response getById(int id, String token) {
        return authenticated(token)
                .when()
                .get("/booking/{id}", id);
    }

    public Response update(int id, Booking booking, String token) {
        return authenticated(token)
                .body(booking)
                .when()
                .put("/booking/{id}", id);
    }

    public Response patch(int id, PatchBookingRequest patch, String token) {
        return authenticated(token)
                .body(patch)
                .when()
                .patch("/booking/{id}", id);
    }

    public Response delete(int id, String token) {
        return authenticated(token)
                .when()
                .delete("/booking/{id}", id);
    }

    public Response search(int roomId, String token) {
        return authenticated(token)
                .queryParam("roomid", roomId)
                .when()
                .get("/booking");
    }

    public Response health() {
        return given().spec(requestSpec)
                .when()
                .get("/booking/actuator/health");
    }

    private RequestSpecification authenticated(String token) {
        RequestSpecification spec = given().spec(requestSpec);
        if (token != null) {
            spec = spec.cookie(TOKEN_COOKIE, token);
        }
        return spec;
    }
}
