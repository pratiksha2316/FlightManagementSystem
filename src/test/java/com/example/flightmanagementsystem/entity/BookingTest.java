package com.example.flightmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void createShouldGenerateBookingIdAndStoreBookingDetails() {
        Booking booking = Booking.create(
                "AI101_2026-06-04 10:30",
                2,
                List.of("Alice", "Bob")
        );

        assertFalse(booking.bookingId().isBlank());
        assertEquals("AI101_2026-06-04 10:30", booking.flightInstanceId());
        assertEquals(2, booking.numberOfSeats());
        assertEquals(List.of("Alice", "Bob"), booking.passengerNames());
    }

    @Test
    void constructorShouldCreateImmutableCopyOfPassengerNames() {
        List<String> passengerNames = new ArrayList<>(List.of("Alice", "Bob"));

        Booking booking = new Booking("booking-1", "AI101_2026-06-04 10:30", 2, passengerNames);
        passengerNames.add("Charlie");

        assertEquals(List.of("Alice", "Bob"), booking.passengerNames());
        assertThrows(UnsupportedOperationException.class, () -> booking.passengerNames().add("Charlie"));
    }
}
