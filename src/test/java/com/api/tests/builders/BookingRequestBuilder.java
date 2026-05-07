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

    private static int getUniqueOffset() {
        // 700-900 days ahead = safely in 2028!
        return (int)(System.currentTimeMillis() % 200) + 700;
    }

    public static BookingRequest validBooking() {
        int offset = getUniqueOffset();
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

    public static BookingRequest updatedBooking(String checkin, String checkout) {
        return BookingRequest.builder()
                .roomid(1)
                .firstname("UpdatedName")
                .lastname("UpdatedLastname")
                .depositpaid(false)
                .bookingdates(BookingDates.builder()
                        .checkin(checkin)
                        .checkout(checkout)
                        .build())
                .email("updated+" + System.currentTimeMillis() + "@example.com")
                .phone("12345617890")
                .build();
    }

    public static BookingRequest invalidFirstAndLastnameBooking(String firstname, String lastname) {
        int offset = (int)(System.currentTimeMillis() % 200) + 200;
        return BookingRequest.builder()
                .roomid(new Random().nextInt(100) + 1)
                .firstname(firstname)
                .lastname(lastname)
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(DateUtils.futureDate(offset))
                        .checkout(DateUtils.futureDate(offset + 3))
                        .build())
                .email("test+" + System.currentTimeMillis() + "@example.com")
                .phone("12345617890")
                .build();
    }

    public static BookingRequest invalidDatesBooking(String checkin, String checkout) {
        int offset = getUniqueOffset();
        return BookingRequest.builder()
                .roomid(new Random().nextInt(900) + 100)
                .firstname("John")
                .lastname("Doe")
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(checkin)
                        .checkout(checkout)
                        .build())
                .email("test+" + System.currentTimeMillis() + "@example.com")
                .phone("12345617890")
                .build();
    }

    public static BookingRequest invalidEmail(String email) {
        int offset = getUniqueOffset();
        return BookingRequest.builder()
                .roomid(new Random().nextInt(900) + 100)
                .firstname("John")
                .lastname("Doe")
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(DateUtils.futureDate(offset))
                        .checkout(DateUtils.futureDate(offset + 3))
                        .build())
                .email(email)
                .phone("12345617890")
                .build();
    }

    public static BookingRequest invalidPhonenumber(String phonenumber) {
        int offset = getUniqueOffset();
        return BookingRequest.builder()
                .roomid(new Random().nextInt(900) + 100)
                .firstname("John")
                .lastname("Doe")
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(DateUtils.futureDate(offset))
                        .checkout(DateUtils.futureDate(offset + 3))
                        .build())
                .email("test+" + System.currentTimeMillis() + "@example.com")
                .phone(phonenumber)
                .build();
    }
}