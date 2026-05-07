package com.api.tests.builders;

import com.api.framework.utils.DateUtils;
import com.api.tests.models.request.BookingDates;
import com.api.tests.models.request.BookingRequest;
import java.util.Random;

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
        int offset = (int)(System.currentTimeMillis() % 200) + 700;
        return BookingRequest.builder()
                .roomid(new Random().nextInt(900) + 100)
                .firstname("John")
                .lastname("Doe")
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(DateUtils.futureDate(offset))
                        .checkout(DateUtils.futureDate(offset + 3))
                        .build())
                .email("john.doe+" + System.currentTimeMillis() + "@example.com")
                .phone("12345617890")
                .build();
    }
}