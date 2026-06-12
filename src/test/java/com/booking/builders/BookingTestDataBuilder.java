package com.booking.builders;

import com.booking.model.Booking;
import com.booking.model.BookingDates;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test Data Builder for {@link Booking}. Provides sensible valid defaults so
 * scenarios only need to override the fields relevant to what they test.
 */
public class BookingTestDataBuilder {

    private int roomid = 1;
    private String firstname = "Jim";
    private String lastname = "Brown";
    private boolean depositpaid = true;
    // Randomized so concurrent/repeated test runs don't collide on the same
    // room/date range as bookings left over from a previous run.
    private LocalDate checkin = LocalDate.now().plusDays(1 + ThreadLocalRandom.current().nextInt(10000));
    private LocalDate checkout = checkin.plusDays(2);
    private String email = "jim.brown@example.com";
    private String phone = "01234567890";

    public static BookingTestDataBuilder aBooking() {
        return new BookingTestDataBuilder();
    }

    public BookingTestDataBuilder withRoomid(int roomid) {
        this.roomid = roomid;
        return this;
    }

    public BookingTestDataBuilder withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public BookingTestDataBuilder withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public BookingTestDataBuilder withDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
        return this;
    }

    public BookingTestDataBuilder withCheckin(LocalDate checkin) {
        this.checkin = checkin;
        return this;
    }

    public BookingTestDataBuilder withCheckout(LocalDate checkout) {
        this.checkout = checkout;
        return this;
    }

    public BookingTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BookingTestDataBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public BookingTestDataBuilder withoutBookingdates() {
        this.checkin = null;
        this.checkout = null;
        return this;
    }

    public Booking build() {
        BookingDates dates = (checkin == null && checkout == null)
                ? null
                : new BookingDates().checkin(checkin).checkout(checkout);

        return new Booking()
                .roomid(roomid)
                .firstname(firstname)
                .lastname(lastname)
                .depositpaid(depositpaid)
                .bookingdates(dates)
                .email(email)
                .phone(phone);
    }
}
