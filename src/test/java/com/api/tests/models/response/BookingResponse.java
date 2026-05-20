package com.api.tests.models.response;

import com.api.tests.models.request.BookingRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Response model returned after creating a booking.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {

    @JsonProperty("bookingid")
    private Integer bookingid;

    @JsonProperty("booking")
    private BookingRequest booking;
}