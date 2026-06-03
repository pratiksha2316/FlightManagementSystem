package com.example.flightmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FlightInstanceTest {

    @Test
    void createShouldGenerateFlightInstanceIdUsingFlightNumberAndDepartureTime() {
        FlightInstance flightInstance = FlightInstance.create("AI101", "2026-06-04 10:30", 3);

        assertEquals("AI101_2026-06-04 10:30", flightInstance.flightInstanceId());
        assertEquals("AI101", flightInstance.flightNumber());
        assertEquals("2026-06-04 10:30", flightInstance.initialDepartureTime());
        assertEquals(3, flightInstance.flightHours());
    }

    @Test
    void generateFlightInstanceIdShouldUseUnderscoreSeparator() {
        String flightInstanceId = FlightInstance.generateFlightInstanceId("AI101", "2026-06-04 10:30");

        assertEquals("AI101_2026-06-04 10:30", flightInstanceId);
    }
}
