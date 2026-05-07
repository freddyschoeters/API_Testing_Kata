package com.api.tests.clients;

import com.api.framework.constants.ApiConstants;
import com.api.framework.specifications.RequestSpecifications;
import com.api.tests.models.request.BookingRequest;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client for booking API operations.
 */
public class BookingClient {

    private static final Logger log = LoggerFactory.getLogger(BookingClient.class);

    public Response createBooking(BookingRequest request) {
        log.info("POST {} body={}", ApiConstants.BOOKING_ENDPOINT, request);
        return RequestSpecifications.baseSpec()
                .body(request)
                .post(ApiConstants.BOOKING_ENDPOINT);
    }

    public Response getBookingById(int bookingId, String token) {
        log.info("GET /booking/{}", bookingId);
        return RequestSpecifications.authenticatedSpec(token)
                .pathParam("id", bookingId)
                .get(ApiConstants.BOOKING_BY_ID);
    }
}