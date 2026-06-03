package com.example.flightmanagementsystem.model;

public record CreateFlightInstanceRequest(
        String flightNumber,
        String initialDepartureTime,
        Integer flightHours
) {
}
