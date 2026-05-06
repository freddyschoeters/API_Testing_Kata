package com.api.tests.builders;

import com.api.framework.utils.DateUtils;
import com.api.tests.models.request.BookingDates;
import com.api.tests.models.request.BookingRequest;

/**
 * Factory class for building BookingRequest test data.
 * Follows the Test Data Builder pattern.
 */
public class BookingRequestBuilder {

    private BookingRequestBuilder() {}

    /**
     * Returns a valid booking with sensible defaults.
     */
    public static BookingRequest validBooking() {
        return BookingRequest.builder()
                .roomid(1)
                .firstname("John")
                .lastname("Doe")
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(DateUtils.futureDate(30))
                        .checkout(DateUtils.futureDate(33))
                        .build())
                .email("john.doe+" + System.currentTimeMillis() + "@example.com")
                .phone("12345617890")
                .build();
    }
}