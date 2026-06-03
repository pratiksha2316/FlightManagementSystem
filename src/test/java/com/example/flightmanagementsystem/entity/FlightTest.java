package com.example.flightmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FlightTest {

    @Test
    void shouldCreateFlightWithFlightNumberAndTotalSeats() {
        Flight flight = new Flight("AI101", 180);

        assertEquals("AI101", flight.flightNumber());
        assertEquals(180, flight.totalSeats());
    }
}
