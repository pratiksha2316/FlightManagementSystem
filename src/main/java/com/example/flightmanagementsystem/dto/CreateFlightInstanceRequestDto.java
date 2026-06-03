package com.example.flightmanagementsystem.dto;

public record CreateFlightInstanceRequestDto(
        String flightNumber,
        String initialDepartureTime,
        Integer flightHours
) {
}
