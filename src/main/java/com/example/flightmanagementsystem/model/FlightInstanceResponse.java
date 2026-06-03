package com.example.flightmanagementsystem.model;

public record FlightInstanceResponse(
        String flightInstanceId,
        String flightNumber,
        String initialDepartureTime,
        int flightHours
) {
}
