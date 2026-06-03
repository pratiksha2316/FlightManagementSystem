package com.example.flightmanagementsystem.dto;

public record FlightInstanceResponseDto(
        String flightInstanceId,
        String flightNumber,
        String initialDepartureTime,
        int flightHours
) {
}
