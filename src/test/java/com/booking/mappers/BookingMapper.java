package com.booking.mappers;

import com.booking.model.Booking;
import com.booking.model.dto.BookingWrapperResponse;
import io.restassured.response.Response;

/**
 * Maps raw Rest-Assured responses to the {@link Booking} model.
 */
public final class BookingMapper {

    private BookingMapper() {
    }

    /**
     * Extracts the {@link Booking} nested under the {@code booking} field of a
     * {@link BookingWrapperResponse}, as returned by the update endpoint.
     */
    public static Booking fromWrapperResponse(Response response) {
        return response.as(BookingWrapperResponse.class).getBooking();
    }

    /**
     * Extracts the {@code bookingid} field from a create/get booking response.
     */
    public static Integer bookingIdFromResponse(Response response) {
        return response.jsonPath().getInt("bookingid");
    }
}
